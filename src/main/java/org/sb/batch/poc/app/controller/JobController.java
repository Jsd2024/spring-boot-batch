package org.sb.batch.poc.app.controller;

import lombok.extern.slf4j.Slf4j;
import org.sb.batch.poc.app.repository.CustomerRdbmsRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
public class JobController {

    @Autowired(required = false)
    JobLauncher jobLauncher;

    @Autowired
    CustomerRdbmsRepository customerRdbmsRepository;
    @Autowired
    Job processJob;

    @RequestMapping("/invokejob")
    public String handle() throws Exception {

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("startAt", System.currentTimeMillis()).toJobParameters();

        jobLauncher.run(processJob, jobParameters);

        return "Batch job has been invoked";//{_id: 1}

    }

    @GetMapping("/run")
    public ResponseEntity<String> runBatchJob() {
        try {
            JobExecution jobExecution = jobLauncher.run(processJob, new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters());
            return ResponseEntity.ok("Batch job has been invoked: " + jobExecution.getStatus());
        } catch (Exception e) {
            log.error("Batch job failed: {}", e.getMessage());
            return ResponseEntity.status(500).body("Batch job failed: " + e.getMessage());
        }
    }


}