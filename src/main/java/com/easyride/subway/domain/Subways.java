package com.easyride.subway.domain;

import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Subways {

    private final List<Subway> subways;

    public List<Subway> getSubways() {
        return Collections.unmodifiableList(subways);
    }
}
