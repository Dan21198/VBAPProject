package com.example.vbapproject.services;

import com.example.vbapproject.model.Account;

import java.util.List;

public interface AccountService {
    Account create(Account newUser);
    Account getById(long id);
    void update(Account account) throws Exception;
    void delete(long id) throws Exception;
    List<Account> getAll();
    List<Account> getByName(String name);
}
