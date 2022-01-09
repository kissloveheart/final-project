package com.hiep.finalproject.form;

import com.hiep.finalproject.entity.Campaign;
import com.hiep.finalproject.entity.Organization;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

public class CampaignForm {
    private Long Id;
    private String name;
    private String description;
    private Organization organization;
    private Date startDate;
    private Date endDate;
    private String targetBudget;
    private boolean status;
    private MultipartFile fileData;

    public CampaignForm() {
    }

    public CampaignForm(Campaign campaign) {
        Id = campaign.getId();
        name = campaign.getName();
        description = campaign.getDescription();
        organization = campaign.getOrganization();
        startDate = campaign.getStartDate();
        endDate = campaign.getEndDate();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi","VN"));
        targetBudget = numberFormat.format(campaign.getTargetBudget());
        status = campaign.isStatus();
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

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTargetBudget() {
        return targetBudget;
    }

    public void setTargetBudget(String targetBudget) {
        this.targetBudget = targetBudget;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public MultipartFile getFileData() {
        return fileData;
    }

    public void setFileData(MultipartFile fileData) {
        this.fileData = fileData;
    }

    @Override
    public String toString() {
        return "CampaignForm{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", organization=" + organization.getId() +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", targetBudget=" + targetBudget +
                ", status=" + status +
                '}';
    }
}
