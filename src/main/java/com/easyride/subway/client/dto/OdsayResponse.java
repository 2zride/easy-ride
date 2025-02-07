package com.easyride.subway.client.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class OdsayResponse {

    private final List<ErrorDetail> error;

    public boolean isError() {
        return error != null;
    }

    protected record ErrorDetail(String code,
                                 String message) {
    }
}
