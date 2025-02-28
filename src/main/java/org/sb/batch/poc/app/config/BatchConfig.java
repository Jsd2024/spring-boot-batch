package org.sb.batch.poc.app.config;


import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

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
    public CustomerProcessor processor(){
        return new CustomerProcessor();
    }

    @Bean
    public Step stepCustom( StepBuilderFactory stepBuilderFactory,
                           CustomReader reader, CustomerProcessor processor, CustomWriter writer) {
        return  stepBuilderFactory.get("step")
                .<CustomerRdbms, CustomerRdbms>chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    @Bean(name = "csv-job")
    public Job job(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                             CustomReader reader, CustomerProcessor processor, CustomWriter writer) {

        return jobBuilderFactory.get("csv-job")
                .start(stepCustom(stepBuilderFactory,reader,processor,writer))
                .build();
    }
}
