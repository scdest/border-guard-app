package com.gov.border_guard.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "countries")
@Getter
@EqualsAndHashCode
@ToString
public class Country {
    @Id
    private final String code;

    public Country(String code) {
        this.code = code;
    }
}
