package com.easyride.subway.client.odsay;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.entity.SubwayStationEntity;
import com.easyride.subway.exception.SubwayErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
public class OdsayStationsManager {

    private static final List<OdsaySubwayStation> STATIONS = new ArrayList<>();

    public static void addStations(List<SubwayStationEntity> stationEntities) {
        stationEntities.stream()
                .map(OdsaySubwayStation::fromEntity)
                .forEach(STATIONS::add);
    }

    public static String findOdsayIdByStation(SubwayStation station) {
        return STATIONS.stream()
                .filter(odsaySubwayStation -> odsaySubwayStation.isSameStation(station))
                .findAny()
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.INVALID_STATION_BY_ODSAY))
                .getOdsayId();
    }

    public static SubwayStation findStationByOdsayId(String odsayId) {
        return STATIONS.stream()
                .filter(odsaySubwayStation -> odsaySubwayStation.isSameOdsayId(odsayId))
                .findAny()
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.INVALID_ODSAY_ID))
                .getStation();
    } // TODO static 제거 -> 의존성 추가 -> Odsay API DTO 분리 -> 테스트 코드 작성

    @Getter
    @RequiredArgsConstructor
    private static class OdsaySubwayStation {

        private final SubwayStation station;
        private final String odsayId;

        public static OdsaySubwayStation fromEntity(SubwayStationEntity entity) {
            SubwayStation station = SubwayStation.of(entity.getStationId(), entity.getName(), entity.getLine());
            String odsayId = entity.getOdsayId();
            return new OdsaySubwayStation(station, odsayId);
        }

        public boolean isSameStation(SubwayStation station) {
            return this.station.equals(station);
        }

        public boolean isSameOdsayId(String odsayId) {
            return this.odsayId.equals(odsayId);
        }
    }
}
