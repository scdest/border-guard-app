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
public interface DtoToEntityMapper {
    BorderRulesEntity toEntity(BorderRulesDto source);
    Applicant toEntity(ApplicantDto source);
    ApplicantDocument toEntity(ApplicantDocumentDto source);
    Country toEntity(CountryDto source);
}
