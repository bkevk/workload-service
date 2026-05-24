package com.bk.microservice.service;

import com.bk.microservice.dto.WorkloadUpdateRequest;
import com.bk.microservice.entity.TrainerTrainingSummary;
import com.bk.microservice.repository.TrainerTrainingSummaryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class TrainerTrainingSummaryService {

    private final TrainerTrainingSummaryRepository repository;

    public TrainerTrainingSummaryService(TrainerTrainingSummaryRepository repository) {
        this.repository = repository;
    }

    public void processEvent(WorkloadUpdateRequest request, String transactionId) {
        log.info("[{}] Processing event for trainer: {}", transactionId, request.getTrainerUsername());

        if (request.getTrainerUsername() == null || request.getTrainerUsername().isEmpty()) {
            log.warn("[{}] Trainer username is required", transactionId);
            throw new IllegalArgumentException("Trainer username is required");
        }
        if (request.getFirstName() == null || request.getLastName() == null) {
            log.warn("[{}] Trainer first and last name are required", transactionId);
            throw new IllegalArgumentException("Trainer first and last name are required");
        }
        if (request.getTrainingDate() == null) {
            log.warn("[{}] Training date is required", transactionId);
            throw new IllegalArgumentException("Training date is required");
        }

        int year = request.getTrainingDate().getYear();
        int month = request.getTrainingDate().getMonthValue();

        TrainerTrainingSummary summary = repository.findById(request.getTrainerUsername()).orElse(null);

        if (summary == null) {
            log.info("[{}] Trainer not found, creating new summary", transactionId);
            summary = TrainerTrainingSummary.builder()
                    .trainerUsername(request.getTrainerUsername())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .status(request.isActive())
                    .years(new HashMap<>())
                    .build();
        } else {
            summary = TrainerTrainingSummary.builder()
                            .firstName(request.getFirstName())
                            .lastName(request.getLastName())
                            .status(request.isActive())
                            .build();
        }

        Map<Integer, Map<Integer, Integer>> years = summary.getYears();
        Map<Integer, Integer> months = years.computeIfAbsent(year, y -> new HashMap<>());
        int currentDuration = months.getOrDefault(month, 0);

        int newDuration = currentDuration + request.getTrainingDuration();
        months.put(month, newDuration);
        years.put(year, months);
        summary.setYears(years);

        repository.save(summary);
        log.info("[{}] Updated summary for trainer: {} year: {} month: {} duration: {}", transactionId, request.getTrainerUsername(), year, month, newDuration);
    }
}

