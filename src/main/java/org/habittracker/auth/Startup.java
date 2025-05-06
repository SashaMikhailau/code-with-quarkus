package org.habittracker.auth;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.habittracker.beans.Party;
import org.habittracker.beans.Player;
import org.habittracker.beans.User;

@Singleton
public class Startup {
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // Add users only if they don't exist
        if (User.find("username", "admin").count() == 0) {
            User.add("admin", "admin", UserRoles.ADMIN);
        }
        if (User.find("username", "user").count() == 0) {
            User.add("user", "user", UserRoles.USER);
        }
        if (User.find("username", "qa").count() == 0) {
            User.add("qa", "qa", UserRoles.USER);
        }

        // Add players only if they don't exist
        if (Player.find("username", "user").count() == 0) {
            Player.persist(new Player("user", "user"));
        }
        if (Player.find("username", "admin").count() == 0) {
            Player.persist(new Player("admin", "admin"));
        }
        if (Player.find("username", "qa").count() == 0) {
            Player.persist(new Player("qa", "qa"));
        }

        // Set up parties for players who don't have one
        Player.streamAll().map(entity -> ((Player) entity)).forEach(player -> {
            if (player.getParty() == null) {
                Party party = new Party(player, player.getUtcEndOfDayHour(), true);
                party.persist();
                player.setParty(party);
                player.persist();
            }
        });
    }
}