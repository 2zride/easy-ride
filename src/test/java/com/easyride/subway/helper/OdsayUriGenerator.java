package com.easyride.subway.helper;

import com.easyride.subway.client.odsay.OdsayProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OdsayUriGenerator {

    @Autowired
    OdsayProperty odsayProperty;

    public String makeStationInfoUri(String stationId) {
        return UriComponentsBuilder.fromUriString(odsayProperty.baseUrl())
                .path("/subwayStationInfo")
                .queryParam("apiKey", odsayProperty.apiKey())
                .queryParam("stationID", stationId)
                .toUriString();
    }
}
