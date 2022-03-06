package com.hiep.finalproject.validator;

import com.hiep.finalproject.command.DonationCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
@Component
public class DonationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == DonationCommand.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        // check field of donation command
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "campaignId","NotEmpty.donationCommand.campaignId");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "amount","NotEmpty.donationCommand.amount");
    }
}
