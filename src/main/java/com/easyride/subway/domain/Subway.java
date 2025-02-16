package com.easyride.subway.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@EqualsAndHashCode(of = {"id", "direction", "nowStation", "endStation"}) // todo ?
public class Subway {

    private final String id;
    private final SubwayDirection direction;
    private final SubwayStation nowStation;
    private final SubwayStation endStation;

    public boolean isSameDirection(SubwayDirection direction) {
        return this.direction.isSame(direction);
    }

    public boolean willPass(SubwayStation targetStation) {
        if (targetStation.hasLine(StationLine.SEOUL_METRO_2)) {
            if (endStation.getName().equals("성수종착")) {
                return true;
            }
            // TODO 엣지 케이스 보충 필요
        }
        int target = Integer.parseInt(targetStation.getId());
        int now = Integer.parseInt(nowStation.getId());
        int end = Integer.parseInt(endStation.getId());
        return Math.min(now, end) <= target && target < Math.max(now, end);
    }

    public int distanceFrom(SubwayStation targetStation) {
        int target = Integer.parseInt(targetStation.getId());
        int now = Integer.parseInt(nowStation.getId());
        return Math.abs(target - now);
    }

    public StationLine stationLine() {
        return this.nowStation.getLine();
    }
}
