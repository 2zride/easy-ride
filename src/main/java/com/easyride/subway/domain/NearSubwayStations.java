package com.easyride.subway.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NearSubwayStations {

    private final List<SubwayStation> stations = new ArrayList<>();

    public void addStations(List<SubwayStation> stations) {
        this.stations.addAll(stations);
    }

    public List<SubwayStation> getStations() {
        return Collections.unmodifiableList(stations);
    }
}
