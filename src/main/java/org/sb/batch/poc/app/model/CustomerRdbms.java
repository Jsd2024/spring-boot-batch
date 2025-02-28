package org.sb.batch.poc.app.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Entity
@Table(name = "CUSTOMER_INFO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRdbms {
    @Id
    @Column(name = "CUSTOMER_ID")
    private int id;
    @Column(name = "FULL_NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
}