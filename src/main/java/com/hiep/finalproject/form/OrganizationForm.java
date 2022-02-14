package com.hiep.finalproject.form;

import com.hiep.finalproject.command.OrganizationCommand;
import com.hiep.finalproject.model.Organization;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
public class OrganizationForm {

        private Long id;
        private String name;
        private String description;
        private MultipartFile fileData;

    public OrganizationForm(OrganizationCommand organization) {
        this.id = organization.getId();
        this.name = organization.getName();
        this.description = organization.getDescription();
    }

}
