package com.easyride.subway.client;

import com.easyride.subway.client.dto.OdsaySearchStationSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OdsaySubwayClient {

    private static final String PATH_OF_SEARCH_STATION = "/searchStation";
    private static final int STATION_CLASS_OF_SUBWAY = 2;

    private final RestClient restClient;

    public OdsaySearchStationSuccessResponse searchStation(String stationName) {
        return restClient.get()
                .uri(makeSearchStationUri(stationName))
                .retrieve()
                .body(OdsaySearchStationSuccessResponse.class);
    }

    private String makeSearchStationUri(String stationName) {
        return UriComponentsBuilder.newInstance()
                .path(PATH_OF_SEARCH_STATION)
                .queryParam("stationName", stationName)
                .queryParam("stationClass", STATION_CLASS_OF_SUBWAY)
                .build(false)
                .toUriString();
    }
}
