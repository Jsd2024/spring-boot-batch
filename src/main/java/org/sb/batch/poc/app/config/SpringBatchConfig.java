package org.sb.batch.poc.app.config;


import jakarta.persistence.EntityManagerFactory;
import lombok.AllArgsConstructor;
import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@AllArgsConstructor
public class SpringBatchConfig {
    @Bean
    public FlatFileItemReader<CustomerRdbms> itemReader(){
        FlatFileItemReader<CustomerRdbms> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csv-reader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    private LineMapper<CustomerRdbms> lineMapper() {
        DefaultLineMapper<CustomerRdbms> lineMapper = new DefaultLineMapper<>();

        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("id","name","email");
        tokenizer.setStrict(false);

        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper<>();
        mapper.setTargetType(CustomerRdbms.class);

        lineMapper.setFieldSetMapper(mapper);
        lineMapper.setLineTokenizer(tokenizer);
        return lineMapper;
    }

    @Bean
    public CustomerProcessor processor(){
        return new CustomerProcessor();
    }

    @Bean
    public JpaItemWriter<CustomerRdbms> jpaItemWriter(EntityManagerFactory entityManagerFactory) {
        JpaItemWriter<CustomerRdbms> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        writer.setUsePersist(true);
        return writer;
    }

    /**
     * @param repository
     * @param transactionManager
     * @param itemReader
     * @param processor
     * @param jpaItemWriter
     * @param taskExecutor
     * @return
     */
    @Bean
    public Step step(JobRepository repository,
                     PlatformTransactionManager transactionManager,
                     ItemReader<CustomerRdbms> itemReader,
                     ItemProcessor<CustomerRdbms, CustomerRdbms> processor,
                     JpaItemWriter<CustomerRdbms> jpaItemWriter,
                     TaskExecutor taskExecutor) {
        return new StepBuilder("csv-step", repository)
                .<CustomerRdbms, CustomerRdbms>chunk(10, transactionManager)
                .reader(itemReader)
                .processor(processor)
                .writer(jpaItemWriter)  // Injecting the JPA writer
                .taskExecutor(taskExecutor)
                .build();
    }
    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);//TBD to test
        return asyncTaskExecutor;
    }
    @Bean(name = "csv-job")
    public Job job(JobRepository jobRepository,
                   PlatformTransactionManager transactionManager,
                   Step step) {
        return new JobBuilder("csv-job", jobRepository)
                .flow(step)
                .end()
                .build();
    }



}