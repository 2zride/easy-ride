package com.easyride.subway.entity;

import com.easyride.subway.domain.StationLine;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubwayStationRepository extends JpaRepository<SubwayStationEntity, Long> {

    Optional<SubwayStationEntity> findByNameAndLine(String name, StationLine line);
}
