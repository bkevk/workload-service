package com.bk.microservice.repository;

import com.bk.microservice.entity.TrainerWorkload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrainerWorkloadRepository extends JpaRepository<TrainerWorkload, String> {}
