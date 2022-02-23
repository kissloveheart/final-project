package com.hiep.finalproject.repository;

import com.hiep.finalproject.model.Account;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account,Long> {
    Optional<Account> findByEmail(String email);
    @Query("select a from Account a where a.id =?1")
    Optional<Account> getAccountById(Long id);
    Iterable<Account> findAllByRoleLikeAndEnableEquals(String role,boolean enabled);
    Iterable<Account> findAllByRoleLike(String role);
    Iterable<Account> findAllByEnableEquals(boolean enabled);

}
