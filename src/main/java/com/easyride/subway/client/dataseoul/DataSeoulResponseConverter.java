package com.easyride.subway.client.dataseoul;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.client.dto.DataSeoulErrorResponse;
import com.easyride.subway.client.dto.DataSeoulRealTimeSubwayPositionResponse;
import com.easyride.subway.exception.SubwayErrorCode;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

@Slf4j
public class DataSeoulResponseConverter {

    private static final int STATUS_WHEN_SUCCESS = 200;
    private static final String CODE_WHEN_EMPTY_RESULT = "INFO-200";
    private static final String ERROR_FIELD_WHEN_SUCCESS = "errorMessage";

    private static DataSeoulResponseConverter INSTANCE;
    private static ObjectMapper OBJECT_MAPPER;

    public static DataSeoulResponseConverter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataSeoulResponseConverter();
            OBJECT_MAPPER = new ObjectMapper()
                    .registerModule(new JavaTimeModule())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return INSTANCE;
    }

    public DataSeoulRealTimeSubwayPositionResponse convert(ConvertibleClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            throw new EasyRideException(SubwayErrorCode.DATA_SEOUL_API_ERROR);
        }
        String responseBody = new String(response.getBody().readAllBytes());
        DataSeoulErrorResponse errorResponse = extractErrorResponse(responseBody);
        if (isEmptyResult(errorResponse)) {
            throw new EasyRideException(SubwayErrorCode.NO_SUBWAY_IN_OPERATION);
        }
        if (isError(errorResponse)) { // TODO 로깅
            throw new EasyRideException(SubwayErrorCode.DATA_SEOUL_API_ERROR);
        }
        return OBJECT_MAPPER.readValue(responseBody, DataSeoulRealTimeSubwayPositionResponse.class);
    }

    private DataSeoulErrorResponse extractErrorResponse(String responseBody) throws IOException {
        JsonNode rootNode = OBJECT_MAPPER.readTree(responseBody);
        if (rootNode.has(ERROR_FIELD_WHEN_SUCCESS)) {
            JsonNode errorNode = rootNode.get(ERROR_FIELD_WHEN_SUCCESS);
            return OBJECT_MAPPER.treeToValue(errorNode, DataSeoulErrorResponse.class);
        }
        return OBJECT_MAPPER.treeToValue(rootNode, DataSeoulErrorResponse.class);
    }

    private boolean isError(DataSeoulErrorResponse errorResponse) {
        return errorResponse.status() != STATUS_WHEN_SUCCESS;
    }

    private boolean isEmptyResult(DataSeoulErrorResponse errorResponse) {
        return errorResponse.code().equals(CODE_WHEN_EMPTY_RESULT);
    }
}
