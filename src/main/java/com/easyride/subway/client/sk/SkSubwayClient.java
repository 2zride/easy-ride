package com.easyride.subway.client.sk;

import com.easyride.subway.client.dto.SkRealTimeCongestionResponse;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import java.util.Map;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@EnableConfigurationProperties(SkProperty.class)
@Component
public class SkSubwayClient {

    private static final String PATH_OF_REAL_TIME_CONGESTION = "/congestion/rltm/trains/{subwayLine}/{trainY}";

    private final RestClient restClient;
    private final SkResponseConverter responseConverter;

    public SkSubwayClient(RestClient.Builder restClientBuilder, SkProperty property) {
        this.restClient = restClientBuilder
                .baseUrl(property.baseUrl())
                .defaultHeader("appKey", property.apiKey())
                .build();
        this.responseConverter = SkResponseConverter.getInstance();
    }

    public SubwayCongestion fetchCongestion(Subway subway) {
        SkRealTimeCongestionResponse response = restClient.get()
                .uri(makeRealTimeCongestionUri(subway.stationLine().getNumber(), subway.getId()))
                .exchange((req, res) -> responseConverter.convert(res));
        return response.toSubwayCongestion(subway);
    }

    private String makeRealTimeCongestionUri(int subwayLine, String subwayId) {
        return UriComponentsBuilder.fromUriString(PATH_OF_REAL_TIME_CONGESTION)
                .uriVariables(Map.of(
                        "subwayLine", subwayLine,
                        "trainY", subwayId))
                .build(false)
                .toUriString();
    }
}
