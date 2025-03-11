package org.sb.batch.poc.app.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRdbms {

    private int id;
    private String name;
//    @Column(name = "EMAIL")
//    private String email;
   private List<String> emails;
}