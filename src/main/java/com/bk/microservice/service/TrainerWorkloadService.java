package com.bk.microservice.service;

import com.bk.microservice.dto.WorkloadUpdateRequest;
import com.bk.microservice.entity.TrainerWorkload;
import com.bk.microservice.repository.TrainerWorkloadRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrainerWorkloadService {
    private final TrainerWorkloadRepository repository;

    public TrainerWorkloadService(TrainerWorkloadRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void updateWorkload(WorkloadUpdateRequest req, String transactionId) {
        log.info("[{}] Update workload request: {}", transactionId, req);
        TrainerWorkload workload = repository.findById(req.getTrainerUsername())
                .orElseGet(() -> {
                    TrainerWorkload tw = new TrainerWorkload();
                    tw.setTrainerUsername(req.getTrainerUsername());
                    tw.setFirstName(req.getFirstName());
                    tw.setLastName(req.getLastName());
                    tw.setActive(req.isActive());
                    return tw;
                });

        int year = req.getTrainingDate().getYear();

        workload.setFirstName(req.getFirstName());
        workload.setLastName(req.getLastName());
        workload.setActive(req.isActive());

        workload.getWorkload().add(year);

        repository.save(workload);
        log.info("[{}] Workload updated for trainer: {}", transactionId, req.getTrainerUsername());
    }

    public TrainerWorkload getMonthlySummary(String trainerUsername, int year, int month, String transactionId) {
        log.info("[{}] Get monthly summary for trainer: {}, year: {}", transactionId, trainerUsername, year);
        return repository.findById(trainerUsername).orElse(null);
    }
}
