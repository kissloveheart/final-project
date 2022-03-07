package com.hiep.finalproject.dto;

import java.util.Date;

public interface IDonationDto {
    Long getId();
    Date getDate();
    Double getAmount();
    String getCampaign();
    String getOrganization();
    String getAccount();
}
