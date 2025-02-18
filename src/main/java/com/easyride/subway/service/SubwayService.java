package com.easyride.subway.service;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.client.dataseoul.DataSeoulSubwayClient;
import com.easyride.subway.client.odsay.OdsaySubwayClient;
import com.easyride.subway.client.sk.SkSubwayClient;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayPath;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.exception.SubwayErrorCode;
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
        SubwayStation searchStation = searchStations.findStationByStationLine(stationLine);
        NearSubwayStations nearStations = odsaySubwayClient.fetchStationInfo(searchStation.getId());
        return new NearSubwayStationsResponse(nearStations);
    }

    public SubwayCarCongestionsResponse findSubwayCongestion(SubwayStation targetStation, SubwayStation nextStation) {
        // TODO 너무 많은 API 호출량
        SubwayPath subwayPathOfTarget = odsaySubwayClient.fetchSubwayPath(
                new Subway(null, null, targetStation, nextStation)); // TODO client 역할 줄이도록 리팩터링
        SubwayDirection direction = subwayPathOfTarget.getDirection();

        // 같은 방향의 전철만 거르기
        Subways subways = dataSeoulSubwayClient.fetchRealTimeSubwayPositions(targetStation.getLine());
        List<Subway> subwaysWithSameDirection = subways.getSubways().stream()
                .filter(subway -> subway.isSameDirection(direction))
                .toList();

        List<SubwayPath> subwayPaths = subwaysWithSameDirection.stream()
                .map(odsaySubwayClient::fetchSubwayPath)
                .toList();
        Subway subway = subwayPaths.stream()
                .filter(subwayPath -> subwayPath.willPass(targetStation))
                .min(Comparator.comparingInt(subwayPath -> subwayPath.distanceFrom(targetStation)))
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.NO_APPROACHING_SUBWAY))
                .getSubway();
        SubwayCongestion subwayCongestion = skSubwayClient.fetchCongestion(subway);
        return new SubwayCarCongestionsResponse(targetStation, nextStation, subwayCongestion);
    }
}
