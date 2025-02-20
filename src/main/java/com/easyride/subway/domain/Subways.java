package com.easyride.subway.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class Subways {

    private final List<Subway> subways = new ArrayList<>();

    public void add(Subway subway) {
        this.subways.add(subway);
    }

    public void deleteIf(Predicate<Subway> condition) {
        this.subways.removeIf(condition);
    }

    public <T> List<T> mapAll(Function<Subway, T> mapper) {
        return this.subways.stream()
                .map(mapper)
                .toList();
    }
}
