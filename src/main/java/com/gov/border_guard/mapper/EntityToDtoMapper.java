package com.gov.border_guard.mapper;

import com.gov.border_guard.dtos.ApplicantDocumentDto;
import com.gov.border_guard.dtos.ApplicantDto;
import com.gov.border_guard.dtos.BorderRulesDto;
import com.gov.border_guard.dtos.CountryDto;
import com.gov.border_guard.entities.Applicant;
import com.gov.border_guard.entities.ApplicantDocument;
import com.gov.border_guard.entities.BorderRulesEntity;
import com.gov.border_guard.entities.Country;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EntityToDtoMapper {
    BorderRulesDto toDto(BorderRulesEntity source);
    ApplicantDto toDto(Applicant source);
    ApplicantDocumentDto toDto(ApplicantDocument source);
    CountryDto toDto(Country source);
}
