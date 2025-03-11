package org.sb.batch.poc.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.sb.batch.poc.app.model.ChunkData;
import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.item.ItemWriter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CustomWriter implements ItemWriter<CustomerRdbms> {

	private static final AtomicInteger chunkCounter = new AtomicInteger(0);
	private final ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public void write(List<? extends CustomerRdbms> chunkItems) throws Exception {
		int chunkId = chunkCounter.getAndIncrement();
		// Create a JSON file for each chunk
		File jsonFile = new File("src/main/resources/out/customers_chunk_" + chunkId + ".json");

		// Wrap data with chunkId
		ChunkData chunkData = new ChunkData(chunkId,
                new ArrayList<>(chunkItems));

		// Write JSON file
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable Pretty Printing
		objectMapper.writeValue(jsonFile, chunkData);

		System.out.println("Written Chunk: " + chunkId + " to file: " + jsonFile.getAbsolutePath());
		System.out.println("Completed writing  CustomerRdbms data."+chunkItems);

	}
}