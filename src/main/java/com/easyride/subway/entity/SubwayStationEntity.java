package com.easyride.subway.entity;

import com.easyride.subway.domain.StationLine;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

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
}
