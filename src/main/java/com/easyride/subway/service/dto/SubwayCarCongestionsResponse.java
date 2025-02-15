package com.easyride.subway.service.dto;

import com.easyride.subway.domain.SubwayCarCongestion;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayStation;
import java.util.List;

public record SubwayCarCongestionsResponse(
        SubwayStationDetail station,
        List<SubwayCarCongestionDetail> carCongestions
) {

    public SubwayCarCongestionsResponse(SubwayStation station, SubwayStation nextStation, SubwayCongestion congestion) {
        this(new SubwayStationDetail(station, nextStation),
                congestion.getCarCongestions().stream()
                        .map(SubwayCarCongestionDetail::new)
                        .toList());
    }

    private record SubwayStationDetail(
            String stationName,
            int stationLine,
            String nextStationName
    ) {
        private SubwayStationDetail(SubwayStation nowStation, SubwayStation nextStation) {
            this(nowStation.getName(), nowStation.getLine().getNumber(), nextStation.getName());
        }
    }

    private record SubwayCarCongestionDetail(
            int carNumber,
            int carCongestion
    ) {
        private SubwayCarCongestionDetail(SubwayCarCongestion carCongestion) {
            this(carCongestion.getNumber(), carCongestion.getCongestion());
        }
    }
}
