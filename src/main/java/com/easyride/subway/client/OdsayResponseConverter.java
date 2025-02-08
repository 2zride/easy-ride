package com.easyride.subway.client;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.client.dto.OdsayResponse;
import com.easyride.subway.exception.SubwayErrorCode;
import java.io.IOException;
import org.springframework.web.client.RestClient.RequestHeadersSpec.ConvertibleClientHttpResponse;

public class OdsayResponseConverter {

    private static OdsayResponseConverter INSTANCE;

    public static OdsayResponseConverter getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new OdsayResponseConverter();
        }
        return INSTANCE;
    }

    public <T extends OdsayResponse> T convert(ConvertibleClientHttpResponse response,
                                               Class<T> bodyClass) throws IOException {
        if (response.getStatusCode().isError()) {
            throw new EasyRideException(SubwayErrorCode.ODSAY_API_ERROR);
        }
        T responseBody = response.bodyTo(bodyClass);
        if (responseBody.isError()) {
            throw new EasyRideException(SubwayErrorCode.ODSAY_API_ERROR);
        }
        return responseBody;
    }
}
