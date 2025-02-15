package com.easyride.subway.fixture;

import com.easyride.subway.domain.SubwayPosition;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.UpDownLine;

public class SubwayFixture {

    public static final SubwayPosition POSITION_2390 = new SubwayPosition("2390",
            UpDownLine.UP,
            SubwayStation.of("215", "잠실나루", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final SubwayPosition POSITION_2413 = new SubwayPosition("2413",
            UpDownLine.DOWN,
            SubwayStation.of("209", "한양대", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final SubwayPosition POSITION_2373 = new SubwayPosition("2373",
            UpDownLine.DOWN,
            SubwayStation.of("217", "잠실새내", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final SubwayPosition POSITION_2344 = new SubwayPosition("2344",
            UpDownLine.UP,
            SubwayStation.of("209", "한양대", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final SubwayPosition POSITION_2389 = new SubwayPosition("2389",
            UpDownLine.DOWN,
            SubwayStation.of("230", "신림", 2),
            SubwayStation.of("211", "성수종착", 2));
}
