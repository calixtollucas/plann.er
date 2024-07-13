package com.devruka.planner.model.links;

import jakarta.validation.constraints.NotBlank;

public record LinkRequestPayload(
        @NotBlank(message = "the title cannot be blank or null")
        String title,
        @NotBlank(message = "the url cannot be blank or null")
        String url
) {
}
