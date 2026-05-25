Feature: Trainer Workload Event Processing

  Scenario: Process a new training event for a new trainer
    Given the trainer workload database is empty
    When a workload event is received for trainer "Archil.Bakradze" with first name "Archil", last name "Bakradze", status true, date "2024-05-01", duration 60, action "ADD"
    Then the trainer summary for "Archil.Bakradze" should have 60 minutes for year 2024 and month 5

  Scenario: Process a new training event for an existing trainer and month
    Given a trainer summary exists for "Bidzina.Kevkhishvili" with 30 minutes in year 2024 month 5
    When a workload event is received for trainer "Bidzina.Kevkhishvili" with first name "Bidzina", last name "Kevkhishvili", status true, date "2024-05-01", duration 45, action "ADD"
    Then the trainer summary for "Bidzina.Kevkhishvili" should have 75 minutes for year 2024 and month 5