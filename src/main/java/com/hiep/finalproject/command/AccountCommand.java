package com.hiep.finalproject.command;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
public class AccountCommand {
    private Long id;
    private String email;
    private String password;
    private String phone;
    private String role;
    private boolean enable;
    private Date createdDate;
    private Double balance;
    private String address;
    private List<DonationCommand> donationCommandList = new ArrayList<>();
}
