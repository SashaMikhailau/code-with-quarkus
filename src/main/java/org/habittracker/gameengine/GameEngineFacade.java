package org.habittracker.gameengine;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class GameEngineFacade {
    public List<GameEngineAction> calculateBattle(
            List<String> selectedSkills, String battleGroundId
    ) {
        return Collections.singletonList(new GameEngineAction());
    }
}
