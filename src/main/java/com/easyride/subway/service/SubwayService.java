package com.easyride.subway.service;

import com.easyride.global.exception.EasyRideException;
import com.easyride.subway.domain.NearSubwayStations;
import com.easyride.subway.domain.StationLine;
import com.easyride.subway.domain.Subway;
import com.easyride.subway.domain.SubwayCongestion;
import com.easyride.subway.domain.SubwayDirection;
import com.easyride.subway.domain.SubwayStation;
import com.easyride.subway.domain.Subways;
import com.easyride.subway.entity.SubwayStationEntity;
import com.easyride.subway.entity.SubwayStationRepository;
import com.easyride.subway.exception.SubwayErrorCode;
import com.easyride.subway.service.dto.NearSubwayStationsResponse;
import com.easyride.subway.service.dto.SubwayCarCongestionsResponse;
import jakarta.annotation.PostConstruct;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubwayService {

    private final SubwayClientProcessor clientProcessor;
    private final SubwayStationRepository stationRepository;

    @PostConstruct
    public void loadOdsayStations() { // TODO 위치 고민
        List<SubwayStationEntity> stations = stationRepository.findAll();
        clientProcessor.addOdsayStations(stations);
    }

    public NearSubwayStationsResponse findNearSubwayStations(String stationName, int stationLineNumber) {
        StationLine stationLine = StationLine.asStationLine(stationLineNumber);
        SubwayStationEntity stationEntity = stationRepository.findByNameAndLine(stationName, stationLine)
                .orElseThrow(() -> new EasyRideException(SubwayErrorCode.INVALID_STATION));
        SubwayStation station = SubwayStation.fromEntity(stationEntity);
        NearSubwayStations nearStations = clientProcessor.fetchNearStations(station);
        return new NearSubwayStationsResponse(nearStations);
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
