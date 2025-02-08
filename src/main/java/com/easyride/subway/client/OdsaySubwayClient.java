package com.easyride.subway.client;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.client.dto.OdsaySearchStationResponse;
import com.easyride.subway.client.dto.OdsayStationInfoResponse;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.exception.SubwayErrorCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OdsaySubwayClient {

    private static final String PATH_OF_SEARCH_STATION = "/searchStation";
    private static final String PATH_OF_FETCH_STATION_INFO = "/subwayStationInfo";
    private static final int STATION_CLASS_OF_SUBWAY = 2;

    private final RestClient restClient;

    public OdsaySubwayClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public SubwayStations searchStation(String stationName) {
        OdsaySearchStationResponse response = restClient.get()
                .uri(makeSearchStationUri(stationName))
                .retrieve()
                .body(OdsaySearchStationResponse.class);
        if (response.isError()) {
            throw new EasyRideException(SubwayErrorCode.ODSAY_API_ERROR);
        }
        return response.toDomain();
    }

    private String makeSearchStationUri(String stationName) {
        return UriComponentsBuilder.newInstance()
                .path(PATH_OF_SEARCH_STATION)
                .queryParam("stationName", stationName)
                .queryParam("stationClass", STATION_CLASS_OF_SUBWAY)
                .build(false)
                .toUriString();
    }

    public NearSubwayStations fetchStationInfo(String stationId) {
        OdsayStationInfoResponse response = restClient.get()
                .uri(makeStationInfoUri(stationId))
                .retrieve()
                .body(OdsayStationInfoResponse.class);
        if (response.isError()) {
            throw new EasyRideException(SubwayErrorCode.ODSAY_API_ERROR);
        }
        return response.toDomain();
    }

    private String makeStationInfoUri(String stationId) {
        return UriComponentsBuilder.newInstance()
                .path(PATH_OF_FETCH_STATION_INFO)
                .queryParam("stationID", stationId)
                .build(false)
                .toUriString();
    }
}
