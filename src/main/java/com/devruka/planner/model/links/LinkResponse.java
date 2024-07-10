package com.devruka.planner.model.links;

import java.util.UUID;

public record LinkResponse(
        UUID link_id,
        String title,
        String url
) {
}
