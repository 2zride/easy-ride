package com.easyride.bus.dto.response;

import com.easyride.bus.domain.StationInfo;
import java.util.Optional;

public record StationSearchResponse(
        Optional<String> code,
        Optional<String> message,
        Optional<StationInfo> stationInfo
) {

}
