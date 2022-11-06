package com.gov.border_guard.services;

import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class InternalClock {
    public Instant now() {
        return Instant.now();
    }
}
