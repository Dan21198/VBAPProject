package com.example.vbapproject.services;

import com.example.vbapproject.exception.RecordNotFoundException;
import com.example.vbapproject.model.Car;
import com.example.vbapproject.model.Customer;
import com.example.vbapproject.model.Order;
import com.example.vbapproject.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService{
    private final OrderRepository orderRepository;

    @Override
    public Order create(Order newOrder) {
        Order ret = orderRepository.save(newOrder);
        return ret;
    }

    @Override
    public Order getById(long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Order not found."));
        return order;
    }

    @Override
    public void update(Order order) throws RecordNotFoundException {
        Order existingOrder = orderRepository.findById(order.getId())
                .orElseThrow(() -> new RecordNotFoundException("Order not found."));

        existingOrder.setCustomer(order.getCustomer());
        existingOrder.setDateOfOrder(order.getDateOfOrder());

        Double cost = calculateOrderCost(existingOrder);
        existingOrder.setCost(cost);

        orderRepository.save(existingOrder);
    }

    private Double calculateOrderCost(Order order) {
        Double cost = 0.0;
        for (Car car : order.getCars()) {
            cost += car.getPrice();
        }
        return cost;
    }

    @Override
    public void delete(long id) throws Exception {
        boolean exists = orderRepository.existsById(id);
        if(exists){
            orderRepository.deleteById(id);
        }else {
            throw new RecordNotFoundException("Order not found.");
        }
    }

    @Override
    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getByName(String name) {
        return null;
    }

    @Override
    public Customer findCustomerByOrderId(long orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order != null) {
            return order.getCustomer();
        }
        return null;
    }
}
