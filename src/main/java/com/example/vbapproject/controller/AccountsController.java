package com.example.vbapproject.controller;

import com.example.vbapproject.model.Account;
import com.example.vbapproject.services.AccountService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin("http://localhost:3000")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1")
public class AccountsController {

    private final AccountService accountService;

    @PostMapping("/accounts")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public Account create(@Valid @RequestBody Account newUser) {
        return accountService.create(newUser);
    }

    @GetMapping("/accounts")
    @PreAuthorize("hasRole('ADMIN')")
    public List<Account> getAll() {
        return accountService.getAll();
    }

    @GetMapping("/getAccountById/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public Account get(@PathVariable("id") long id) {
        return accountService.getById(id);
    }

    @PutMapping("/accounts/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public void update(@PathVariable("id") long id, @Valid @RequestBody Account user) throws Exception {
        user.setId(id);
        accountService.update(user);
    }

    @DeleteMapping("/account/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #id == principal.id)")
    public void delete(@PathVariable("id") long id) throws Exception {
        accountService.delete(id);
    }
}
