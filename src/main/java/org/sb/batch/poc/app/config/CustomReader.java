package org.sb.batch.poc.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.item.ItemReader;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Paths;
import java.util.Arrays;

public class CustomReader
//        extends FlatFileItemReader<CustomerRdbms>
        implements ItemReader<CustomerRdbms>
{
    private final RandomAccessFile raf;
    private final File statusFile = Paths.get("status.json").toFile();
    private long seekPosition = 0;
    private int processedCount = 0;

    public CustomReader(String filePath)  {
        try {
            this.raf = new RandomAccessFile(filePath, "r");

        loadSeekPosition();
        raf.seek(seekPosition); // Resume from last position

        // Skip the header only if starting from the beginning
        if (seekPosition == 0) {
            raf.readLine();  // Read and ignore the first line
        }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadSeekPosition() {
        try {
            if (statusFile.exists()) {
                ObjectMapper mapper = new ObjectMapper();
                CustomReader.ProcessingStatus status = mapper.readValue(statusFile, CustomReader.ProcessingStatus.class);
                this.seekPosition = status.getSeekPosition();
                this.processedCount = status.getProcessedCount();
            }
        } catch (IOException e) {
            System.out.println("No previous status found, starting from beginning.");
        }
    }

    @Override
    public CustomerRdbms read() throws Exception {
        String line = raf.readLine();
        if (line == null) {
            saveSeekPosition();  // Final save
            return null;
        }

        CustomerRdbms record = parseLine(line);
        processedCount++;

        // Save progress every 1000 records
        if (processedCount % 1000 == 0) {
            saveSeekPosition();
        }

        return record;
    }

    private CustomerRdbms parseLine(String line) {
        String[] fields = line.split(",");
        CustomerRdbms record = new CustomerRdbms();
        record.setId(Integer.parseInt(fields[0]));
        record.setName(fields[1]);
        record.setEmails(Arrays.asList(fields[2].split("\\|")));
        return record;
    }

    private void saveSeekPosition() throws IOException {
        CustomReader.ProcessingStatus status = new CustomReader.ProcessingStatus(System.currentTimeMillis(), raf.getFilePointer(), processedCount);
        new ObjectMapper().writeValue(statusFile, status);
    }

    private static class ProcessingStatus {
        public long timestamp;
        public long seekPosition;
        public int processedCount;

        public ProcessingStatus() {}
        public ProcessingStatus(long timestamp, long seekPosition, int processedCount) {
            this.timestamp = timestamp;
            this.seekPosition = seekPosition;
            this.processedCount = processedCount;
        }

        public long getSeekPosition() { return seekPosition; }
        public int getProcessedCount() { return processedCount; }
    }



//    public CustomReader() {
//        setResource(new FileSystemResource("src/main/resources/data.csv"));
//        setName("csv-reader");
//        setLinesToSkip(1); // Skip header row
//        setLineMapper(lineMapper());
//    }
//
//    private LineMapper<CustomerRdbms> lineMapper() {
//        DefaultLineMapper<CustomerRdbms> lineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        tokenizer.setDelimiter(",");
//        tokenizer.setNames("id","name","email");
//        tokenizer.setStrict(false);
//
//        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper<>();
//        mapper.setTargetType(CustomerRdbms.class);
//
//        lineMapper.setFieldSetMapper(mapper);
//        lineMapper.setLineTokenizer(tokenizer);
//        return lineMapper;
//    }
}












//// {

//
//	public CustomReader() {
//		setResource(new FileSystemResource("src/main/resources/customers.csv"));
//		setName("csv-reader");
//		setLinesToSkip(1); // Skip header row
//		setLineMapper(lineMapper());
//	}
//
//	private LineMapper<CustomerRdbms> lineMapper() {
//        DefaultLineMapper<CustomerRdbms> lineMapper = new DefaultLineMapper<>();
//
//        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
//        tokenizer.setDelimiter(",");
//        tokenizer.setNames("id","name","email");
//        tokenizer.setStrict(false);
//
//        BeanWrapperFieldSetMapper mapper = new BeanWrapperFieldSetMapper<>();
//        mapper.setTargetType(CustomerRdbms.class);
//
//        lineMapper.setFieldSetMapper(mapper);
//        lineMapper.setLineTokenizer(tokenizer);
//        return lineMapper;
//    }
//}