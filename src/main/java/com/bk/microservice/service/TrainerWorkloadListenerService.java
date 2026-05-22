package com.bk.microservice.service;

import com.bk.microservice.dto.WorkloadUpdateRequest;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
public class TrainerWorkloadListenerService {

    private final TrainerWorkloadService workloadService;
    private final TrainerTrainingSummaryService summaryService;

    public TrainerWorkloadListenerService(TrainerWorkloadService workloadService, TrainerTrainingSummaryService summaryService) {
        this.workloadService = workloadService;
        this.summaryService = summaryService;
    }

    @JmsListener(destination = "trainer.workload.queue")
    public void receiveWorkloadUpdate(WorkloadUpdateRequest request) {
        String transactionId = java.util.UUID.randomUUID().toString();
        workloadService.updateWorkload(request, transactionId);
        summaryService.processEvent(request, transactionId);
    }
}
