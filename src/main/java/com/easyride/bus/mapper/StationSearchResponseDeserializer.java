package com.easyride.bus.mapper;

import com.easyride.bus.dto.response.StationSearchResponse;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import java.io.IOException;
import java.util.Optional;
import org.springframework.boot.jackson.JsonComponent;

@JsonComponent
public class StationSearchResponseDeserializer extends JsonDeserializer<StationSearchResponse> {

    @Override
    public StationSearchResponse deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) {
        try {
            JsonNode node = jsonParser.getCodec()
                    .readTree(jsonParser);
            return parse(node);
        } catch (IOException exception) {
            throw new RuntimeException("ODSAY 정류장 검색 API 역직렬화 과정에서 오류가 발생하였습니다.");
        }
    }

    private StationSearchResponse parse(JsonNode node) {
        Optional<String> code = find(node, "code");
        Optional<String> message = find(node, "message", "msg");
        return new StationSearchResponse(code, message, Optional.of(node));
    }

    private Optional<String> find(JsonNode node, String... fieldName) {
        for (String field : fieldName) {
            JsonNode nodeName = node.findPath(field);
            if (!nodeName.isMissingNode()) {
                return Optional.of(nodeName.textValue());
            }
        }
        return Optional.empty();
    }
}
