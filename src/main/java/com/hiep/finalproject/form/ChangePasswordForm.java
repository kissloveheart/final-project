package com.hiep.finalproject.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class ChangePasswordForm {
    @NotBlank(message = "Hãy nhập mật khẩu")
    private String oldPass;
    @NotBlank(message = "Hãy nhập mật khẩu mới")
    private String newPass;
    @NotBlank(message = "Hãy nhập lại mật khẩu mới")
    private String newRePass;
}
