package com.hiep.finalproject.validator;

import com.hiep.finalproject.form.OrganizationForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.io.IOException;
@Component
public class OrganizationValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == OrganizationForm.class;
    }


    @Override
    public void validate(Object target, Errors errors) {
        OrganizationForm organizationForm = (OrganizationForm) target;

        // check field of organizationForm.
        ValidationUtils.rejectIfEmpty(errors, "name","NotEmpty.organizationForm.name");
        ValidationUtils.rejectIfEmpty(errors, "description","NotEmpty.organizationForm.description");

        if (organizationForm.getId() == null) {
            try {
                if (organizationForm.getFileData().getBytes().length == 0) {
                    errors.rejectValue("fileData", "NotNull.organizationForm.fileData");
                }
            } catch (IOException e) {
                e.printStackTrace();
                errors.rejectValue("fileData", "NotNull.organizationForm.fileData");
            }
        }
    }
}
