package com.hiep.finalproject.converter;

import com.hiep.finalproject.command.AccountCommand;
import com.hiep.finalproject.model.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AccountToAccountCommand implements Converter<Account, AccountCommand> {
    @Autowired
    private DonationToDonationCommand donationToDonationCommand;

    @Override
    public AccountCommand convert(Account source) {
        AccountCommand command = new AccountCommand();
        command.setId(source.getId());
        command.setEmail(source.getEmail());
        command.setPhone(source.getPhone());
        command.setRole(source.getRole());
        command.setCreatedDate(source.getCreatedDate());
        command.setEnable(source.isEnable());
        command.setBalance(source.getBalance());
        command.setAddress(source.getAddress());

        if ((source.getDonationList() != null && !source.getDonationList().isEmpty())) {
            source.getDonationList().forEach(donation ->
                    command.getDonationCommandList().add(donationToDonationCommand.convert(donation)));
        }

        return command;

    }
}
