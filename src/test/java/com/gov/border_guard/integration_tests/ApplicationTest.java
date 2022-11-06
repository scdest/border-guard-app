package com.gov.border_guard.integration_tests;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gov.border_guard.dtos.ApplicantDocumentDto;
import com.gov.border_guard.dtos.ApplicantDto;
import com.gov.border_guard.dtos.CountryDto;
import com.gov.border_guard.entities.BorderRulesEntity;
import com.gov.border_guard.entities.Country;
import com.gov.border_guard.enums.DocumentType;
import com.gov.border_guard.repositories.BorderRulesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTest {

    @Autowired
    private BorderRulesRepository borderRulesRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setup() {
        BorderRulesEntity entity = new BorderRulesEntity();
        entity.setId("BORDER_RULES");
        entity.setDeniedCountries(Set.of(new Country("UKR"), new Country("USA")));
        entity.setWantedCriminalsIds(Set.of("1", "2", "3"));
        borderRulesRepository.save(entity);
    }

    @Test
    public void testApplicantIsAllowed() throws Exception {
        ApplicantDto applicantDto = new ApplicantDto();
        ApplicantDocumentDto documentDto = new ApplicantDocumentDto();
        documentDto.setDocumentType(DocumentType.ID_CARD);
        documentDto.setCountry(new CountryDto("GBR"));
        documentDto.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));
        applicantDto.setDocument(documentDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/application")
                .content(objectMapper.writeValueAsString(applicantDto)).
                contentType("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$.allowed").value("true"));
    }

    @Test
    public void testApplicantIsNotAllowed() throws Exception {
        ApplicantDto applicantDto = new ApplicantDto();
        ApplicantDocumentDto documentDto = new ApplicantDocumentDto();
        documentDto.setDocumentType(DocumentType.INTERNATIONAL_PASSPORT);
        documentDto.setCountry(new CountryDto("UKR"));
        documentDto.setExpiresAt(Instant.now().plus(1, ChronoUnit.DAYS));
        applicantDto.setDocument(documentDto);
        mockMvc.perform(MockMvcRequestBuilders.post("/application")
                        .content(objectMapper.writeValueAsString(applicantDto)).
                        contentType("application/json"))
                .andDo(print())
                .andExpect(jsonPath("$.allowed").value("false"))
                .andExpect(jsonPath("$.deniedReasons").value("Your country is in denied countries list"));
    }

}
