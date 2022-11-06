package com.gov.border_guard.repositories;

import com.gov.border_guard.entities.BorderRulesEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BorderRulesRepository extends MongoRepository<BorderRulesEntity, String> {
}
