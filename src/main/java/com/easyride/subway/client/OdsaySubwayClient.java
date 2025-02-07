package com.easyride.subway.client;

import com.easyride.subway.client.dto.OdsaySearchStationResponse;
import com.easyride.subway.domain.SubwayStation;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OdsaySubwayClient {

    private static final String PATH_OF_SEARCH_STATION = "/searchStation";
    private static final int STATION_CLASS_OF_SUBWAY = 2;

    private final RestClient restClient;

    public OdsaySubwayClient(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder.build();
    }

    public List<SubwayStation> searchStation(String stationName) {
        OdsaySearchStationResponse response = restClient.get()
                .uri(makeSearchStationUri(stationName))
                .retrieve()
                .body(OdsaySearchStationResponse.class);
        if (response.isError()) {
            throw new RuntimeException("오디세이 API 호출 과정에서 예외가 발생했습니다."); // TODO 커스텀 예외로 변경
        }
        return response.toDomains();
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
