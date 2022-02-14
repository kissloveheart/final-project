package com.hiep.finalproject.converter;

import com.hiep.finalproject.command.CampaignCommand;
import com.hiep.finalproject.model.Campaign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CampaignToCampaignCommand implements Converter<Campaign, CampaignCommand> {
    @Autowired
    private OrganizationToOrganizationCommand organizationToOrganizationCommand;
    @Autowired
    private DonationToDonationCommand donationToDonationCommand;
    @Override
    public CampaignCommand convert(Campaign source) {
        CampaignCommand command = new CampaignCommand();
        command.setId(source.getId());
        command.setName(source.getName());
        command.setDescription(source.getDescription());
        command.setOrganizationCommand(organizationToOrganizationCommand.convert(source.getOrganization()));
        command.setStartDate(source.getStartDate());
        command.setEndDate(source.getEndDate());
        command.setStatus(source.isStatus());
        command.setTargetBudget(source.getTargetBudget());

        if(source.getDonationList() != null && source.getDonationList().size() > 0){
            source.getDonationList().forEach(donation -> command.getDonationCommandList()
                    .add(donationToDonationCommand.convert(donation)));
        }

        return command;
    }
}
