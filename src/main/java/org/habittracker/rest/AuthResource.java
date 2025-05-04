package org.habittracker.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.habittracker.auth.UserRoles;

@RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
@Path("/auth")
public class AuthResource {

    @GET
    public Response checkCredentials(@Context SecurityContext ctx) {
        return Response.ok(UserRoles.getRoleName(ctx)).build();
    }
}
