package com.easyride.bus.mapper;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.easyride.bus.domain.GeoLocation;
import com.easyride.bus.dto.response.StationSearchResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class StationInfoMapperTest {

    @Test
    void 정상응답의_경우_매핑에_성공한다() throws IOException {
        StationInfoMapper stationInfoMapper = new StationInfoMapper();
        JsonNode successResponse = readJsonFileAsNode("odsay/success.json");
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        StationSearchResponse response = new StationSearchResponse(
                true,
                Optional.empty(),
                Optional.empty(),
                Optional.of(successResponse)
        );

        assertThatCode(() -> stationInfoMapper.responseToInfo(response, stationGeoLocation))
                .doesNotThrowAnyException();
    }

    @Test
    void 오디세이_서버_에러가_반환되면_서버_에러를_반환한다() throws IOException {
        StationInfoMapper stationInfoMapper = new StationInfoMapper();
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        JsonNode serverErrorResponse = readJsonFileAsNode("odsay/error500.json");

        StationSearchResponse response = new StationSearchResponse(
                false,
                Optional.of("500"),
                Optional.empty(),
                Optional.of(serverErrorResponse)
        );

        assertThatThrownBy(() -> stationInfoMapper.responseToInfo(response, stationGeoLocation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("서버 에러");
    }

    @Test
    void 오디세이_클라이언트_에러가_반환되면_클라이언트_에러를_반환한다() throws IOException {
        StationInfoMapper stationInfoMapper = new StationInfoMapper();
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        JsonNode clientErrorResponse = readJsonFileAsNode("odsay/error.json");

        StationSearchResponse response = new StationSearchResponse(
                false,
                Optional.of("-8"),
                Optional.empty(),
                Optional.of(clientErrorResponse)
        );

        assertThatThrownBy(() -> stationInfoMapper.responseToInfo(response, stationGeoLocation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("400 에러");
    }

    @Test
    void 응답값이_null이면_에러를_반환한다() {
        StationInfoMapper stationInfoMapper = new StationInfoMapper();
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");

        assertThatThrownBy(() -> stationInfoMapper.responseToInfo(null, stationGeoLocation))
                .isInstanceOf(RuntimeException.class);
    }

    public JsonNode readJsonFileAsNode(String path) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()) {
            return objectMapper.readTree(inputStream);
        }
    }
}
