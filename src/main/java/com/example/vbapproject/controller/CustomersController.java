package com.example.vbapproject.controller;

import com.example.vbapproject.model.Customer;
import com.example.vbapproject.services.CustomerService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class CustomersController {
    private final CustomerService customerService;

    @PostMapping("/customers")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Customer create(@Valid @RequestBody Customer newCustomer) {
        return customerService.create(newCustomer);
    }

    @GetMapping("/customers")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Customer> getAll() {
        return customerService.getAll();
    }

    @GetMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER')" +
            " and @customerServiceImpl.hasAccountAccess(#id, principal.username))")
    public Customer get(@PathVariable("id") long id) {
        return customerService.getById(id);
    }

    @GetMapping("/customers/search/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Customer> searchByName(@PathVariable("name") String name) {
        return customerService.getByName(name);
    }

    @PutMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER')" +
            " and @customerServiceImpl.hasAccountAccess(#id, principal.username))")
    public void update(@PathVariable("id") long id, @Valid @RequestBody Customer customer) throws Exception {
        if (customer.getId() != id) {
            throw new Exception("ID in path does not match ID in customer object");
        }
        customerService.update(customer);
    }

    @DeleteMapping("/customers/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') " +
            "and @customerServiceImpl.hasAccountAccess(#id, principal.username))")
    public void delete(@PathVariable("id") long id) throws Exception {
        customerService.delete(id);
    }
}
