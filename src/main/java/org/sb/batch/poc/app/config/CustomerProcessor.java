package org.sb.batch.poc.app.config;

//import org.sb.batch.poc.app.model.Customer;
import org.sb.batch.poc.app.model.CustomerRdbms;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<CustomerRdbms,CustomerRdbms> {

    //    @Override
//    public Customer process(Customer customer) throws Exception {
//        if(customer.getCountry().equals("United States")) {
//            return customer;
//        }else{
//            return null;
//        }
//    }
    @Override
    public CustomerRdbms process(CustomerRdbms item) throws Exception {
        return item;
    }
}
//package org.sb.batch.poc.app.config;
//
////import org.sb.batch.poc.app.model.CustomerNoSql;
//import org.sb.batch.poc.app.model.CustomerRdbms;
//import org.springframework.batch.item.ItemProcessor;
//
//public class CustomerProcessor implements ItemProcessor<CustomerRdbms, CustomerRdbms> {
//
////    @Override
////    public Customer process(Customer customer) throws Exception {
////        if(customer.getCountry().equals("United States")) {
////            return customer;
////        }else{
////            return null;
////        }
////    }
////    @Override
////    public CustomerNoSql process(CustomerNoSql item) throws Exception {
////        return item;
////    }
//
//    @Override
//    public CustomerRdbms process(CustomerRdbms item) throws Exception {
//        return null;
//    }
//}