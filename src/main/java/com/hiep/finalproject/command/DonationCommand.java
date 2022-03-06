package com.hiep.finalproject.command;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class DonationCommand {
    private Long id;
    private Long campaignId;
    private Date date;
    private Double amount;
    private String email;
}
