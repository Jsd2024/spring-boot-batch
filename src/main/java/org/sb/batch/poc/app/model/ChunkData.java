package org.sb.batch.poc.app.model;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ChunkData {
    private int chunkId;
    private List<CustomerRdbms> customers;
}
