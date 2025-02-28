package org.sb.batch.poc.app.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.sb.batch.poc.app.model.ChunkData;
import org.sb.batch.poc.app.model.CustomerRdbms;
import org.sb.batch.poc.app.repository.CustomerRdbmsRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CustomWriter implements ItemWriter<CustomerRdbms> {
	@Autowired
	private CustomerRdbmsRepository customerRdbmsRepository;

	private static final AtomicInteger chunkCounter = new AtomicInteger(0);
	private final ObjectMapper objectMapper = new ObjectMapper();


	@Override
	public void write(Chunk<? extends CustomerRdbms> chunkItems) throws Exception {
		int chunkId = chunkCounter.getAndIncrement();
		// Create a JSON file for each chunk
		File jsonFile = new File("src/main/resources/out/customers_chunk_" + chunkId + ".json");

		// Wrap data with chunkId
		ChunkData chunkData = new ChunkData(chunkId,
				new ArrayList<>(chunkItems.getItems()));

		// Sort items by customerId
//		List<CustomerRdbms> sortedCustomers = chunkItems.getItems().stream()
//				.sorted(Comparator.comparingInt((CustomerRdbms c) -> c.getId()))
//				.collect(Collectors.toList());
		//ChunkData sortedChunkData = new ChunkData(chunkId, sortedCustomers);

		// Write JSON file
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable Pretty Printing
		objectMapper.writeValue(jsonFile, chunkData);

		System.out.println("Written Chunk: " + chunkId + " to file: " + jsonFile.getAbsolutePath());

		customerRdbmsRepository.saveAll(chunkItems);
		System.out.println("Completed writing  CustomerRdbms data."+chunkItems);

	}
}