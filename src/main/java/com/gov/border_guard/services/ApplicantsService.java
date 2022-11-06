package com.gov.border_guard.services;

import com.gov.border_guard.dtos.ApplicationResponse;
import com.gov.border_guard.entities.Applicant;
import com.gov.border_guard.enums.DocumentType;
import com.gov.border_guard.repositories.ApplicantsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ApplicantsService {

    private static final String DENIED_COUNTRY_REASON = "Your country is in denied countries list";
    private static final String WANTED_CRIMINAL_REASON = "You are a wanted criminal";

    private static final String DOCUMENT_EXPIRED_REASON = "Your document is expired";

    private final BorderRulesService borderRulesService;
    private final ApplicantsRepository applicantsRepository;

    private final InternalClock internalClock;

    public ApplicationResponse checkApplicantValid(Applicant applicant) {
        Set<String> deniedReasons = new HashSet<>();
        if (DocumentType.INTERNATIONAL_PASSPORT == applicant.getDocument().getDocumentType()) {
            if (borderRulesService.isDeniedCountry(applicant.getDocument().getCountry())) {
                deniedReasons.add(DENIED_COUNTRY_REASON);
            }
        }

        if (borderRulesService.isWantedCriminal(applicant.getDocument().getId())) {
            deniedReasons.add(WANTED_CRIMINAL_REASON);
        }

        if (applicant.getDocument().getExpiresAt().isBefore(internalClock.now())) {
            deniedReasons.add(DOCUMENT_EXPIRED_REASON);
        }

        applicant.setAllowed(deniedReasons.isEmpty());
        applicant.setDeniedReasons(deniedReasons);
        applicant = applicantsRepository.save(applicant);
        return new ApplicationResponse(applicant.getId(), applicant.getAllowed(), applicant.getDeniedReasons());
    }
}
