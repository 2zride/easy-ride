package com.easyride.bus.dto.response;

import com.fasterxml.jackson.databind.JsonNode;
import java.util.Optional;

public record StationSearchResponse(
        Optional<String> code,
        Optional<String> message,
        Optional<JsonNode> response
) {

}
