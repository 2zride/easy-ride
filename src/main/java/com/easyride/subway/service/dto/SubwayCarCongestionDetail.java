package com.easyride.subway.service.dto;

import com.easyride.subway.domain.SubwayCarCongestion;

public record SubwayCarCongestionDetail(
        int carNumber,
        int carCongestion
) {

    public SubwayCarCongestionDetail(SubwayCarCongestion carCongestion) {
        this(carCongestion.getNumber(), carCongestion.getCongestion());
    }
}
