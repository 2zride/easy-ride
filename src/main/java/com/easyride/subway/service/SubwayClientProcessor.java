package com.easyride.subway.service;

import com.easyride.subway.client.dataseoul.DataSeoulSubwayClient;
import com.easyride.subway.client.dto.DataSeoulRealTimeSubwayResponse;
import com.easyride.subway.client.dto.OdsayStationInfoResponse;
import com.easyride.subway.client.dto.OdsayStationPathResponse;
import com.easyride.subway.client.dto.SkRealTimeCongestionResponse;
import com.easyride.subway.client.odsay.OdsayStationsManager;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayWithPath;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.entity.SubwayStationEntity;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SubwayClientProcessor {

    // private final OdsayStationsManager odsayStationsManager;
    private final OdsaySubwayClient odsaySubwayClient;
    private final DataSeoulSubwayClient dataSeoulSubwayClient;
    private final SkSubwayClient skSubwayClient;

    public void addOdsayStations(List<SubwayStationEntity> stations) {
        OdsayStationsManager.addStations(stations);
    }

    public NearSubwayStations fetchNearStations(SubwayStation targetStation) {
        String odsayId = OdsayStationsManager.findOdsayIdByStation(targetStation);
        OdsayStationInfoResponse response = odsaySubwayClient.fetchStationInfo(odsayId);
        return response.toNearSubwayStations();
    }

    public SubwayDirection fetchSubwayDirection(SubwayStation startStation, SubwayStation endStation) {
        String startOdsayId = OdsayStationsManager.findOdsayIdByStation(startStation);
        String endOdsayId = OdsayStationsManager.findOdsayIdByStation(endStation);
        OdsayStationPathResponse response = odsaySubwayClient.fetchSubwayPath(startOdsayId, endOdsayId);
        return response.toSubwayDirection();
    }

    public Subways fetchRealTimeSubways(SubwayStation station) {
        DataSeoulRealTimeSubwayResponse response = dataSeoulSubwayClient.fetchRealTimeSubwaysByLine(station.getLine());
        return response.toSubways();
    }

    public SubwayWithPath fetchSubwayPath(Subway subway) {
        String nowOdsayId = OdsayStationsManager.findOdsayIdByStation(subway.getNowStation());
        String endOdsayId = OdsayStationsManager.findOdsayIdByStation(subway.getEndStation());
        OdsayStationPathResponse response = odsaySubwayClient.fetchSubwayPath(nowOdsayId, endOdsayId);
        return response.toSubwayPath(subway);
    }

    public SubwayCongestion fetchSubwayCongestion(Subway subway) {
        SkRealTimeCongestionResponse response = skSubwayClient.fetchCongestion(subway.stationLine(), subway.getId());
        return response.toSubwayCongestion(subway);
    }
}
