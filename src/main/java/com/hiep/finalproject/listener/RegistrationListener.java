package com.hiep.finalproject.listener;

import com.hiep.finalproject.model.Account;
import com.hiep.finalproject.repository.AccountRepository;
import com.hiep.finalproject.service.AccountService;
import com.hiep.finalproject.service.MailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.UUID;
@Slf4j
@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent> {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private MailService mailService;
    @Autowired
    private AccountService accountService;

    @Override
    @Async
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {
        Account account = accountRepository.findByEmail(event.getAccountCommand().getEmail()).get();
        String token = UUID.randomUUID().toString();
        account.setVerificationToken(accountService.createVerificationToken(account,token));
        String name = account.getEmail().substring(0,account.getEmail().indexOf("@"));
        String confirmationUrl = event.getAppUrl() + "/confirm/" + token;
        try {
            if(event.getPassword() == null){
                mailService.sendRegisMail(name,confirmationUrl,account.getEmail(),event.getLocale());
            }
            mailService.sendNewUserMail(name,confirmationUrl, account.getEmail(), event.getPassword());
        } catch (MessagingException e) {
            log.info("Email sent unsuccessfully");
        }
    }
}
