package com.example.vbapproject.services;

import com.example.vbapproject.exception.RecordNotFoundException;
import com.example.vbapproject.model.Account;
import com.example.vbapproject.model.Customer;
import com.example.vbapproject.repository.AccountRepository;
import com.example.vbapproject.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService{

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;

    @Override
    public Customer create(Customer newCustomer) {
        Customer customer = Customer.builder()
                .name(newCustomer.getName())
                .surName(newCustomer.getSurName())
                .accountNumber(newCustomer.getAccountNumber())
                .phone(newCustomer.getPhone())
                .email(newCustomer.getEmail())
                .build();

        return customerRepository.save(customer);
    }

    @Override
    public Customer getById(long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Customer not found."));
        return customer;
    }

    @Override
    public void update(Customer customer) throws RecordNotFoundException {
        Customer existingCustomer = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new RecordNotFoundException("Customer not found."));

        existingCustomer.setName(customer.getName());
        existingCustomer.setSurName(customer.getSurName());
        existingCustomer.setAccountNumber(customer.getAccountNumber());
        existingCustomer.setPhone(customer.getPhone());
        existingCustomer.setEmail(customer.getEmail());

        Account accountToUpdate = customer.getAccount();
        if (accountToUpdate != null) {
            Account existingAccount = existingCustomer.getAccount();
            if (existingAccount != null) {
                existingAccount.setUserName(accountToUpdate.getUserName());
                existingAccount.setPassword(accountToUpdate.getPassword());
                existingAccount.setRole(accountToUpdate.getRole());
                accountRepository.save(existingAccount);
            } else {
                existingCustomer.setAccount(accountToUpdate);
            }
        }

        customerRepository.save(existingCustomer);
    }

    @Override
    public void delete(long id) throws Exception {
        boolean exists = customerRepository.existsById(id);
        if(exists){
            customerRepository.deleteById(id);
        }else {
            throw new RecordNotFoundException("Customer not found.");
        }
    }

    @Override
    public List<Customer> getAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> getByName(String name) {
        return null;
    }

    @Override
    public boolean hasAccountAccess(long customerId, String username) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if (customer.isPresent()) {
            Account customerAccount = customer.get().getAccount();
            if (customerAccount != null) {
                String associatedUsername = customerAccount.getUserName();
                return associatedUsername.equals(username);
            }
        }
        return false;
    }

}
