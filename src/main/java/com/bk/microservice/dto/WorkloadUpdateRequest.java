package com.bk.microservice.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class WorkloadUpdateRequest {
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private LocalDate trainingDate;
    private int trainingDuration;
    private String actionType; // "ADD" or "DELETE"
}
