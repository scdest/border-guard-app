package com.gov.border_guard.unit_tests;

import com.gov.border_guard.entities.Country;
import com.gov.border_guard.services.CountriesHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CountriesHolderTest {

    @Mock
    private Resource countriesListMock;
    @InjectMocks
    private CountriesHolder countriesHolder;

    @Test
    public void testCountriesExists() throws IOException {
        when(countriesListMock.getFile()).thenReturn(new File(getClass().getClassLoader().getResource("static/countries.csv").getFile()));
        countriesHolder.init();

        Assertions.assertThat(countriesHolder.countriesExists(Set.of(new Country("UKR"), new Country("USA")))).isTrue();
        Assertions.assertThat(countriesHolder.countriesExists(Set.of(new Country("BEB"), new Country("FEV")))).isFalse();
    }
}
