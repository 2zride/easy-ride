package com.easyride.bus.dto.response;

import java.util.List;

public record StationSearchResponse(
        long stationId,
        List<String> busNumbers
) {

}
