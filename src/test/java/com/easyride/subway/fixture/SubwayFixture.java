package com.easyride.subway.fixture;

import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayStation;

public class SubwayFixture {

    public static final Subway POSITION_2390 = new Subway("2390",
            SubwayDirection.INNER,
            SubwayStation.of("215", "잠실나루", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final Subway POSITION_2413 = new Subway("2413",
            SubwayDirection.OUTER,
            SubwayStation.of("209", "한양대", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final Subway POSITION_2373 = new Subway("2373",
            SubwayDirection.OUTER,
            SubwayStation.of("217", "잠실새내", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final Subway POSITION_2344 = new Subway("2344",
            SubwayDirection.INNER,
            SubwayStation.of("209", "한양대", 2),
            SubwayStation.of("211", "성수종착", 2));

    public static final Subway POSITION_2389 = new Subway("2389",
            SubwayDirection.OUTER,
            SubwayStation.of("230", "신림", 2),
            SubwayStation.of("211", "성수종착", 2));
}
