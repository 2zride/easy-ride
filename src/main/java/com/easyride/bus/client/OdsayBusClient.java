package com.easyride.bus.client;

import com.easyride.bus.config.OdsayProperty;
import com.easyride.bus.domain.GeoLocation;
import com.easyride.bus.domain.StationInfo;
import com.easyride.bus.dto.request.StationSearchRequest;
import com.easyride.bus.dto.response.StationSearchResponse;
import com.easyride.bus.mapper.StationInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j
@Component
@EnableConfigurationProperties(OdsayProperty.class)
public class OdsayBusClient {

    private static final int SEARCH_BUS_STATION_RADIUS = 1;

    private final OdsayProperty property;
    private final RestClient restClient;
    private final StationInfoMapper stationInfoMapper;

    public OdsayBusClient(
            OdsayProperty property,
            RestClient.Builder restClientBuilder,
            StationInfoMapper stationInfoMapper
    ) {
        this.property = property;
        this.restClient = restClientBuilder.build();
        this.stationInfoMapper = stationInfoMapper;
    }

    public StationInfo searchStationInfo(StationSearchRequest searchRequest) {
        GeoLocation stationGeoLocation = searchRequest.stationGeoLocation();
        StationSearchResponse searchResponse = restClient.get()
                .uri(makeStationSearchUrl(searchRequest))
                .retrieve()
                .body(StationSearchResponse.class);

        return stationInfoMapper.responseToInfo(searchResponse, stationGeoLocation);
    }

    private String makeStationSearchUrl(StationSearchRequest searchRequest) {
        GeoLocation stationGeoLocation = searchRequest.stationGeoLocation();
        return UriComponentsBuilder.fromUriString(property.baseUrl())
                .queryParam("apiKey", property.apiKey())
                .queryParam("x", stationGeoLocation.getLongitude())
                .queryParam("y", stationGeoLocation.getLatitude())
                .queryParam("radius", String.valueOf(SEARCH_BUS_STATION_RADIUS))
                .build(false)
                .toUriString();
    }
}
