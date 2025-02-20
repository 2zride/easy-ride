package com.easyride.subway.service;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.SubwayStations;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.exception.SubwayErrorCode;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import com.easyride.subway.service.dto.SubwayCarCongestionsResponse;
import java.util.Comparator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubwayService {

    private final SubwayClientProcessor clientProcessor;

    public NearSubwayStationsResponse findNearSubwayStations(String stationName, int stationLine) {
        SubwayStations candidateStations = clientProcessor.searchStation(stationName);
        validateIsEmpty(candidateStations);
        SubwayStation targetStation = candidateStations.filter(
                station -> station.hasLine(stationLine),
                SubwayErrorCode.INVALID_STATION);
        NearSubwayStations nearStations = clientProcessor.fetchNearStations(targetStation);
        return new NearSubwayStationsResponse(nearStations);
    }

    private void validateIsEmpty(SubwayStations stations) {
        if (stations.isEmpty()) {
            throw new EasyRideException(SubwayErrorCode.INVALID_STATION);
        }
    }

    public SubwayCarCongestionsResponse findSubwayCongestion(SubwayStation targetStation, SubwayStation nextStation) {
        // TODO 너무 많은 API 호출량
        SubwayDirection direction = clientProcessor.fetchSubwayDirection(targetStation, nextStation);
        Subways candidateSubways = clientProcessor.fetchRealTimeSubways(targetStation);
        candidateSubways.deleteIf(subway -> subway.isDifferentDirection(direction));
        Subway targetSubway = fetchTargetSubway(targetStation, candidateSubways);
        SubwayCongestion subwayCongestion = clientProcessor.fetchSubwayCongestion(targetSubway);
        return new SubwayCarCongestionsResponse(targetStation, nextStation, subwayCongestion);
    }

    private Subway fetchTargetSubway(SubwayStation targetStation, Subways candidateSubways) {
        return candidateSubways.mapAll(clientProcessor::fetchSubwayPath)
                .stream()
                .filter(subwayWithPath -> subwayWithPath.willPass(targetStation))
                .min(Comparator.comparingInt(subwayWithPath -> subwayWithPath.distanceFrom(targetStation)))
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.NO_APPROACHING_SUBWAY))
                .getSubway();
    }
}
