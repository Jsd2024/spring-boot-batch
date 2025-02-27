package org.sb.batch.poc.app.config;

import org.sb.batch.poc.app.config.CustomReader;
import org.sb.batch.poc.app.config.CustomWriter;
import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import javax.sql.DataSource;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    public BatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
    }

    @Bean
    public CustomWriter customWriter() {
        CustomWriter writer = new CustomWriter();
        return writer;
    }

    @Bean
    public CustomReader customReader(){
        CustomReader itemReader = new CustomReader();
        return itemReader;
    }

    @Bean
    public Job csvJob(Step csvStep) {
        return jobBuilderFactory.get("csv-job")
                .incrementer(new RunIdIncrementer())
                .start(csvStep)
                .build();
    }

    @Bean
    public Step csvStep(CustomReader reader, CustomWriter writer,
                        PlatformTransactionManager transactionManager) {
        return stepBuilderFactory.get("csv-step")
                .<CustomerRdbms, CustomerRdbms>chunk(50)
                .reader(reader)
                .writer(writer)
                .transactionManager(transactionManager)
                .build();
    }
}
