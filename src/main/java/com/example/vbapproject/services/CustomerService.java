package com.example.vbapproject.services;

import com.example.vbapproject.model.Customer;

import java.util.List;

public interface CustomerService {
    Customer create(Customer newCustomer);
    Customer getById(long id);
    void update(Customer customer) throws Exception;
    void delete(long id) throws Exception;
    List<Customer> getAll();
    List<Customer> getByName(String name);

    boolean hasAccountAccess(long customerId, String username);
}
