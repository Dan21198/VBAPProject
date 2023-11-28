package com.example.vbapproject.controller;

import com.example.vbapproject.model.Customer;
import com.example.vbapproject.model.Order;
import com.example.vbapproject.services.OrderService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class OrdersController {
    private final OrderService orderService;

    @PostMapping("/orders")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Order create(@Valid @RequestBody Order newOrder) {
        return orderService.create(newOrder);
    }

    @GetMapping("/orders")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') " +
            "and @customerServiceImpl.hasAccountAccess(#id, principal.username))")
    public Order get(@PathVariable("id") long id) {
        return orderService.getById(id);
    }

    @PutMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') " +
            "and @customerServiceImpl.hasAccountAccess(#id, principal.username))")
    public void update(@PathVariable("id") long id, @Valid @RequestBody Order order) throws Exception {
        order.setId(id);
        orderService.update(order);
    }

    @DeleteMapping("/orders/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') " +
            "and @customerServiceImpl.hasAccountAccess(#id, principal.username))")
    public void delete(@PathVariable("id") long id) throws Exception {
        orderService.delete(id);
    }

    @GetMapping("/orders/{orderId}/customer")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Customer> findCustomerByOrderId(@PathVariable("orderId") long orderId) {
        Customer customer = orderService.findCustomerByOrderId(orderId);
        if (customer == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(customer);
        }
    }
}
