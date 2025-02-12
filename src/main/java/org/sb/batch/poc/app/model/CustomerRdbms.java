package org.sb.batch.poc.app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

//import javax.persistence.*;


@Entity
@Table(name = "CUSTOMERS_INFO")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Document
public class CustomerRdbms {
    @Id
    @Column(name = "CUSTOMER_ID")
    private int id;
    @Column(name = "FULL_NAME")
    private String name;
    @Column(name = "EMAIL")
    private String email;
}