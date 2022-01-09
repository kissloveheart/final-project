package com.hiep.finalproject.form;

import com.hiep.finalproject.entity.Organization;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

public class OrganizationForm {

        private Long Id;
        private String name;
        private String description;
        private MultipartFile fileData;

    public OrganizationForm() {
    }

    public OrganizationForm(Organization organization) {
        Id = organization.getId();
        this.name = organization.getName();
        this.description = organization.getDescription();
    }

    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }
}
