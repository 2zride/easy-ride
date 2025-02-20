package com.easyride.subway.client.dataseoul;

import com.easyride.subway.client.dto.DataSeoulRealTimeSubwayResponse;
import com.easyride.subway.domain.StationLine;
import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@EnableConfigurationProperties(DataSeoulProperty.class)
@Component
public class DataSeoulSubwayClient {

    private static final String PATH_OF_REAL_TIME_SUBWAY_POSITION = "/realtimePosition/{startIndex}/{endIndex}/{subwayName}";
    private static final int DEFAULT_START_INDEX_OF_REAL_TIME_SUBWAY_POSITION = 0;
    private static final int DEFAULT_END_INDEX_OF_REAL_TIME_SUBWAY_POSITION = 100;

    private final RestClient restClient;
    private final DataSeoulResponseConverter responseConverter;

    public DataSeoulSubwayClient(RestClient.Builder restClientBuilder, DataSeoulProperty property) {
        this.restClient = restClientBuilder
                .baseUrl(baseUri(property))
                .build();
        this.responseConverter = DataSeoulResponseConverter.getInstance();
    }

    private String baseUri(DataSeoulProperty property) {
        return UriComponentsBuilder.fromUriString(property.baseUrl())
                .uriVariables(Map.of(
                        "apiKey", property.apiKey()
                ))
                .toUriString();
    }

    public DataSeoulRealTimeSubwayResponse fetchRealTimeSubwaysByLine(StationLine stationLine) {
        String stationLineName = DataSeoulStationLineMapper.asStationLineName(stationLine);
        return restClient.get()
                .uri(makeRealTimeSubwayPositionUri(stationLineName))
                .exchange((req, res) -> responseConverter.convert(res));
    }

    private String makeRealTimeSubwayPositionUri(String stationLineName) {
        return UriComponentsBuilder.fromUriString(PATH_OF_REAL_TIME_SUBWAY_POSITION)
                .uriVariables(Map.of(
                        "startIndex", DEFAULT_START_INDEX_OF_REAL_TIME_SUBWAY_POSITION,
                        "endIndex", DEFAULT_END_INDEX_OF_REAL_TIME_SUBWAY_POSITION,
                        "subwayName", stationLineName
                ))
                .build(false)
                .toUriString();
    }
}
