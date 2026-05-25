package com.bk.microservice.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "trainer_training_summary")
@CompoundIndex(name = "name_idx", def = "{'firstName': 1, 'lastName': 1}")
@Builder
@Getter
@Setter
public class TrainerTrainingSummary {
    @Id
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean status;

    private Map<Integer, Map<Integer, Integer>> years = new HashMap<>();
}
