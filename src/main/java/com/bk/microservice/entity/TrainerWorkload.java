package com.bk.microservice.entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class TrainerWorkload {
    @Id
    private String trainerUsername;
    private String firstName;
    private String lastName;
    private boolean isActive;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<Integer> workload = new ArrayList<>();
}
