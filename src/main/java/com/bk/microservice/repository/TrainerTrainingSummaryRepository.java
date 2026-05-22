package com.bk.microservice.repository;

import com.bk.microservice.entity.TrainerTrainingSummary;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainerTrainingSummaryRepository extends MongoRepository<TrainerTrainingSummary, String> {
    List<TrainerTrainingSummary> findByFirstNameAndLastName(String firstName, String lastName);
}
