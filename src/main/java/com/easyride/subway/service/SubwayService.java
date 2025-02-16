package com.easyride.subway.service;

import com.easyride.subway.client.dataseoul.DataSeoulSubwayClient;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import com.easyride.subway.service.dto.SubwayCarCongestionsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubwayService {

    private final OdsaySubwayClient odsaySubwayClient;
    private final DataSeoulSubwayClient dataSeoulSubwayClient;
    private final SkSubwayClient skSubwayClient;

    public NearSubwayStationsResponse findNearSubwayStations(String stationName, int stationLine) {
        SubwayStations searchStations = odsaySubwayClient.searchStation(stationName);
        SubwayStation searchStation = searchStations.findStationByStationLine(stationLine);
        NearSubwayStations nearStations = odsaySubwayClient.fetchStationInfo(searchStation.getId());
        return new NearSubwayStationsResponse(nearStations);
    }

    public SubwayCarCongestionsResponse findSubwayCongestion(SubwayStation targetStation, SubwayStation nextStation) {
        Subways subways = dataSeoulSubwayClient.fetchRealTimeSubwayPositions(targetStation.getLine());
        Subway subway = subways.findApproachingSubway(targetStation, nextStation);
        SubwayCongestion subwayCongestion = skSubwayClient.fetchCongestion(subway);
        return new SubwayCarCongestionsResponse(targetStation, nextStation, subwayCongestion);
    }
}
