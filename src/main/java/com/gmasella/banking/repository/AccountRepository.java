package com.gmasella.banking.repository;

import com.gmasella.banking.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {



}
