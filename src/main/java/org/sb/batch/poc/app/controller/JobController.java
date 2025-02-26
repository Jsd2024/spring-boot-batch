package org.sb.batch.poc.app.controller;

import org.sb.batch.poc.app.model.CustomerRdbms;
//import org.sb.batch.poc.app.repository.CustomerNoSqlRepository;
import org.sb.batch.poc.app.repository.CustomerRdbmsRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

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
    @RequestMapping(value = "/insert/customers/rdbms", method = RequestMethod.POST)
    public ResponseEntity<List<CustomerRdbms>> createEmpDataRdbms
            (@RequestBody CustomerRdbms customer) {
        customerRdbmsRepository.save(customer);
        List<CustomerRdbms> customers = List.of(customer);
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }
    @RequestMapping(value = "/json", method = RequestMethod.POST)
    public ResponseEntity<?> createJson
            (@RequestBody CustomerRdbms customer) throws FileNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        File file = new
                File(Objects.requireNonNull(classLoader.getResourceAsStream("/Response.json")).toString());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        OutputStreamWriter outputStreamWriter = new
                OutputStreamWriter(fileOutputStream, StandardCharsets.UTF_8);
        BufferedWriter writer = new BufferedWriter(outputStreamWriter);
        try {
           String result = "user Should be active user";
            writer.write(result);
            writer.close();
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity<>(file, HttpStatus.CREATED);
    }
}