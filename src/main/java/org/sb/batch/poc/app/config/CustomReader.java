package org.sb.batch.poc.app.config;

import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;

public class CustomReader extends FlatFileItemReader<CustomerRdbms> implements ItemReader<CustomerRdbms> {

    public CustomReader() {
        setResource(new FileSystemResource("src/main/resources/customers.csv"));
        setName("csv-reader");
        setLinesToSkip(1); // Skip header row
        setLineMapper(lineMapper());
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
}