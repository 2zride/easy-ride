package com.easyride.subway.service;

import com.easyride.subway.client.dataseoul.DataSeoulSubwayClient;
import com.easyride.subway.client.dto.DataSeoulRealTimeSubwayResponse;
import com.easyride.subway.client.dto.OdsaySearchStationResponse;
import com.easyride.subway.client.dto.OdsayStationInfoResponse;
import com.easyride.subway.client.dto.OdsayStationPathResponse;
import com.easyride.subway.client.dto.SkRealTimeCongestionResponse;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.domain.SubwayWithPath;
import com.easyride.subway.domain.Subways;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SubwayClientProcessor {

    private final OdsaySubwayClient odsaySubwayClient;
    private final DataSeoulSubwayClient dataSeoulSubwayClient;
    private final SkSubwayClient skSubwayClient;

    public SubwayStations searchStation(String stationName) {
        OdsaySearchStationResponse response = odsaySubwayClient.searchStation(stationName);
        return response.toSubwayStations();
    }

    public NearSubwayStations fetchNearStations(SubwayStation station) {
        OdsayStationInfoResponse response = odsaySubwayClient.fetchStationInfo(station.getId());
        return response.toNearSubwayStations();
    }

    public SubwayDirection fetchSubwayDirection(SubwayStation startStation, SubwayStation endStation) {
        OdsayStationPathResponse response = odsaySubwayClient.fetchSubwayPath(startStation.getId(), endStation.getId());
        return response.toSubwayDirection();
    }

    public Subways fetchRealTimeSubways(SubwayStation station) {
        DataSeoulRealTimeSubwayResponse response = dataSeoulSubwayClient.fetchRealTimeSubwaysByLine(station.getLine());
        return response.toSubways();
    }

    public SubwayWithPath fetchSubwayPath(Subway subway) {
        OdsayStationPathResponse response = odsaySubwayClient.fetchSubwayPath(
                subway.getNowStation().getId(),
                subway.getEndStation().getId());
        return response.toSubwayPath(subway);
    }

    public SubwayCongestion fetchSubwayCongestion(Subway subway) {
        SkRealTimeCongestionResponse response = skSubwayClient.fetchCongestion(subway.stationLine(), subway.getId());
        return response.toSubwayCongestion(subway);
    }
}
