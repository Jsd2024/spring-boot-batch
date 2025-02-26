package org.sb.batch.poc.app.config;


import lombok.AllArgsConstructor;
import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class SpringBatchConfig {

    @Bean
    public CustomerProcessor processor(){
        return new CustomerProcessor();
    }


    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);//TBD to test
        return asyncTaskExecutor;
    }

//custom writer

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
    public Step stepCustom(JobRepository repository,
                     PlatformTransactionManager transactionManager,
                     ItemReader<CustomerRdbms> itemReader,
                     ItemProcessor<CustomerRdbms, CustomerRdbms> processor,
                     ItemWriter<CustomerRdbms> customerRdbmsItemWriter,
                     TaskExecutor taskExecutor) {
        return new StepBuilder("csv-step", repository)
                .<CustomerRdbms, CustomerRdbms>chunk(10, transactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(customerRdbmsItemWriter)  // Injecting the custom writer
//                .taskExecutor(taskExecutor)// deactivate this to maintain order
                .build();
    }

    @Bean(name = "csv-job")
    public Job job(JobRepository jobRepository,
                   PlatformTransactionManager transactionManager,
                   Step stepCustom) {
        return new JobBuilder("csv-job", jobRepository)
                .flow(stepCustom)
                .end()
                .build();
    }
}