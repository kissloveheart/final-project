package com.hiep.finalproject.validator;

import com.hiep.finalproject.form.AccountForm;
import com.hiep.finalproject.model.Account;
import com.hiep.finalproject.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Optional;

@Slf4j
@Component
public class AccountValidator implements Validator {
    @Autowired
    private AccountRepository accountRepository;
    private EmailValidator emailValidator = EmailValidator.getInstance();

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == AccountForm.class ;
    }

    @Override
    public void validate(Object target, Errors errors) {
        AccountForm accountForm = (AccountForm) target;

        // check field of campaignForm.
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email","NotEmpty.accountForm.email");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password","NotEmpty.accountForm.password");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "rePassword","NotEmpty.accountForm.rePassword");

        if (!errors.hasFieldErrors("email")) {
            if(!emailValidator.isValid(accountForm.getEmail())){
                log.info("Email không hợp lệ");
                errors.rejectValue("email", "Pattern.accountForm.email");
            } else if(accountForm.getId() == null){
                Optional<Account> accountOptional = accountRepository.findByEmail(accountForm.getEmail());
                if(accountOptional.isPresent()){
                    log.info("Email đã được sử dụng bởi tài khoản khác");
                    errors.rejectValue("email", "Duplicate.accountForm.email");
                };
            }
        }

        if (!errors.hasErrors()) {
            if (!accountForm.getPassword().equals(accountForm.getRePassword())) {
                errors.rejectValue("rePassword", "Match.accountForm.rePassword");
            }
        }


    }
}
