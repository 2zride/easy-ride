package com.easyride.subway.exception;

import com.easyride.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SubwayErrorCode implements ErrorCode {

    ODSAY_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "오디세이 API 호출 과정에서 예외가 발생했습니다."),
    SK_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "SK Open API 호출 과정에서 예외가 발생했습니다."),
    DATA_SEOUL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서울시 공공데이터 API 호출 과정에서 예외가 발생했습니다."),
    INVALID_STATION(HttpStatus.BAD_REQUEST, "해당 호선과 이름의 지하철역이 없습니다."),
    NO_SUBWAY_IN_OPERATION(HttpStatus.BAD_REQUEST, "해당 호선에서 현재 운행 중인 지하철이 없습니다."),
    INVALID_DIRECTION(HttpStatus.BAD_REQUEST, "유효하지 않은 방향입니다."),
    DATA_SEOUL_INVALID_STATION_LINE_NAME(HttpStatus.BAD_REQUEST, "유효하지 않은 지하철역 호선명입니다."),
    NO_APPROACHING_SUBWAY(HttpStatus.BAD_REQUEST, "다가올 지하철이 없습니다."),
    ;

    private final HttpStatus status;
    private final String message;
}
