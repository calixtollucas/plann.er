package com.devruka.planner.model.trip;

import jakarta.validation.constraints.*;

import java.util.List;

public record TripRequestPayload(
        @NotBlank(message = "destination cannot be null")
        String destination,

        String starts_at,

        String ends_at,

        @NotNull(message = "emails_to_invite should not be null")
        List<String> emails_to_invite,

        @NotBlank(message = "email cannot be blank")
        @Email(message = "invalid email format, should be email@email.domain")
        String owner_email,

        @NotBlank(message = "owner_name cannot be null")
        String owner_name
) {
}
