package com.easyride.bus.dto.request;

public record StationSearchRequest(
        String stationName,
        String longitude,
        String latitude
) {

}
