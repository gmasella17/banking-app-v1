package com.gmasella.banking.controller;

import com.gmasella.banking.dto.AccountDTO;
import com.gmasella.banking.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    // Add Account REST API
    @PostMapping
    public ResponseEntity<AccountDTO> addAccount(@RequestBody AccountDTO accountDTO){
        return new ResponseEntity<>(accountService.createAccount(accountDTO), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id){
        AccountDTO accountDTO = accountService.getAccountByID(id);
        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/{id}/deposit")
    public ResponseEntity<AccountDTO> deposit(@PathVariable Long id,
                                              @RequestBody Map<String, Double> request){
        AccountDTO accountDTO = accountService.deposit(id, request.get("amount"));
        return ResponseEntity.ok(accountDTO);
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<AccountDTO> withdraw(@PathVariable Long id,
                                               @RequestBody Map<String, Double> request) throws Exception {
        AccountDTO accountDTO = accountService.withdraw(id, request.get("amount"));
        return ResponseEntity.ok(accountDTO);

    }

    @GetMapping
    public ResponseEntity<List<AccountDTO>> getAllAccounts(){
        List<AccountDTO> accountDTOList = accountService.getAllAccounts();
        return ResponseEntity.ok(accountDTOList);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteAccount(@PathVariable Long id){

        accountService.deleteAccount(id);
        return ResponseEntity.ok("Successfully deleted Account.");

    }

}
