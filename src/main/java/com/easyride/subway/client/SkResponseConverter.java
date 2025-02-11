package com.easyride.subway.client;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.client.dto.SkRealTimeCongestionResponse;
import com.easyride.subway.exception.SubwayErrorCode;
import java.io.IOException;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

public class SkResponseConverter {

    private static SkResponseConverter INSTANCE;

    public static SkResponseConverter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SkResponseConverter();
        }
        return INSTANCE;
    }

    public SkRealTimeCongestionResponse convert(ConvertibleClientHttpResponse response) throws IOException {
        if (response.getStatusCode().isError()) {
            throw new EasyRideException(SubwayErrorCode.SK_API_ERROR);
        }
        SkRealTimeCongestionResponse responseBody = response.bodyTo(SkRealTimeCongestionResponse.class);
        if (responseBody.isError()) {
            throw new EasyRideException(SubwayErrorCode.SK_API_ERROR);
        }
        return responseBody;
    }
}
