package com.bk.microservice.component.steps;

import com.bk.microservice.dto.WorkloadUpdateRequest;
import com.bk.microservice.entity.TrainerTrainingSummary;
import com.bk.microservice.repository.TrainerTrainingSummaryRepository;
import com.bk.microservice.service.TrainerTrainingSummaryService;
import io.cucumber.java.en.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ContextConfiguration
public class WorkloadComponentSteps {

    @Autowired
    private TrainerTrainingSummaryService service;

    @Autowired
    private TrainerTrainingSummaryRepository repository;

    private String transactionId = "tx-test";
    private WorkloadUpdateRequest lastRequest;

    @Given("the trainer workload database is empty")
    public void the_database_is_empty() {
        repository.deleteAll();
    }

    @Given("a trainer summary exists for {string} with {int} minutes in year {int} month {int}")
    public void a_trainer_summary_exists(String username, int minutes, int year, int month) {
        repository.deleteAll();
        Map<Integer, Map<Integer, Integer>> years = new HashMap<>();
        Map<Integer, Integer> months = new HashMap<>();
        months.put(month, minutes);
        years.put(year, months);
        TrainerTrainingSummary summary = TrainerTrainingSummary.builder()
                        .trainerUsername(username)
                                .firstName(username.split("\\.")[0])
                                        .lastName(username.split("\\.")[1])
                                                .status(true)
                                                        .years(years).build();
        repository.save(summary);
    }

    @When("a workload event is received for trainer {string} with first name {string}, last name {string}, status {word}, date {string}, duration {int}, action {string}")
    public void a_workload_event_is_received(String username, String firstName, String lastName, String status, String date, int duration, String action) {
        WorkloadUpdateRequest req = new WorkloadUpdateRequest();
        req.setTrainerUsername(username);
        req.setFirstName(firstName);
        req.setLastName(lastName);
        req.setActive(Boolean.parseBoolean(status));
        req.setTrainingDate(LocalDate.parse(date));
        req.setTrainingDuration(duration);
        req.setActionType(action);
        lastRequest = req;
        service.processEvent(req, transactionId);
    }

    @Then("the trainer summary for {string} should have {int} minutes for year {int} and month {int}")
    public void the_trainer_summary_should_have_minutes(String username, int expectedMinutes, int year, int month) {
        TrainerTrainingSummary summary = repository.findById(username).orElse(null);
        assertNotNull(summary, "Trainer summary should exist");
        assertTrue(summary.getYears().containsKey(year), "Year should exist");
        assertTrue(summary.getYears().get(year).containsKey(month), "Month should exist");
        assertEquals(expectedMinutes, summary.getYears().get(year).get(month));
    }
}
