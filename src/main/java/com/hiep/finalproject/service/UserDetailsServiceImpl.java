package com.hiep.finalproject.service;

import com.hiep.finalproject.model.Account;
import com.hiep.finalproject.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);

        if(accountOptional.isEmpty()){
            log.info("Account not found: "+email);
            throw new UsernameNotFoundException("Account " + email + " was not found in the database");
        }
        Account account = accountOptional.get();
        log.info("Found account: "+account.getEmail());
        List<GrantedAuthority> grantList = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole());
        grantList.add(authority);

        UserDetails userDetails = new User(account.getEmail(), account.getEncryptedPassword(), grantList);
        return  userDetails;
    }
}
