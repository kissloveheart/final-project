package com.hiep.finalproject.form;

import com.hiep.finalproject.entity.Account;
import org.springframework.web.multipart.MultipartFile;

public class AccountForm {

    private Long Id;
    private String email;
    private String password;
    private String confirmPassword;
    private String phone;
    private String role;
    private boolean enable;
    private MultipartFile fileData;

    public AccountForm(Account account) {
        this.Id = account.getId();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.role = account.getRole();
        this.enable = account.isEnable();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "AccountForm{" +
                "Id=" + Id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", role='" + role + '\'' +
                ", enable=" + enable +
                ", fileData=" + fileData +
                '}';
    }
}
