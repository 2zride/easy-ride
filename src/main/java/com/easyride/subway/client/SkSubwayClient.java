package com.easyride.subway.client;

import com.easyride.subway.client.dto.SkRealTimeCongestionResponse;
import com.easyride.subway.domain.SubwayCongestion;
import java.util.Map;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SkSubwayClient {

    private static final String PATH_OF_REAL_TIME_CONGESTION = "/congestion/rltm/trains/{subwayLine}/{trainY}";

    private final RestClient restClient;
    private final SkResponseConverter responseConverter;

    public SkSubwayClient(RestClient.Builder restClientBuilder) { // TODO 두개의 빈 중 하나 선택
        this.restClient = restClientBuilder.build();
        this.responseConverter = SkResponseConverter.getInstance();
    }

    public SubwayCongestion fetchRealTimeCongestion(int subwayLine, String trainY) {
        SkRealTimeCongestionResponse response = restClient.get()
                .uri(makeRealTimeCongestionUri(subwayLine, trainY))
                .exchange((req, res) -> responseConverter.convert(res));
        return response.toSubwayCongestion();
    }

    private String makeRealTimeCongestionUri(int subwayLine, String trainY) {
        return UriComponentsBuilder.fromUriString(PATH_OF_REAL_TIME_CONGESTION)
                .uriVariables(Map.of(
                        "subwayLine", subwayLine,
                        "trainY", trainY))
                .build(false)
                .toUriString();
    }
}
