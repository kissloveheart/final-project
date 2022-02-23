package com.hiep.finalproject.service;

import com.hiep.finalproject.command.AccountCommand;
import com.hiep.finalproject.converter.AccountToAccountCommand;
import com.hiep.finalproject.form.AccountForm;
import com.hiep.finalproject.form.ChangePasswordForm;
import com.hiep.finalproject.model.Account;
import com.hiep.finalproject.model.VerificationToken;
import com.hiep.finalproject.repository.AccountRepository;
import com.hiep.finalproject.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
public class AccountServiceImpl implements AccountService{
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    AccountToAccountCommand accountToAccountCommand;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TokenRepository tokenRepository;
    @Autowired
    MailService mailService;
    @Override
    public AccountCommand saveAccount(AccountForm accountForm) throws IOException {
        Account account;
        if(accountForm.getId()!= null){
            account = accountRepository.findById(accountForm.getId()).get();
        } else {
            account = new Account();
        }
        if (accountForm.getEmail() != null){
            account.setEmail(accountForm.getEmail());
        }
        if( accountForm.getPassword() != null){
            account.setEncryptedPassword(passwordEncoder.encode(accountForm.getPassword()));
        }
        if( accountForm.getPhone() != null){
            account.setPhone(accountForm.getPhone());
        }
        if(accountForm.getRole() != null){
            account.setRole(accountForm.getRole());
        }
        if(accountForm.getEnable() != null){
            account.setEnable(accountForm.getEnable());
        }
        if(accountForm.getCreatedDate() != null){
            account.setCreatedDate(accountForm.getCreatedDate());
        }
        if(accountForm.getAddress() != null){
            account.setAddress(accountForm.getAddress());
        }

        if (accountForm.getFileData() != null) {
            byte[] image = accountForm.getFileData().getBytes();
            if (image.length > 0) {
                account.setImage(image);
            }
        }
        Account saveAccount = accountRepository.save(account);
        return accountToAccountCommand.convert(saveAccount);
    }


    @Override
    public VerificationToken createVerificationToken(Account account, String token) {
        VerificationToken myToken = new VerificationToken(token, account);
        if(account.getVerificationToken() != null){
            myToken.setId(account.getVerificationToken().getId());
        }
        return tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String verificationToken) {
        return tokenRepository.findByToken(verificationToken);
    }

    @Override
    public void setEnableAccount(VerificationToken verificationToken) {
        Account account = verificationToken.getAccount();
        account.setEnable(true);
        accountRepository.save(account);
    }
    @Async
    @Override
    public void sendForgetPasswordEmail(Account account, String url) {
        String token = UUID.randomUUID().toString();
        createVerificationToken(account,token);
        String name = account.getEmail().substring(0,account.getEmail().indexOf("@"));
        String confirmationUrl = url + "/confirm/" + token;
        try {
            mailService.sendForgetMail(name,confirmationUrl,account.getEmail());
        } catch (MessagingException e) {
            log.info("Email sent unsuccessfully");
        }
    }

    @Override
    public AccountCommand getAccountCommand() {
        return accountToAccountCommand.convert(getCurrentAccount());
    }

    @Override
    public byte[] getAccountAvatar(Long id) {
        Optional<Account> account = accountRepository.getAccountById(id);
        if(account.isEmpty()){
            return new byte[0];
        }
        return account.get().getImage();
    }

    @Override
    public Boolean checkPassword(ChangePasswordForm form) {
        if(form.getNewPass() == null || form.getNewRePass() == null || form.getOldPass() == null){
            return false;
        }
        if(!form.getNewPass().equals(form.getNewRePass())){
            return false;
        }
        return passwordEncoder.matches(form.getOldPass(),getCurrentAccount().getEncryptedPassword());
    }

    @Override
    public void saveNewPassword(String password) {
        Account account = getCurrentAccount();
        account.setEncryptedPassword(passwordEncoder.encode(password));
        accountRepository.save(account);
    }

    @Override
    public Account getCurrentAccount(){
        // Get user order
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if(principal instanceof UserDetails) {
            email = ((UserDetails) principal).getUsername();
            Optional<Account> accountOptional = accountRepository.findByEmail(email);
            if (accountOptional.isPresent()) {
                return accountOptional.get();
            }
        }
        log.info("User not login");
        return  null;
    }

    @Override
    public List<AccountCommand> getAllAccountList(String role, Boolean enabled) {
        List<AccountCommand> accountList = new ArrayList<>();
        if(role != null && enabled != null){
            accountRepository.findAllByRoleLikeAndEnableEquals(role,enabled)
                    .iterator().forEachRemaining(account -> accountList.add(accountToAccountCommand.convert(account)));
            return accountList;
        }
        if(role != null){
            accountRepository.findAllByRoleLike(role)
                    .iterator().forEachRemaining(account -> accountList.add(accountToAccountCommand.convert(account)));
            return accountList;
        }
        if(enabled != null){
            accountRepository.findAllByEnableEquals(enabled)
                    .iterator().forEachRemaining(account -> accountList.add(accountToAccountCommand.convert(account)));
            return accountList;
        }
        accountRepository.findAll()
                .iterator().forEachRemaining(account -> accountList.add(accountToAccountCommand.convert(account)));
        return accountList;
    }
}
