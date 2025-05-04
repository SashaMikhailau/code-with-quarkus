package org.habittracker.auth;

import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.habittracker.beans.Party;
import org.habittracker.beans.Player;

@Singleton
public class Startup {
    @Transactional
    public void loadUsers(@Observes StartupEvent evt) {
        // reset and load all test users
        /*User.deleteAll();
        User.add("admin", "admin", UserRoles.ADMIN);
        User.add("user", "user", UserRoles.USER);*/
        //Player.persist(new Player("user", "user"));
        //Player.persist(new Player("admin", "admin"));
        //User.add("qa", "qa", UserRoles.USER);
        //Player.persist(new Player("qa", "qa"));
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