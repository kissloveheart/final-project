package com.hiep.finalproject.converter;

import com.hiep.finalproject.command.OrganizationCommand;
import com.hiep.finalproject.model.Organization;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class OrganizationToOrganizationCommand implements Converter<Organization, OrganizationCommand> {
    @Override
    public OrganizationCommand convert(Organization source) {
        OrganizationCommand command = new OrganizationCommand();
        command.setId(source.getId());
        command.setName(source.getName());
        command.setDescription(source.getDescription());
        return command;
    }
}
