package org.habittracker.beans;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "habits")
@Getter
@Setter
@NoArgsConstructor
public class Habit extends PanacheEntity {
    private String userId;
    private String title;
    private String note;
    private String type;
    private boolean completed;
    private int streak;
    private int complexity;

    public Habit(String userId, String title, String type) {
        this.userId = userId;
        this.title = title;
        this.type = type;
        this.completed = false;
    }
}