package com.gmasella.banking.service.impl;

import com.gmasella.banking.dto.AccountDTO;
import com.gmasella.banking.dto.TransactionDTO;
import com.gmasella.banking.dto.TransferFundsDTO;
import com.gmasella.banking.entity.Account;
import com.gmasella.banking.entity.Transaction;
import com.gmasella.banking.exception.AccountException;
import com.gmasella.banking.mapper.AccountMapper;
import com.gmasella.banking.repository.AccountRepository;
import com.gmasella.banking.repository.TransactionRepository;
import com.gmasella.banking.service.AccountService;
import com.gmasella.banking.service.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;

    private TransactionRepository transactionRepository;

    private static final String TRANSACTION_TYPE_TRANSFER = "TRANSFER";





    public AccountServiceImpl(AccountRepository accountRepository,
                              TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
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
                .orElseThrow(()-> new AccountException("Account does not exist"));

        return AccountMapper.mapToAccountDTO(account);
    }

    @Override
    public AccountDTO deposit(Long id, double amount) {

        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new AccountException("Account does not exist"));
        double total = account.getBalance() + amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);
        saveTransaction(id, amount, TransactionType.DEPOSIT);

        return AccountMapper.mapToAccountDTO(savedAccount);
    }

    @Override
    public AccountDTO withdraw(Long id, double amount) throws Exception {
        Account account = accountRepository
                .findById(id)
                .orElseThrow(()-> new AccountException("Account does not exist"));

        if(account.getBalance() < amount){
            throw new RuntimeException("Insufficient Funds!");
        }

        double total = account.getBalance() - amount;
        account.setBalance(total);
        Account savedAccount = accountRepository.save(account);

        saveTransaction(id, amount, TransactionType.WITHDRAW);

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
                .orElseThrow(()-> new AccountException("Account does not exist"));

        accountRepository.deleteById(id);
    }

    @Override
    public void transferFunds(TransferFundsDTO transferFundsDTO) {

        // get account that is sending the amount
        Account fromAccount = accountRepository
                .findById(transferFundsDTO.fromAccountId())
                .orElseThrow(()-> new AccountException("Account does not exist"));

        // get account that is receiving the amount
        Account toAccount = accountRepository
                .findById(transferFundsDTO.toAccountId())
                .orElseThrow(()-> new AccountException("Account does not exist"));

        // transfer the funds
        if(fromAccount.getBalance() < transferFundsDTO.amount()){
            throw new RuntimeException("Insufficient Funds!");
        }
        fromAccount.setBalance(fromAccount.getBalance() - transferFundsDTO.amount());
        toAccount.setBalance(toAccount.getBalance() + transferFundsDTO.amount());

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction transaction = new Transaction();
        transaction.setAccountId(transferFundsDTO.fromAccountId());
        transaction.setAmount(transferFundsDTO.amount());
        transaction.setTransactionType(TransactionType.TRANSFER);
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> getAccountTransactions(Long accountId) {

        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTimestampDesc(accountId);

        return transactions.stream().map(this::convertEntityToDTO).collect(Collectors.toList());
    }

    private TransactionDTO convertEntityToDTO(Transaction transaction){

        return new TransactionDTO(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getAmount(),
                transaction.getTransactionType(),
                transaction.getTimestamp()
        );
    }

    private void saveTransaction(Long id, double amount, TransactionType transactionType){

        Transaction transaction = new Transaction();
        transaction.setAccountId(id);
        transaction.setAmount(amount);
        transaction.setTransactionType(TransactionType.valueOf(transactionType.name()));
        transaction.setTimestamp(LocalDateTime.now());

        transactionRepository.save(transaction);
    }




}
