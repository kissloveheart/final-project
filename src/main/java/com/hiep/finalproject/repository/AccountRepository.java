package com.hiep.finalproject.repository;

import com.hiep.finalproject.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Long> {
    Optional<Account> findByEmail(String email);
}
