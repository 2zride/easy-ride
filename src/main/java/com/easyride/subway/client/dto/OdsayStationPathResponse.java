package com.easyride.subway.client.dto;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayPath;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.exception.SubwayErrorCode;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;

public class OdsayStationPathResponse extends OdsayResponse {

    private final SuccessDetail result;

    public OdsayStationPathResponse(List<ErrorDetail> error, SuccessDetail result) {
        super(error);
        this.result = result;
    }

    public SubwayPath toSubwayPath(Subway subway) {
        DriveInfoDetail driveInfo = result.driveInfoSet.driveInfo.get(0);
        List<SubwayStation> stations = result.stationSet.stations.stream()
                .map(detail -> toSubwayStation(detail, Integer.parseInt(driveInfo.stationLine)))
                .toList();
        return new SubwayPath(
                subway,
                SubwayDirectionMapper.asDirection(driveInfo.wayCode, driveInfo.stationLine),
                stations);
    }

    private SubwayStation toSubwayStation(StationPathDetail stationPathDetail, int stationLine) {
        return SubwayStation.of(stationPathDetail.passingStationId, stationPathDetail.passingStationName, stationLine);
    }

    private record SuccessDetail(
            DriveInfoSet driveInfoSet,
            StationSet stationSet
    ) {
    }

    private record DriveInfoSet(
            List<DriveInfoDetail> driveInfo
    ) {
    }

    private record DriveInfoDetail(
            String stationLine,
            Integer stationCount,
            String wayCode
    ) {
    }

    private record StationSet(
            List<StationPathDetail> stations
    ) {
    }

    private record StationPathDetail(
            @JsonAlias("endSID")
            String passingStationId,
            @JsonAlias("endName")
            String passingStationName
    ) {
    }

    @RequiredArgsConstructor
    private enum SubwayDirectionMapper {

        UP_MAPPER("1", SubwayDirection.UP),
        DOWN_MAPPER("2", SubwayDirection.DOWN),
        INNER_MAPPER("2", SubwayDirection.INNER),
        OUTER_MAPPER("1", SubwayDirection.OUTER),
        ;

        private final String value;
        private final SubwayDirection direction;

        private static SubwayDirection asDirection(String value, String stationLine) {
            if (stationLine.equals("2")) {
                return asDirectionWhenSeoulMetro2Line(value);
            }
            return Arrays.stream(values())
                    .filter(mapper -> mapper.value.equals(value))
                    .findAny()
                    .orElseThrow(() -> new EasyRideException(SubwayErrorCode.INVALID_DIRECTION))
                    .direction;
        }

        private static SubwayDirection asDirectionWhenSeoulMetro2Line(String value) {
            if (value.equals(INNER_MAPPER.value)) {
                return INNER_MAPPER.direction;
            }
            if (value.equals(OUTER_MAPPER.value)) {
                return OUTER_MAPPER.direction;
            }
            throw new EasyRideException(SubwayErrorCode.INVALID_DIRECTION);
        }
    }
}
