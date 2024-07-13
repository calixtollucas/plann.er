package com.devruka.planner.model.participant;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ParticipantRequestPayload(
        String name,
        @Email(message = "invalid email format")
        @NotNull(message = "the email cannot be blank or null")
        @NotBlank(message = "the email cannot be blank or null")
        String email
) {
}
