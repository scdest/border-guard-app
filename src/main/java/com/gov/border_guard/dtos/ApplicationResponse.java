package com.gov.border_guard.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
public class ApplicationResponse {
    private String id;
    private Boolean allowed;
    private Set<String> deniedReasons;
}
