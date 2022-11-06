package com.gov.border_guard.dtos;

import com.gov.border_guard.entities.Country;
import com.gov.border_guard.enums.DocumentType;
import lombok.Data;

import java.time.Instant;
@Data
public class ApplicantDocumentDto {
    private String id;
    private CountryDto country;
    private DocumentType documentType;
    private String firstName;
    private String lastName;
    private Instant expiresAt;
}
