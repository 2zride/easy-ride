package com.easyride.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubwayCarCongestion {

    private final int number;
    private final int congestion;
}
