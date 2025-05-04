package org.habittracker.dto;

import java.util.List;

public record PlayerDTO(List<String> selectedSkillIds,
                        String locationId,
                        String battleGroundId,
                        int utcEndOfDayHour,
                        boolean isSolo,
                        boolean isPartyLeader) {
}
