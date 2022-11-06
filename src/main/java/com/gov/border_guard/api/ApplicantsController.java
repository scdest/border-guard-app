package com.gov.border_guard.api;

import com.gov.border_guard.dtos.ApplicantDto;
import com.gov.border_guard.dtos.ApplicationResponse;
import com.gov.border_guard.mapper.DtoToEntityMapper;
import com.gov.border_guard.services.ApplicantsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ApplicantsController {
    private final ApplicantsService applicantsService;
    private final DtoToEntityMapper dtoToEntityMapper;

    @PostMapping("/application")
    public ResponseEntity<ApplicationResponse> checkApplicant(@RequestBody ApplicantDto applicantDto) {
        return ResponseEntity.ok(
                applicantsService.checkApplicantValid(
                        dtoToEntityMapper.toEntity(applicantDto)
                )
        );
    }
}
