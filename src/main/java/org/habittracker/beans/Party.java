package org.habittracker.beans;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "party")
public class Party extends PanacheEntity {
    private String locationId;
    private String battleGroundId;
    private int utcEndOfDayHour;
    private boolean isSolo;
    private String partyLeaderID;

    public Party(@NotNull Player partyLeader, int utcEndOfDayHour, boolean isSolo) {
        this.partyLeaderID = partyLeader.getUserId();
        this.utcEndOfDayHour = utcEndOfDayHour;
        this.locationId = null;
        this.battleGroundId = null;
        this.isSolo = isSolo;
    }

    public void exitLocation() {
        this.locationId = null;
        this.battleGroundId = null;
        this.persist();
    }

    public boolean isPartyLeader(Player player) {
        return partyLeaderID.equals(player.getUserId());
    }
}

