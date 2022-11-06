package com.gov.border_guard.dtos;

import lombok.Data;

import java.util.Set;

@Data
public class ApplicantDto {
    private String id;
    private Boolean allowed;
    private ApplicantDocumentDto document;
    private Set<String> deniedReasons;
}
