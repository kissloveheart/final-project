package com.hiep.finalproject.listener;


import com.hiep.finalproject.command.AccountCommand;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;


@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {
    private String appUrl;
    private Locale locale;
    private AccountCommand accountCommand;
    private String password;

    public OnRegistrationCompleteEvent(
            AccountCommand accountCommand, Locale locale, String appUrl,String password) {
        super(accountCommand);

        this.accountCommand = accountCommand;
        this.locale = locale;
        this.appUrl = appUrl;
        this.password = password;
    }

}
