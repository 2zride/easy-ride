package com.easyride.subway.service.dto;

import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayStation;
import java.util.List;

public record SubwayCarCongestionsResponse(
        SubwayStationDetail station,
        SubwayStationDetail nextStation,
        List<SubwayCarCongestionDetail> carCongestions
) {

    public SubwayCarCongestionsResponse(SubwayStation station, SubwayStation nextStation, SubwayCongestion congestion) {
        this(new SubwayStationDetail(station),
                new SubwayStationDetail(nextStation),
                congestion.getCarCongestions().stream()
                        .map(SubwayCarCongestionDetail::new)
                        .toList());
    }
}
