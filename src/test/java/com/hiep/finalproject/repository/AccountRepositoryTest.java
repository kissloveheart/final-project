package com.hiep.finalproject.repository;

import com.hiep.finalproject.model.Account;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;
@DataJpaTest
@Slf4j
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;
    @Test
    void findAllByRoleLikeAndEnableEquals() {
        List<Account> accountList = new ArrayList<>();
        accountRepository.findAllByRoleLikeAndEnableEquals("",true)
                .forEach(accountList::add);
        accountList.forEach(account -> log.info(account.toString()));
    }
}