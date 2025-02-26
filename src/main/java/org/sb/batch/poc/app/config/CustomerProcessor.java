package org.sb.batch.poc.app.config;

import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<CustomerRdbms,CustomerRdbms> {
    @Override
    public CustomerRdbms process(CustomerRdbms item) throws Exception {
        String data = item.getName().toUpperCase();
        System.out.println("CustomerProcessor>> Processing - " + data+"\'s Data");
        return item;
    }
}