package com.devruka.planner.model.activity;

import jakarta.validation.constraints.NotBlank;

public record ActivityRequestPayload(
        @NotBlank(message = "title cannot be blank or null")
        String title,
        String occurs_at
) {
}
