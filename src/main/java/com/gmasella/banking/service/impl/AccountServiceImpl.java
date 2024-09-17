package com.gmasella.banking.service.impl;

import com.gmasella.banking.dto.AccountDTO;
import com.gmasella.banking.entity.Account;
import com.gmasella.banking.mapper.AccountMapper;
import com.gmasella.banking.repository.AccountRepository;
import com.gmasella.banking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;


    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDTO createAccount(AccountDTO accountDTO) {

        Account account = AccountMapper.mapToAccount(accountDTO);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public AccountDTO getAccountByID(Long id) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Account does not exist"));

        return AccountMapper.mapToAccountDTO(account);
    }

    @Override
    public AccountDTO deposit(Long id, double amount) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Account does not exist"));
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public AccountDTO withdraw(Long id, double amount) throws Exception {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Account does not exist"));

        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient Funds!");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {

        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::mapToAccountDTO).toList();
    }

    @Override
    public void deleteAccount(Long id) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException("Account does not exist"));

        accountRepository.deleteById(id);
    }


}
