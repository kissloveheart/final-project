package com.hiep.finalproject.command;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class CampaignCommand {
    private Long id;
    private String name;
    private String description;
    private OrganizationCommand organizationCommand;
    private Date startDate;
    private Date endDate;
    private Double targetBudget;
    private Boolean status;
    private List<DonationCommand> donationCommandList = new ArrayList<>();

    public Double getTotalAmount(){
        if (getDonationCommandList() != null){
            return donationCommandList.stream().map(DonationCommand::getAmount)
                    .reduce(0D, Double::sum);
        }
        return 0D;
    }
}
