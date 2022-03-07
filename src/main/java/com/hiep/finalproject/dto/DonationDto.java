package com.hiep.finalproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DonationDto {
    Long id;
    Date date;
    Double amount;
    String campaign;
    String organization;
    String account;

    public DonationDto(IDonationDto iDonationDto) {
        this.id = iDonationDto.getId();
        this.date = iDonationDto.getDate();
        this.amount = iDonationDto.getAmount();
        this.campaign = iDonationDto.getCampaign();
        this.organization = iDonationDto.getOrganization();
        this.account = iDonationDto.getAccount();
    }
}
