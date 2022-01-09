package com.hiep.finalproject.service;

import com.hiep.finalproject.dao.AccountDAO;
import com.hiep.finalproject.entity.Account;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final static Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    private AccountDAO accountDAO;

    public UserDetailsServiceImpl(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Account account = accountDAO.findAccount(email);

        if(account == null){
            log.info("Account not found: "+email);
            throw new UsernameNotFoundException("Account " + email + " was not found in the database");
        }

        log.info("Found account: "+account.getEmail());
        List<GrantedAuthority> grantList = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(account.getRole());
        grantList.add(authority);

        UserDetails userDetails = new User(account.getEmail(), account.getPassword(), grantList);
        return  userDetails;
    }
}
