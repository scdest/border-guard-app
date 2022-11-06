package com.gov.border_guard.entities;

import com.gov.border_guard.enums.DocumentType;
import lombok.Data;

import java.time.Instant;

@Data
public class ApplicantDocument {
    private String id;
    private Country country;
    private DocumentType documentType;
    private String firstName;
    private String lastName;
    private Instant expiresAt;
}
