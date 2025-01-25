package com.easyride.bus.dto.request;

import com.easyride.bus.domain.GeoLocation;

public record StationSearchRequest(
        String stationName,
        GeoLocation stationGeoLocation
) {

}
