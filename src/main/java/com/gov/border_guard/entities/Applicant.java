package com.gov.border_guard.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document(collection = "applicants")
@Data
public class Applicant {
    @Id
    private String id;
    private Boolean allowed;
    private ApplicantDocument document;
    private Set<String> deniedReasons;
}
