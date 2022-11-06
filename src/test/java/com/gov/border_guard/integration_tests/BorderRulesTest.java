package com.gov.border_guard.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.border_guard.dtos.BorderRulesDto;
import com.gov.border_guard.dtos.CountryDto;
import com.gov.border_guard.entities.BorderRulesEntity;
import com.gov.border_guard.entities.Country;
import com.gov.border_guard.mapper.DtoToEntityMapper;
import com.gov.border_guard.mapper.EntityToDtoMapper;
import com.gov.border_guard.repositories.BorderRulesRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class BorderRulesTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper jsonMapper;
    @Autowired
    private BorderRulesRepository repository;
    @Autowired
    private DtoToEntityMapper dtoToEntityMapper;

    @Autowired
    private EntityToDtoMapper entityToDtoMapper;

    @Test
    public void testBorderRulesSavedToDb() throws Exception {
        BorderRulesDto dto = new BorderRulesDto(Set.of(new CountryDto("UKR")), Set.of("1", "2"));
        mockMvc.perform(MockMvcRequestBuilders.post("/border-rules")
                .content(jsonMapper.writeValueAsString(dto)).
                contentType("application/json"));
        Assertions.assertThat(dtoToEntityMapper.toEntity(dto)).isEqualTo(repository.findAll().get(0));
    }

    @Test
    public void testBorderRulesUpdatedWhenAlreadyExists() throws Exception {
        BorderRulesEntity entity = new BorderRulesEntity();
        entity.setId("BORDER_RULES");
        entity.setDeniedCountries(Set.of(new Country("UKR"), new Country("USA")));
        entity.setWantedCriminalsIds(Set.of("1", "2", "3"));
        repository.save(entity);

        BorderRulesDto dto = new BorderRulesDto(Set.of(new CountryDto("UKR")), Set.of("1", "2"));

        mockMvc.perform(MockMvcRequestBuilders.post("/border-rules")
                .content(jsonMapper.writeValueAsString(dto)).
                contentType("application/json"));
        Assertions.assertThat(repository.count()).isEqualTo(1);
        Assertions.assertThat(dtoToEntityMapper.toEntity(dto)).isEqualTo(repository.findAll().get(0));
    }

    @Test
    public void testGetBorderRules() throws Exception {
        BorderRulesEntity entity = new BorderRulesEntity();
        entity.setId("BORDER_RULES");
        entity.setDeniedCountries(Set.of(new Country("UKR"), new Country("USA")));
        entity.setWantedCriminalsIds(Set.of("1", "2", "3"));
        repository.save(entity);

        mockMvc.perform(MockMvcRequestBuilders.get("/border-rules"))
                .andExpectAll(
                        status().isOk(),
                        content().json(
                                jsonMapper.writeValueAsString(
                                        entityToDtoMapper.toDto(entity)
                                )
                        )
                );
    }
}
