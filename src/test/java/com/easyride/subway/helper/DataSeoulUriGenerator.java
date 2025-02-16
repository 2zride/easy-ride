package com.easyride.subway.helper;

import com.easyride.subway.client.dataseoul.DataSeoulProperty;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class DataSeoulUriGenerator {

    @Autowired
    DataSeoulProperty dataSeoulProperty;

    public String makeRealTimeTrainPositionUri(String subwayName) {
        return UriComponentsBuilder.fromUriString(dataSeoulProperty.baseUrl())
                .path("/realtimePosition/{startIndex}/{endIndex}/{subwayName}")
                .uriVariables(Map.of(
                        "apiKey", dataSeoulProperty.apiKey(),
                        "startIndex", 0,
                        "endIndex", 100,
                        "subwayName", subwayName
                ))
                .toUriString();
    }
}
