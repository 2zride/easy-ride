package com.easyride.subway.client.odsay;

import com.easyride.subway.client.dto.OdsaySearchStationResponse;
import com.easyride.subway.client.dto.OdsayStationInfoResponse;
import com.easyride.subway.client.dto.OdsayStationPathResponse;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayPath;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@EnableConfigurationProperties(OdsayProperty.class)
@Component
public class OdsaySubwayClient {

    private static final String PATH_OF_SEARCH_STATION = "/searchStation";
    private static final String PATH_OF_FETCH_STATION_INFO = "/subwayStationInfo";
    private static final String PATH_OF_FETCH_SUBWAY_PATH = "/subwayPath";
    private static final int STATION_CLASS_OF_SUBWAY = 2;
    private static final int SEARCH_PATH_CONDITION_OF_MIN_EXCHANGE = 2;

    private final RestClient restClient;
    private final OdsayResponseConverter responseConverter;

    public OdsaySubwayClient(RestClient.Builder restClientBuilder, OdsayProperty property) {
        this.restClient = restClientBuilder
                .baseUrl(baseUri(property))
                .build();
        this.responseConverter = OdsayResponseConverter.getInstance();
    }

    private String baseUri(OdsayProperty property) {
        return UriComponentsBuilder.fromUriString(property.baseUrl())
                .queryParam("apiKey", property.apiKey())
                .toUriString();
    }

    public SubwayStations searchStation(String stationName) {
        OdsaySearchStationResponse response = restClient.get()
                .uri(makeSearchStationUri(stationName))
                .exchange((req, res) -> responseConverter.convert(res, OdsaySearchStationResponse.class));
        return response.toSubwayStations();
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
                .exchange((req, res) -> responseConverter.convert(res, OdsayStationInfoResponse.class));
        return response.toNearSubwayStations();
    }

    private String makeStationInfoUri(String stationId) {
        return UriComponentsBuilder.newInstance()
                .path(PATH_OF_FETCH_STATION_INFO)
                .queryParam("stationID", stationId)
                .build(false)
                .toUriString();
    }

    public SubwayPath fetchSubwayPath(Subway subway) {
        SubwayStation startStation = subway.getNowStation();
        SubwayStation endStation = subway.getEndStation();
        OdsayStationPathResponse response = restClient.get()
                .uri(makeSubwayPathUri(startStation.getId(), endStation.getId()))
                .exchange((req, res) -> responseConverter.convert(res, OdsayStationPathResponse.class));
        return response.toSubwayPath(subway);
    }

    private String makeSubwayPathUri(String startStationId, String endStationId) {
        return UriComponentsBuilder.newInstance()
                .path(PATH_OF_FETCH_SUBWAY_PATH)
                .queryParam("CID", 1000)
                .queryParam("SID", startStationId)
                .queryParam("EID", endStationId)
                .queryParam("Sopt", SEARCH_PATH_CONDITION_OF_MIN_EXCHANGE)
                .build(false)
                .toUriString();
    }
}
