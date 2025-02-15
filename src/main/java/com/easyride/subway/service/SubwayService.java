package com.easyride.subway.service;

import com.easyride.subway.client.dataseoul.DataSeoulStationLineName;
import com.easyride.subway.client.dataseoul.DataSeoulSubwayClient;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayPosition;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.domain.UpDownLine;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import com.easyride.subway.service.dto.SubwayCarCongestionsResponse;
import java.util.Comparator;
import java.util.List;
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
        String searchStationId = searchStations.fetchStationIdByStationLine(stationLine);
        NearSubwayStations nearStations = odsaySubwayClient.fetchStationInfo(searchStationId);
        return new NearSubwayStationsResponse(nearStations);
    }

    public SubwayCarCongestionsResponse findSubwayCongestion(SubwayStation station, SubwayStation nextStation) {
        String stationLineName = DataSeoulStationLineName.asStationLineName(station.getLine());
        List<SubwayPosition> subwayPositions = dataSeoulSubwayClient.fetchRealTimeSubwayPositions(stationLineName);

        UpDownLine direction = UpDownLine.decideUpDownLine(station, nextStation);
        SubwayPosition position = subwayPositions.stream()
                .filter(subwayPosition -> subwayPosition.isSameDirection(direction))
                .filter(subwayPosition -> subwayPosition.isBeforeEndStation(direction, station))
                .min(Comparator.comparingInt(subwayPosition -> subwayPosition.distanceFrom(station)))
                .orElseThrow();// todo

        SubwayCongestion subwayCongestion = skSubwayClient.fetchRealTimeCongestion(
                position.fetchStationLine().getNumber(), position.getSubwayNumber());

        return new SubwayCarCongestionsResponse(station, nextStation, subwayCongestion);
    }
}
