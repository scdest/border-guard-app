package com.gov.border_guard.dtos;

import lombok.Getter;

import java.util.Objects;
import java.util.Set;

@Getter
public class BorderRulesDto {
    private final Set<CountryDto> deniedCountries;
    private final Set<String> wantedCriminalsIds;

    public BorderRulesDto(Set<CountryDto> deniedCountries, Set<String> wantedCriminalsIds) {
        this.deniedCountries = Objects.requireNonNull(deniedCountries);
        this.wantedCriminalsIds = Objects.requireNonNull(wantedCriminalsIds);
    }
}
