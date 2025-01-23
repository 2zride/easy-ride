package com.easyride.bus.mapper;

import com.easyride.bus.domain.Coordinates;
import com.easyride.bus.domain.StationInfo;
import com.easyride.bus.dto.response.StationSearchResponse;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StationInfoMapper {

    private static final String ODSAY_SERVER_ERROR = "500";
    private static final String BUS_INFO_ARRAY_FIELD_NAME = "businfo";
    private static final String STATION_INFO_ARRAY_FIELD_NAME = "station";
    private static final String STATION_ID_FIELD_NAME = "stationID";
    private static final String STATION_LONGITUDE_FIELD_NAME = "x";
    private static final String STATION_LATITUDE_FIELD_NAME = "y";
    private static final String BUS_NUMBER_FIELD_NAME = "busNo";
    private static final String API_RESPONSE_FIELD_NAME = "result";

    public StationInfo responseToInfo(StationSearchResponse response, Coordinates stationCoordinates) {
        if (response == null) {
            throw new RuntimeException("searchResult is null");
        }

        if (response.code().isPresent()) {
            checkOdsayException(response);
        }

        return response.searchResult()
                .map(node -> searchStationByCoordinates(node, stationCoordinates))
                .orElseThrow(RuntimeException::new); //TODO 에러 교체
    }

    private StationInfo searchStationByCoordinates(JsonNode node, Coordinates stationCoordinates) {
        Iterator<JsonNode> stationInfos = findStationInfos(node);
        while (stationInfos.hasNext()) {
            JsonNode stationInfo = stationInfos.next();
            if (isSameStation(stationInfo, stationCoordinates)) {
                return mapStationInfo(stationInfo);
            }
        }

        throw new RuntimeException("같은 위치 좌표의 버스 정류장이 없습니다."); //TODO 500에러 객체 변경
    }

    private Iterator<JsonNode> findStationInfos(JsonNode node) {
        return node.get(API_RESPONSE_FIELD_NAME)
                .get(STATION_INFO_ARRAY_FIELD_NAME)
                .elements();
    }

    private boolean isSameStation(JsonNode stationInfo, Coordinates stationCoordinates) {
        String responseLongitude = stationInfo.get(STATION_LONGITUDE_FIELD_NAME).asText();
        String responseLatitude = stationInfo.get(STATION_LATITUDE_FIELD_NAME).asText();
        Coordinates responseCoordinates = new Coordinates(responseLongitude, responseLatitude);
        return stationCoordinates.equals(responseCoordinates);
    }

    private StationInfo mapStationInfo(JsonNode node) {
        long stationId = node.get(STATION_ID_FIELD_NAME).asLong();
        Iterator<JsonNode> busInfos = node.get(BUS_INFO_ARRAY_FIELD_NAME).elements();

        List<String> busNumbers = new ArrayList<>();
        while (busInfos.hasNext()) {
            JsonNode busInfo = busInfos.next();
            busNumbers.add(busInfo.get(BUS_NUMBER_FIELD_NAME).asText());
        }

        return new StationInfo(stationId, busNumbers);
    }

    private void checkOdsayException(StationSearchResponse response) {
        if (isServerErrorCode(response)) {
            log.error("ODsay Server Error: {}", response);
            throw new RuntimeException("서버 에러"); //TODO 커스텀 에러로 전환
        }

        throw new RuntimeException("400 에러"); //TODO 커스텀 에러로 전환
    }

    private boolean isServerErrorCode(StationSearchResponse response) {
        Optional<String> code = response.code();
        return code.isPresent() && ODSAY_SERVER_ERROR.equals(code.get());
    }
}
