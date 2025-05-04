package org.habittracker.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.SecurityContext;
import org.habittracker.auth.UserRoles;

@Path("/party")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
public class PartyResource {

    private String getCurrentUser(SecurityContext ctx) {
        return ctx.getUserPrincipal().getName();
    }


}
