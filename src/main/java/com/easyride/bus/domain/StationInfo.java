package com.easyride.bus.domain;

import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StationInfo {

    private final long stationId;
    private final List<String> busNumbers;

}
