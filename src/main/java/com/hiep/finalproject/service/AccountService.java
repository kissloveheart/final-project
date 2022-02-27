package com.hiep.finalproject.service;

import com.hiep.finalproject.command.AccountCommand;
import com.hiep.finalproject.form.AccountForm;
import com.hiep.finalproject.form.ChangePasswordForm;
import com.hiep.finalproject.model.Account;
import com.hiep.finalproject.model.VerificationToken;

import java.io.IOException;
import java.util.List;

public interface AccountService {
    AccountCommand saveAccount(AccountForm accountForm) throws IOException;
    VerificationToken createVerificationToken(Account account, String token);
    VerificationToken getVerificationToken(String verificationToken);
    void setEnableAccount(VerificationToken verificationToken);
    void sendForgetPasswordEmail(Account account,String url);
    AccountCommand getAccountCommand();
    byte[] getAccountAvatar(Long id);
    Boolean checkPassword(ChangePasswordForm form);
    void saveNewPassword(String password);
    Account getCurrentAccount();
    List<AccountCommand> getAllAccountList(String role, Boolean enabled);
    Boolean deleteUserIdList(String listId);
    void deleteUserSession(String email);
}
