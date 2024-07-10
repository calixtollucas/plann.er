package com.devruka.planner.model.participant;

import java.util.UUID;

public record ParticipantDTO(
        UUID id,
        String name,
        String email,
        Boolean isConfirmed
) {
}
