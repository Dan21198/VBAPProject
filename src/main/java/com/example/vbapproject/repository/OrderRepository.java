package com.example.vbapproject.repository;

import com.example.vbapproject.model.Customer;
import com.example.vbapproject.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT c FROM Customer c JOIN c.orders o WHERE o.id = :orderId")
    public Customer findCustomerByOrderId(@Param("orderId") Long orderId);

}
