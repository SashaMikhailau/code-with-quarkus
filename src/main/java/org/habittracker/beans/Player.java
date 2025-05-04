package org.habittracker.beans;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "player")
@NoArgsConstructor
public class Player extends PanacheEntityBase {
    @Id
    private String userId;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "party_id", referencedColumnName = "id")
    private Party party;

    public int utcEndOfDayHour = 0;

    @ElementCollection(fetch = FetchType.EAGER)
    public List<String> selectedSkillIds = new ArrayList<>();


    public Player(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public void onExitLocation() {
        this.selectedSkillIds.clear();
        this.persist();
    }

    public static List<Player> getByPartyId(String partyId) {
        return list("party_id", partyId);
    }
}

