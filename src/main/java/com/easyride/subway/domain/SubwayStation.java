package com.easyride.subway.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SubwayStation {

    private final String id;
    private final String name;
    private final int line;
}
