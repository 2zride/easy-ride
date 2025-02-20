package com.easyride.subway.entity;

import com.easyride.subway.domain.StationLine;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
@Table(name = "subway_station")
@Entity
public class SubwayStationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String stationId;

    private String odsayId;

    private String code;

    @Enumerated(EnumType.STRING)
    private StationLine line;

    public SubwayStationEntity(String name, String stationId, String odsayId, String code, Integer line) {
        this(null, name, stationId, odsayId, code, StationLine.asStationLine(line));
    }
}
