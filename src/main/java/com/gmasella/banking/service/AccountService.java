package com.gmasella.banking.service;

import com.gmasella.banking.dto.AccountDTO;
import com.gmasella.banking.dto.TransferFundsDTO;

import java.util.List;

public interface AccountService {

    AccountDTO createAccount(AccountDTO accountDTO);

    AccountDTO getAccountByID(Long id);

    AccountDTO deposit(Long id, double amount);

    AccountDTO withdraw(Long id, double amount) throws Exception;

    List<AccountDTO> getAllAccounts();

    void deleteAccount(Long id);

    void transferFunds(TransferFundsDTO transferFundsDTO);
}
