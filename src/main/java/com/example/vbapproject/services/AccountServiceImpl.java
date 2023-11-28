package com.example.vbapproject.services;

import com.example.vbapproject.exception.RecordNotFoundException;
import com.example.vbapproject.model.Account;
import com.example.vbapproject.repository.AccountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService{
    private final AccountRepository accountRepository;

    @Override
    public Account create(Account newUser) {
        Account ret = accountRepository.save(newUser);
        return ret;
    }

    @Override
    public Account getById(long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Account not found."));
    }

    @Override
    public void update(Account account) throws RecordNotFoundException {
        Account existingAccount = accountRepository.findById(account.getId())
                .orElseThrow(() -> new RecordNotFoundException("Account not found."));

        existingAccount.setUserName(account.getUserName());
        existingAccount.setPassword(account.getPassword());
        existingAccount.setRole(account.getRole());

        accountRepository.save(existingAccount);
    }

    @Override
    public void delete(long id) throws Exception {
        boolean exists = accountRepository.existsById(id);
        if(exists){
            accountRepository.deleteById(id);
        }else {
            throw new RecordNotFoundException("Account not found.");
        }
    }

    @Override
    public List<Account> getAll() {
        return accountRepository.findAll();
    }

    @Override
    public List<Account> getByName(String name) {
        return null;
    }

}
