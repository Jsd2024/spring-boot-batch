//package org.sb.batch.poc.app.config;
//
//
//import lombok.AllArgsConstructor;
//import org.sb.batch.poc.app.model.CustomerRdbms;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
////@AllArgsConstructor
//public class SpringBatchConfig {
//
//    @Bean
//    public CustomerProcessor processor(){
//        return new CustomerProcessor();
//    }
//
//
//    @Bean
//    public TaskExecutor taskExecutor() {
//        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
//        asyncTaskExecutor.setConcurrencyLimit(10);//TBD to test
//        return asyncTaskExecutor;
//    }
//
////custom writer
//
//    @Bean
//    public CustomWriter customWriter() {
//        CustomWriter writer = new CustomWriter();
//        return writer;
//    }
//
//    @Bean
//    public CustomReader customReader(){
//        CustomReader itemReader = new CustomReader();
//        return itemReader;
//    }
//
//    @Bean
//    public Step csvStep(CustomReader reader, CustomWriter writer) {
//        return stepBuilderFactory.get("csv-step")
//                .<CustomerRdbms, CustomerRdbms>chunk(50)
//                .reader(reader)
//                .writer(writer)
//                .build();
//    }
//    private final JobBuilderFactory jobBuilderFactory;
//    private final StepBuilderFactory stepBuilderFactory;
//
//    public SpringBatchConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
//        this.jobBuilderFactory = jobBuilderFactory;
//        this.stepBuilderFactory = stepBuilderFactory;
//    }
//
//    @Bean
//    public JobBuilderFactory jobBuilderFactory() {
//        return new JobBuilderFactory(jobBuilderFactory);
//    }
//
//    @Bean
//    public StepBuilderFactory stepBuilderFactory() {
//        return new StepBuilderFactory(getJobRepository(), getTransactionManager());
//    }
//
//
//    @Bean(name = "csv-job")
//    public Job csvJob(Step step) {
//        return jobBuilderFactory.get("csv-job")
//                .incrementer(new RunIdIncrementer())
//                .start(step)
//                .build();
//    }
//
//}