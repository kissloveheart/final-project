package com.hiep.finalproject.service;

import com.hiep.finalproject.command.CampaignCommand;
import com.hiep.finalproject.command.OrganizationCommand;
import com.hiep.finalproject.dto.OrganizationDto;
import com.hiep.finalproject.form.CampaignForm;
import com.hiep.finalproject.form.OrganizationForm;
import com.hiep.finalproject.model.Organization;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface OrganizationService {
    List<OrganizationCommand> getAllOrganization();
    Organization getOrganizationById(Long id);
    List<OrganizationDto> getAllOrganizationDto();
    Boolean deleteOrganizationIdList(String id);
    OrganizationCommand getOrganizationCommandById(Long id);
    void saveOrganization(OrganizationForm organizationForm) throws ParseException, IOException;
}
