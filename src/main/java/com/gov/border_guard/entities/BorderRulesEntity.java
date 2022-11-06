package com.gov.border_guard.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Set;

@Document
@Data
@EqualsAndHashCode(exclude = "id")
public class BorderRulesEntity {
    @Id
    private String id;
    private Set<Country> deniedCountries;
    private Set<String> wantedCriminalsIds;
}
