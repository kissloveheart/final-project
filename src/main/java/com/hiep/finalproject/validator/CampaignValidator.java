package com.hiep.finalproject.validator;

import com.hiep.finalproject.form.CampaignForm;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class CampaignValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == CampaignForm.class;
    }

    @Override
    public void validate(Object target, Errors errors) {
        CampaignForm campaignForm = (CampaignForm) target;

        // check field of campaignForm.
        ValidationUtils.rejectIfEmpty(errors, "name","NotEmpty.campaignForm.name");
        ValidationUtils.rejectIfEmpty(errors, "description","NotEmpty.campaignForm.description");
        ValidationUtils.rejectIfEmpty(errors, "targetBudget","NotEmpty.campaignForm.targetBudget");

        if(campaignForm.getOrganization() == null){
            errors.rejectValue("organization","NotNull.campaignForm.organization");
        }
        if(!errors.hasFieldErrors("targetBudget")){
            NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
            double targetBudget;
            try {
                targetBudget = numberFormat.parse(campaignForm.getTargetBudget()).doubleValue();
                if(targetBudget <1000000){
                    errors.rejectValue("targetBudget","NotEmpty.campaignForm.targetBudget") ;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (campaignForm.getId() == null) {
            try {
                if (campaignForm.getFileData().getBytes().length == 0) {
                    errors.rejectValue("fileData", "NotNull.campaignForm.fileData");
                }
            } catch (IOException e) {
                e.printStackTrace();
                errors.rejectValue("fileData", "NotNull.campaignForm.fileData");
            }
        }
    }


}
