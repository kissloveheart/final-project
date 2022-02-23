package com.hiep.finalproject.service;

import com.hiep.finalproject.command.OrganizationCommand;
import com.hiep.finalproject.converter.OrganizationToOrganizationCommand;
import com.hiep.finalproject.dto.OrganizationDto;
import com.hiep.finalproject.form.OrganizationForm;
import com.hiep.finalproject.model.Organization;
import com.hiep.finalproject.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrganizationServiceImpl implements OrganizationService {
    @Autowired
    OrganizationRepository organizationRepository;
    @Autowired
    OrganizationToOrganizationCommand organizationToOrganizationCommand;

    @Override
    public List<OrganizationCommand> getAllOrganization() {
        List<OrganizationCommand> list = new ArrayList<>();
        organizationRepository.findAll().iterator()
                .forEachRemaining(organization -> list.add(organizationToOrganizationCommand.convert(organization)));
        return list;
    }

    @Override
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id).orElseGet(null);
    }

    @Override
    public List<OrganizationDto> getAllOrganizationDto() {
        List<OrganizationDto> list = new ArrayList<>();
        organizationRepository.findAllOrganization()
                .forEach(iOrganizationDto -> list.add(new OrganizationDto(iOrganizationDto)));
        return list;
    }

    @Override
    @Transactional
    public Boolean deleteOrganizationIdList(String listId) {
        String[] idDelete = listId.split("-");
        List<Long> organizationDeleteIdList = new ArrayList<>();
        if (idDelete.length > 0) {
            for (String id : idDelete) {
                try {
                    organizationDeleteIdList.add(Long.parseLong(id));
                } catch (NumberFormatException e) {
                    log.warn("This is not a number");
                    return false;
                }
            }
        }
        organizationRepository.deleteAllById(organizationDeleteIdList);
        return true;
    }

    @Override
    public OrganizationCommand getOrganizationCommandById(Long id) {
        Organization organization = getOrganizationById(id);
        return organizationToOrganizationCommand.convert(organization);
    }

    @Override
    public void saveOrganization(OrganizationForm organizationForm) throws  IOException {
        Organization organization;
        if(organizationForm.getId()!= null){
            organization = organizationRepository.findById(organizationForm.getId()).get();
        } else {
            organization = new Organization();
        }
        organization.setName(organizationForm.getName());
        organization.setDescription(organizationForm.getDescription());

        if (organizationForm.getFileData() != null) {
            byte[] image = organizationForm.getFileData().getBytes();
            if (image.length > 0) {
                organization.setImage(image);
            }
        }
        organizationRepository.save(organization);
    }
}
