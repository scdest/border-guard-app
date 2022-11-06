package com.gov.border_guard.unit_tests;

import com.gov.border_guard.dtos.ApplicationResponse;
import com.gov.border_guard.entities.Applicant;
import com.gov.border_guard.entities.ApplicantDocument;
import com.gov.border_guard.entities.Country;
import com.gov.border_guard.enums.DocumentType;
import com.gov.border_guard.repositories.ApplicantsRepository;
import com.gov.border_guard.services.ApplicantsService;
import com.gov.border_guard.services.BorderRulesService;
import com.gov.border_guard.services.InternalClock;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationServiceTest {

    @Mock
    private BorderRulesService borderRulesService;
    @Mock
    private ApplicantsRepository applicantsRepository;
    @Mock
    private InternalClock internalClock;
    @InjectMocks
    private ApplicantsService applicantsService;

    @BeforeEach
    public void setup() {
        when(internalClock.now()).thenReturn(Instant.parse("2022-12-03T10:15:30.00Z"));
    }

    @Test
    public void testApplicantAllowed() {
        Applicant applicant = getApplicant();

        when(applicantsRepository.save(any())).thenReturn(applicant);
        ApplicationResponse response = applicantsService.checkApplicantValid(applicant);

        Assertions.assertThat(response.getAllowed()).isTrue();
        verify(applicantsRepository).save(applicant);
    }

    @Test
    public void testApplicantDeniedDocumentExpired() {
        Applicant applicant = getApplicant();
        applicant.getDocument().setExpiresAt(Instant.parse("2021-12-03T10:15:30.00Z"));
        when(applicantsRepository.save(any())).thenReturn(applicant);
        ApplicationResponse response = applicantsService.checkApplicantValid(applicant);

        Assertions.assertThat(response.getAllowed()).isFalse();
        Assertions.assertThat(response.getDeniedReasons()).isEqualTo(Set.of("Your document is expired"));
        verify(applicantsRepository).save(applicant);
    }

    @Test
    public void testApplicantDeniedCountryDeniedForInternationalPassport() {
        Applicant applicant = getApplicant();
        applicant.getDocument().setDocumentType(DocumentType.INTERNATIONAL_PASSPORT);

        when(borderRulesService.isDeniedCountry(any())).thenReturn(true);
        when(applicantsRepository.save(any())).thenReturn(applicant);
        ApplicationResponse response = applicantsService.checkApplicantValid(applicant);

        Assertions.assertThat(response.getAllowed()).isFalse();
        Assertions.assertThat(response.getDeniedReasons()).isEqualTo(Set.of("Your country is in denied countries list"));
        verify(applicantsRepository).save(applicant);
    }

    @Test
    public void testApplicantDeniedWantedCriminal() {
        Applicant applicant = getApplicant();

        when(borderRulesService.isWantedCriminal(any())).thenReturn(true);
        when(applicantsRepository.save(any())).thenReturn(applicant);
        ApplicationResponse response = applicantsService.checkApplicantValid(applicant);

        Assertions.assertThat(response.getAllowed()).isFalse();
        Assertions.assertThat(response.getDeniedReasons()).isEqualTo(Set.of("You are a wanted criminal"));
        verify(applicantsRepository).save(applicant);
    }

    @Test
    public void testApplicantAllowedWithIdCardIfCountryDenied() {
        Applicant applicant = getApplicant();

        when(applicantsRepository.save(any())).thenReturn(applicant);
        ApplicationResponse response = applicantsService.checkApplicantValid(applicant);

        Assertions.assertThat(response.getAllowed()).isTrue();
        verify(borderRulesService, never()).isDeniedCountry(any());
        verify(applicantsRepository).save(applicant);
    }

    private Applicant getApplicant() {
        Applicant applicant = new Applicant();
        ApplicantDocument applicantDocument = new ApplicantDocument();
        applicantDocument.setDocumentType(DocumentType.ID_CARD);
        applicantDocument.setCountry(new Country("USA"));
        applicantDocument.setExpiresAt(Instant.parse("2023-12-03T10:15:30.00Z"));
        applicantDocument.setId("1");
        applicant.setDocument(applicantDocument);
        return applicant;
    }
}
