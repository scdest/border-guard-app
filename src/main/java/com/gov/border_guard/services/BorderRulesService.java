package com.gov.border_guard.services;

import com.gov.border_guard.entities.BorderRulesEntity;
import com.gov.border_guard.entities.Country;
import com.gov.border_guard.repositories.BorderRulesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BorderRulesService {
    private static final String STATIC_ID = "BORDER_RULES";

    private final BorderRulesRepository repository;
    private final CountriesHolder countriesHolder;

    public void saveBorderRules(BorderRulesEntity entity) {
        if (!countriesHolder.countriesExists(entity.getDeniedCountries())) {
            throw new RuntimeException("Invalid countries provided");
        }
        entity.setId(STATIC_ID);
        repository.save(entity);
    }

    public BorderRulesEntity getBorderRules() {
        return repository.findAll().get(0);
    }

    public boolean isDeniedCountry(Country country) {
        return repository.findAll().get(0).getDeniedCountries().contains(country);
    }

    public boolean isWantedCriminal(String passportId) {
        return repository.findAll().get(0).getWantedCriminalsIds().contains(passportId);
    }
}
