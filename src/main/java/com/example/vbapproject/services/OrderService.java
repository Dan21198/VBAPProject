package com.example.vbapproject.services;

import com.example.vbapproject.model.Customer;
import com.example.vbapproject.model.Order;

import java.util.List;

public interface OrderService {
    Order create(Order newOrder);
    Order getById(long id);
    void update(Order order) throws Exception;
    void delete(long id) throws Exception;
    List<Order> getAll();
    List<Order> getByName(String name);
    Customer findCustomerByOrderId(long orderId);
}
