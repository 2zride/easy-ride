package com.easyride.subway.helper;

import com.easyride.subway.client.sk.SkProperty;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class SkUriGenerator {

    @Autowired
    SkProperty skProperty;

    public String makeRealTimeCongestionUri(int subwayLine, String trainY) {
        return UriComponentsBuilder.fromUriString(skProperty.baseUrl())
                .path("/congestion/rltm/trains/{subwayLine}/{trainY}")
                .uriVariables(Map.of(
                        "subwayLine", subwayLine,
                        "trainY", trainY))
                .build(false)
                .toUriString();
    }
}
