package com.devruka.planner.model.activity;

import java.time.LocalDateTime;
import java.util.UUID;

public record ActivityDTO(
        UUID activityId,
        String title,
        LocalDateTime occurs_at
) {
}
