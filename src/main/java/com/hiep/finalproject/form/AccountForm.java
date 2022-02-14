package com.hiep.finalproject.form;

import com.hiep.finalproject.model.Account;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class AccountForm {

    private Long id;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private String role;
    private boolean enable;
    private MultipartFile fileData;

    public AccountForm(Account account) {
        this.id = account.getId();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.role = account.getRole();
        this.enable = account.isEnable();
    }

}
