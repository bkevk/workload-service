package com.bk.microservice.controller;

import com.bk.microservice.dto.WorkloadUpdateRequest;
import com.bk.microservice.entity.TrainerWorkload;
import com.bk.microservice.service.TrainerWorkloadService;
import com.bk.microservice.util.TransactionIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/workload")
public class TrainerWorkloadController {
    private final TrainerWorkloadService service;

    public TrainerWorkloadController(TrainerWorkloadService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> updateWorkload(
            @RequestBody WorkloadUpdateRequest request,
            @RequestHeader(value = "Authorization") String bearerToken,
            @RequestHeader(value = "Transaction-Id", required = false) String transactionId) {
        transactionId = TransactionIdGenerator.ensureTransactionId(transactionId);
        log.info("[{}] /api/workload called with request: {}", transactionId, request);
        // JWT validation logic here (see below)
        service.updateWorkload(request, transactionId);
        log.info("[{}] /api/workload response: 200 OK", transactionId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{trainerUsername}/{year}/{month}")
    public ResponseEntity<TrainerWorkload> getMonthlySummary(
            @PathVariable String trainerUsername,
            @PathVariable int year,
            @PathVariable int month,
            @RequestHeader(value = "Authorization") String bearerToken,
            @RequestHeader(value = "Transaction-Id", required = false) String transactionId) {
        transactionId = TransactionIdGenerator.ensureTransactionId(transactionId);
        log.info("[{}] /api/workload/{}/{}/{} called", transactionId, trainerUsername, year, month);

        TrainerWorkload summary = service.getMonthlySummary(trainerUsername, year, month, transactionId);
        if (summary == null) {
            log.warn("[{}] Trainer not found: {}", transactionId, trainerUsername);
            return ResponseEntity.notFound().build();
        }
        log.info("[{}] /api/workload response: 200 OK", transactionId);
        return ResponseEntity.ok(summary);
    }
}
