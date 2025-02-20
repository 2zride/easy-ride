package com.easyride.subway.domain;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.exception.SubwayErrorCode;
import java.util.List;
import lombok.Getter;

public class SubwayWithPath {

    @Getter
    private final Subway subway;
    private final List<SubwayStation> path;

    public SubwayWithPath(Subway subway, SubwayDirection direction, List<SubwayStation> path) {
        this.subway = new Subway(subway.getId(), direction, subway.getNowStation(), subway.getEndStation());
        this.path = path;
    }

    public boolean willPass(SubwayStation targetStation) {
        // TODO: 현재 - 최단 경로를 기준으로 가져옴. 2호선의 경우 더 긴 경로를 가져올 수 있으므로 추후 데이터 DB 관리 또는 아래 로직 예정
        // TODO: 2호선이면서 종착역이 성수이면 무조건 true 반환하도록
        return path.contains(targetStation);
    }

    public int distanceFrom(SubwayStation targetStation) {
        if (!willPass(targetStation)) {
            throw new EasyRideException(SubwayErrorCode.CANNOT_PASS_THAT_SUBWAY);
        }
        return path.indexOf(targetStation) + 1;
    }
}
