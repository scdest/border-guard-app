package com.gov.border_guard.unit_tests;

import com.gov.border_guard.entities.BorderRulesEntity;
import com.gov.border_guard.entities.Country;
import com.gov.border_guard.repositories.BorderRulesRepository;
import com.gov.border_guard.services.BorderRulesService;
import com.gov.border_guard.services.CountriesHolder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BorderRulesServiceTest {
    @Mock
    private CountriesHolder countriesHolder;
    @Mock
    private BorderRulesRepository repository;
    @InjectMocks
    private BorderRulesService service;

    @Test
    public void shouldSuccessfullySaveBorderRulesToDb() {
        when(countriesHolder.countriesExists(any())).thenReturn(true);
        BorderRulesEntity entity = new BorderRulesEntity();
        service.saveBorderRules(entity);

        entity.setId("BORDER_RULES");
        verify(repository).save(entity);
    }

    @Test
    public void shouldNotSaveBorderRulesToDbIfCountryDoesNotExists() {
        when(countriesHolder.countriesExists(any())).thenReturn(false);
        BorderRulesEntity entity = new BorderRulesEntity();

        Exception exception = assertThrows(
                RuntimeException.class,
                () -> service.saveBorderRules(entity)
        );

        String expectedMessage = "Invalid countries provided";
        String actualMessage = exception.getMessage();

        Assertions.assertThat(actualMessage).isEqualTo(expectedMessage);

        verify(repository, never()).save(entity);
    }

    @Test
    public void shouldCheckIfIsWantedCriminal() {
        BorderRulesEntity borderRulesEntity = new BorderRulesEntity();
        borderRulesEntity.setWantedCriminalsIds(Set.of("1", "2"));
        when(repository.findAll()).thenReturn(List.of(borderRulesEntity));

        Assertions.assertThat(service.isWantedCriminal("1")).isTrue();
        Assertions.assertThat(service.isWantedCriminal("5")).isFalse();
    }

    @Test
    public void shouldCheckIfIsDeniedCountry() {
        BorderRulesEntity borderRulesEntity = new BorderRulesEntity();
        borderRulesEntity.setDeniedCountries(Set.of(new Country("UKR"), new Country("USA")));
        when(repository.findAll()).thenReturn(List.of(borderRulesEntity));

        Assertions.assertThat(service.isDeniedCountry(new Country("GBP"))).isFalse();
        Assertions.assertThat(service.isDeniedCountry(new Country("USA"))).isTrue();
    }
}
