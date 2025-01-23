package com.easyride.bus.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.mock;

import com.easyride.bus.dto.response.StationSearchResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.core.io.ClassPathResource;

class StationSearchResponseDeserializerTest {

    @DisplayName("성공 응답의 경우, 응답값만 존재한다")
    @Test
    void deserializeSuccessResponse() throws IOException {
        StationSearchResponseDeserializer customDeserializer = new StationSearchResponseDeserializer();
        DeserializationContext mockedContext = mock(DeserializationContext.class);
        JsonParser successResponse = getJsonParserByResponsePath("odsay/success.json");

        StationSearchResponse deserializedResponse = customDeserializer.deserialize(successResponse, mockedContext);

        assertAll(
                () -> assertThat(deserializedResponse.code()).isNotPresent(),
                () -> assertThat(deserializedResponse.message()).isNotPresent(),
                () -> assertThat(deserializedResponse.searchResult()).isPresent()
        );
    }

    @DisplayName("실패 응답의 경우, 에러 코드와 에러 메시지가 바인딩된다")
    @ParameterizedTest
    @ValueSource(strings = {"odsay/error8.json", "odsay/error9.json", "odsay/error500.json"})
    void deserializeErrorResponse(String errorResponsePath) throws IOException {
        StationSearchResponseDeserializer customDeserializer = new StationSearchResponseDeserializer();
        DeserializationContext mockedContext = mock(DeserializationContext.class);
        JsonParser successResponse = getJsonParserByResponsePath(errorResponsePath);

        StationSearchResponse deserializedResponse = customDeserializer.deserialize(successResponse, mockedContext);

        assertAll(
                () -> assertThat(deserializedResponse.code()).isPresent(),
                () -> assertThat(deserializedResponse.message()).isPresent(),
                () -> assertThat(deserializedResponse.searchResult()).isPresent()
        );
    }

    private JsonParser getJsonParserByResponsePath(String path) throws IOException {
        Path filePath = new ClassPathResource(path).getFile().toPath();
        String jsonContent = Files.readString(filePath);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.createParser(jsonContent);
    }
}
