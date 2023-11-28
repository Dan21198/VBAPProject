package com.example.vbapproject.repository;

import com.example.vbapproject.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findByName(String name);
    Optional<Customer> findByAccountId(Long accountId);

}
