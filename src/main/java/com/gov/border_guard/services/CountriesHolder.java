package com.gov.border_guard.services;

import com.gov.border_guard.entities.Country;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
@Slf4j
public class CountriesHolder {
    private static final String COMMA_DELIMITER = ",";
    private static final Integer POSITION_OF_COUNTRY_CODE_3 = 2;

    private Set<Country> countries = new HashSet<>();

    @Value("classpath:static/countries.csv")
    private Resource countriesList;

    @PostConstruct
    public void init() {
        this.countries = getDataFromCsv().stream().map(el -> new Country(el.get(POSITION_OF_COUNTRY_CODE_3))).collect(Collectors.toSet());
    }

    public boolean countriesExists(Set<Country> countriesToCheck) {
        return countries.containsAll(countriesToCheck);
    }

    private List<List<String>> getDataFromCsv() {
        List<List<String>> records = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(countriesList.getFile()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(COMMA_DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            log.error("Error while loading countries list");
            throw new RuntimeException(e);
        }
        return records;
    }
}
