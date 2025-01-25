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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

class StationInfoMapperTest {

    @DisplayName("정상응답의 경우, 매핑에 성공한다")
    @Test
    void successCase() throws IOException {
        StationInfoMapper stationInfoMapper = new StationInfoMapper();
        JsonNode successResponse = readJsonFileAsNode("odsay/success.json");
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        StationSearchResponse response = new StationSearchResponse(
                Optional.empty(),
                Optional.empty(),
                Optional.of(successResponse)
        );

        assertThatCode(() -> stationInfoMapper.responseToInfo(response, stationGeoLocation))
                .doesNotThrowAnyException();
    }

    @DisplayName("오디세이 서버 에러가 반환되면, 서버 에러를 반환한다")
    @Test
    void error500() throws IOException {
        StationInfoMapper stationInfoMapper = new StationInfoMapper();
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        JsonNode serverErrorResponse = readJsonFileAsNode("odsay/error500.json");

        StationSearchResponse response = new StationSearchResponse(
                Optional.of("500"),
                Optional.empty(),
                Optional.of(serverErrorResponse)
        );

        assertThatThrownBy(() -> stationInfoMapper.responseToInfo(response, stationGeoLocation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("서버 에러");
    }

    @DisplayName("오디세이 클라이언트 에러가 반환되면, 클라이언트 에러를 반환한다")
    @Test
    void error400() throws IOException {
        StationInfoMapper stationInfoMapper = new StationInfoMapper();
        GeoLocation stationGeoLocation = new GeoLocation("126.978009", "37.4011");
        JsonNode clientErrorResponse = readJsonFileAsNode("odsay/error9.json");

        StationSearchResponse response = new StationSearchResponse(
                Optional.of("-8"),
                Optional.empty(),
                Optional.of(clientErrorResponse)
        );

        assertThatThrownBy(() -> stationInfoMapper.responseToInfo(response, stationGeoLocation))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("400 에러");
    }

    @DisplayName("응답값이 null이면, 에러를 반환한다")
    @Test
    void nullCheck() {
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
