package com.easyride.subway.client.dataseoul;

import com.easyride.subway.client.dto.DataSeoulRealTimeTrainPositionResponse;
import com.easyride.subway.domain.SubwayPosition;
import java.util.List;
import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@EnableConfigurationProperties(DataSeoulProperty.class)
@Component
public class DataSeoulSubwayClient {

    private static final String PATH_OF_REAL_TIME_TRAIN_POSITION = "/realtimePosition/{startIndex}/{endIndex}/{subwayName}";

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

    public List<SubwayPosition> fetchRealTimeTrainPosition(String subwayName) {
        DataSeoulRealTimeTrainPositionResponse response = restClient.get()
                .uri(makeRealTimeTrainPositionUri(subwayName))
                .exchange((req, res) -> responseConverter.convert(res));
        return response.toSubwayPositions();
    }

    private String makeRealTimeTrainPositionUri(String subwayName) {
        return UriComponentsBuilder.fromUriString(PATH_OF_REAL_TIME_TRAIN_POSITION)
                .uriVariables(Map.of(
                        "startIndex", 0, // TODO change
                        "endIndex", 60, // TODO change
                        "subwayName", subwayName
                ))
                .build(false)
                .toUriString();
    }
}
