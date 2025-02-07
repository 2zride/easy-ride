package com.easyride.subway.service.dto;

import com.easyride.subway.domain.SubwayStation;
import java.util.List;

public record NearSubwayStationsResponse(String prevStationName,
                                         String nextStationName) {

    public NearSubwayStationsResponse(List<SubwayStation> stations) {
        this(stations.get(0).getName(), stations.get(1).getName());
    } // TODO 인덱스 사용 refactor
}
