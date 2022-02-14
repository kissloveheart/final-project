package com.hiep.finalproject.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrganizationDto{
    Long id;
    String name;
    String description;
    Integer countCampaign;
    Double totalMoneyDonation;

    public OrganizationDto(IOrganizationDto iOrganizationDto) {
        id = iOrganizationDto.getId();
        name = iOrganizationDto.getName();
        description = iOrganizationDto.getDescription();
        countCampaign = iOrganizationDto.getCountCampaign();
        totalMoneyDonation = iOrganizationDto.getTotalMoneyDonation();
    }
}
