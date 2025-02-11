package com.easyride.subway.service;

import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubwayService {

    private final OdsaySubwayClient subwayClient;

    public NearSubwayStationsResponse findNearSubwayStations(String stationName, int stationLine) {
        SubwayStations searchStations = subwayClient.searchStation(stationName);
        String searchStationId = searchStations.fetchStationIdByStationLine(stationLine);
        NearSubwayStations nearStations = subwayClient.fetchStationInfo(searchStationId);
        return new NearSubwayStationsResponse(nearStations);
    }
}
