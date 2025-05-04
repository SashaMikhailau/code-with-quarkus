package org.habittracker.rest;

import jakarta.annotation.security.RolesAllowed;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.habittracker.auth.UserRoles;
import org.habittracker.beans.Habit;

import java.util.List;

@Path("/habits")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({UserRoles.ADMIN, UserRoles.USER})
public class HabitResource {

    private String getCurrentUser(SecurityContext ctx) {
        return ctx.getUserPrincipal().getName();
    }

    // Create a habit: POST /habits
    @POST
    @Transactional
    public Response createHabit(@Context SecurityContext ctx, Habit habit) {
        habit.setUserId(getCurrentUser(ctx));
        Habit.persist(habit);
        return Response.status(Response.Status.CREATED).entity(getHabits(ctx)).build();
    }

    // Update a habit: PUT /habits/{id}
    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateHabit(
            @Context SecurityContext ctx,
            @PathParam("id") Long id,
            Habit updatedHabit) {
        Habit existingHabit = Habit.findById(id);
        if (existingHabit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!existingHabit.getUserId().equals(getCurrentUser(ctx))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        existingHabit.setTitle(updatedHabit.getTitle());
        existingHabit.setNote(updatedHabit.getNote());

        return getAllUserHabits(ctx);
    }

    // Check/Uncheck habit: PUT /habits/{id}/state?completed=...
    @PUT
    @Path("/{id}/state")
    @Transactional
    public Response updateHabitState(
            @Context SecurityContext ctx,
            @PathParam("id") Long id,
            @QueryParam("completed") boolean completed) {
        Habit habit = Habit.findById(id);
        if (habit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Ensure only the habit owner can update its state
        if (!habit.getUserId().equals(getCurrentUser(ctx))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        habit.setCompleted(completed);
        if (completed) {
            habit.setStreak(habit.getStreak() + 1);
        } else {
            habit.setStreak(habit.getStreak() - 1);
        }
        return getAllUserHabits(ctx);
    }

    private Response getAllUserHabits(SecurityContext ctx) {
        return Response.ok(getHabits(ctx)).build();
    }

    // Get all habits for a player: GET /habits
    @GET
    public List<Habit> getHabits(@Context SecurityContext ctx) {
        return Habit.find("userId", getCurrentUser(ctx)).list();
    }

    // Remove a habit: DELETE /habits/{id}
    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteHabit(@Context SecurityContext ctx, @PathParam("id") Long id) {
        Habit habit = Habit.findById(id);
        if (habit == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        // Only the habit owner can delete the habit.
        if (!habit.getUserId().equals(getCurrentUser(ctx))) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        habit.delete();
        return Response.ok(getHabits(ctx)).build();
    }
}