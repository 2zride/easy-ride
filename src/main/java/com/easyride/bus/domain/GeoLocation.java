package com.easyride.bus.domain;

import java.util.Objects;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class GeoLocation {

    private final String longitude;
    private final String latitude;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GeoLocation that = (GeoLocation) o;
        return Objects.equals(getLongitude(), that.getLongitude()) && Objects.equals(getLatitude(),
                that.getLatitude());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLongitude(), getLatitude());
    }
}
