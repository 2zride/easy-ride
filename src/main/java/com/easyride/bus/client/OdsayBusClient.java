package com.easyride.bus.client;

import com.easyride.bus.config.OdsayProperty;
import com.easyride.bus.domain.StationInfo;
import com.easyride.bus.dto.request.StationSearchRequest;
import com.easyride.bus.dto.response.StationSearchResponse;
import com.easyride.bus.mapper.StationInfoMapper;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@EnableConfigurationProperties(OdsayProperty.class)
public class OdsayBusClient {

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

    public StationInfo serachStationInfo(StationSearchRequest searchRequest) {
        StationSearchResponse searchResponse = restClient.get()
                .uri(makeStationSearchUrl(searchRequest))
                .retrieve()
                .body(StationSearchResponse.class);

        return stationInfoMapper.responseToInfo(searchResponse, searchRequest.longitude(), searchRequest.latitude());
    }

    public String makeStationSearchUrl(StationSearchRequest searchRequest) {
        return UriComponentsBuilder.fromHttpUrl(property.baseUrl())
                .queryParam("apiKey", property.apiKey())
                .queryParam("stationName", searchRequest.stationName())
                .queryParam("stationClass", "1")
                .queryParam("key", property.apiKey())
                .build(false)
                .toUriString();
    }
}
