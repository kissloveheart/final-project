package com.hiep.finalproject.converter;

import com.hiep.finalproject.command.DonationCommand;
import com.hiep.finalproject.model.Donation;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DonationToDonationCommand implements Converter<Donation, DonationCommand> {
    @Override
    public DonationCommand convert(Donation source) {
        DonationCommand command = new DonationCommand();
        command.setId(source.getId());
        command.setAmount(source.getAmount());
        command.setDate(source.getDate());
        if (source.getCampaign() != null) {
            command.setCampaignId(source.getCampaign().getId());
        }
        return command;
    }
}
