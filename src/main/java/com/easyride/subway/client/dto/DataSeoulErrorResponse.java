package com.easyride.subway.client.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

public record DataSeoulErrorResponse(
        Integer status,
        String code,
        String message,
        @JsonAlias("total")
        Integer totalCount
) {
}
