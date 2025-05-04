package org.habittracker.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.apache.commons.lang3.ObjectUtils;
import org.habittracker.auth.UserRoles;
import org.habittracker.beans.Party;
import org.habittracker.beans.Player;
import org.habittracker.beans.User;
import org.habittracker.dto.LocationDTO;
import org.habittracker.dto.PlayerDTO;
import org.habittracker.contentful.ContentFulService;
import org.habittracker.contentful.beans.BattleGround;
import org.habittracker.contentful.beans.Location;
import org.habittracker.dto.SelectedSkillsDTO;
import org.habittracker.gameengine.GameEngineAction;
import org.habittracker.gameengine.GameEngineFacade;

import java.util.List;


@Path("/player")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({UserRoles.ADMIN, UserRoles.USER, UserRoles.QA})
public class PlayerResource {

    @Inject
    ContentFulService contentFulService;

    @Inject
    GameEngineFacade gameEngineFacade;

    private String getCurrentUser(SecurityContext ctx) {
        return ctx.getUserPrincipal().getName();
    }

    @GET
    public Response getCharacterData(@Context SecurityContext ctx) {
        String userId = getCurrentUser(ctx);
        Player player = Player.find("userId", userId).singleResult();
        if (player == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Party party = player.getParty();
        var playerSnapshot = party != null ? (new PlayerDTO(player.getSelectedSkillIds(), party.getLocationId(), party.getBattleGroundId(), party.getUtcEndOfDayHour(), party.isSolo(), true)) :
                (
                        new PlayerDTO(player.getSelectedSkillIds(), null, null, 0, true, true)
                );
        return Response.ok(playerSnapshot).build();
    }

    @PUT
    @Path("/location")
    @Transactional
    public Response updateGlobalMove(@Context SecurityContext ctx, @QueryParam("locationId") String locationId) {
        Player player = Player.find("userId", getCurrentUser(ctx)).firstResult();
        if (player != null && player.getParty() != null) {
            return contentFulService.getLocationById(locationId)
                    .map(Location::getBattleGrounds)
                    .filter(battleGrounds -> !battleGrounds.isEmpty())
                    .map(List::getFirst)
                    .map(
                            startBattleground -> {
                                Party party = player.getParty();
                                party.setLocationId(locationId);
                                party.setBattleGroundId(startBattleground.getId());

                                return getPartyLocation(party);
                            }
                    ).orElseGet(() -> Response.status(Response.Status.NOT_FOUND).build());
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private static Response getPartyLocation(Party party) {
        return Response.ok(new LocationDTO(party.getLocationId(), party.getBattleGroundId())).build();
    }

    private static Response getSelectedSkills(Player player){
        return Response.ok(new SelectedSkillsDTO(player.getSelectedSkillIds())).build();
    }

    @PUT
    @Path("/skill/{skillId}")
    @Transactional
    public Response addSkill(@Context SecurityContext ctx, @PathParam("skillId") String skillId) {
        Player player = Player.findById(getCurrentUser(ctx));
        if (player == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!player.selectedSkillIds.contains(skillId)) {
            player.selectedSkillIds.add(skillId);
            player.persist();
        }

        return getSelectedSkills(player);
    }

    @DELETE
    @Path("/skill/{skillId}")
    @Transactional
    public Response removeSkill(@Context SecurityContext ctx, @PathParam("skillId") String skillId) {
        Player player = Player.findById(getCurrentUser(ctx));
        if (player == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        player.selectedSkillIds.remove(skillId);
        player.persist();
        return getSelectedSkills(player);
    }

    @PUT
    @Path("/location/nextroom")
    @RolesAllowed({UserRoles.ADMIN, UserRoles.QA})
    @Transactional
    public Response moveToNextBattleground(@Context SecurityContext ctx) {
        Player player = Player.find("userId", getCurrentUser(ctx)).firstResult();
        if (ObjectUtils.allNotNull(player, player.getParty(), player.getParty().getLocationId())) {
            Party party = player.getParty();
            return contentFulService.getLocationById(party.getLocationId())
                    .map(Location::getBattleGrounds)
                    .filter(battleGrounds -> battleGrounds != null && !battleGrounds.isEmpty())
                    .map(battleGrounds -> {
                        int currentIndex = battleGrounds.indexOf(
                                battleGrounds.stream()
                                        .filter(bg -> bg.getId().equals(party.getBattleGroundId()))
                                        .findFirst()
                                        .orElse(null)
                        );
                        if (currentIndex != -1) {
                            List<GameEngineAction> gameEngineActions = gameEngineFacade.calculateBattle(player.selectedSkillIds, party.getBattleGroundId());
                            player.selectedSkillIds.clear();
                            if (currentIndex < battleGrounds.size() - 1) {
                                BattleGround nextBattleground = battleGrounds.get(currentIndex + 1);
                                party.setBattleGroundId(nextBattleground.getId());
                            } else {
                                party.exitLocation();
                            }
                            return getPartyLocation(party);
                        } else {
                            player.onExitLocation();
                            return currentPLayerData(player);
                        }
                    })
                    .orElseGet(() -> {
                        player.onExitLocation();
                        return currentPLayerData(player);
                    });
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

    private static Response currentPLayerData(Player player) {
        return Response.ok(player).build();
    }
}
