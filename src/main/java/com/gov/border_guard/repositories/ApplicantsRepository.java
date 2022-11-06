package com.gov.border_guard.repositories;

import com.gov.border_guard.entities.Applicant;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicantsRepository extends MongoRepository<Applicant, String> {

}
