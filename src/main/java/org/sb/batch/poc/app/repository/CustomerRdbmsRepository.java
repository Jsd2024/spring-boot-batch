package org.sb.batch.poc.app.repository;


import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRdbmsRepository extends
        CrudRepository<CustomerRdbms, Integer> {}

