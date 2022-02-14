package com.hiep.finalproject.command;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrganizationCommand {
    private Long id;
    private String name;
    private String description;

}
