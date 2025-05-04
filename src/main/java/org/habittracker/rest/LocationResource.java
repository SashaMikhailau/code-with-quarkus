package org.habittracker.rest;

import com.fasterxml.jackson.annotation.JsonView;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.habittracker.auth.UserRoles;
import org.habittracker.contentful.ContentFulService;
import org.habittracker.contentful.beans.BattleGround;
import org.habittracker.contentful.beans.Location;
import org.habittracker.jackson.JsonViews;

import java.util.Collection;

@Path("/locations")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
public class LocationResource {

    @Inject
    ContentFulService contentFulService;

    @GET
    @JsonView(JsonViews.GlobalMapView.class)
    public Collection<Location> getAllLocations() {
        return contentFulService.getAllItems(Location.class);
    }

    @GET
    @Path("/{id}")
    @JsonView(JsonViews.LocalMapView.class)
    public Location getLocationById(@PathParam("id") String id) {
        return contentFulService.getLocationById(id)
                .orElseThrow(() -> new NotFoundException("Location not found for id: " + id));
    }

    @GET
    @Path("/battleground/{id}")
    @JsonView(JsonViews.BattleGroundView.class)
    public BattleGround getBattleGroundById(@PathParam("id") String id) {
        return contentFulService.getBattleGroundById(id)
                .orElseThrow(() -> new NotFoundException("Battleground not found for id: " + id));
    }
}