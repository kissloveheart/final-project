package com.hiep.finalproject.model;

import com.hiep.finalproject.entity.Organization;

import java.util.Date;

public class CampaignInfo {
    private Long Id;
    private String name;
    private String description;
    private Organization organization;
    private Date startDate;
    private Date endDate;
    private Long countDonation;
    private Double moneyDonation;
    private Double targetBudget;
    private Boolean status;


    public CampaignInfo(Long id, String name, String description, Organization organization, Date startDate,
                        Date endDate, Long countDonation, Double moneyDonation, Double targetBudget, Boolean status) {
        Id = id;
        this.name = name;
        this.description = description;
        this.organization = organization;
        this.startDate = startDate;
        this.endDate = endDate;
        this.countDonation = countDonation;
        this.moneyDonation = moneyDonation;
        this.targetBudget = targetBudget;
        this.status = status;
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

    public Long getCountDonation() {
        return countDonation;
    }

    public void setCountDonation(Long countDonation) {
        this.countDonation = countDonation;
    }

    public Double getMoneyDonation() {
        return moneyDonation;
    }

    public void setMoneyDonation(Double moneyDonation) {
        this.moneyDonation = moneyDonation;
    }

    public Double getTargetBudget() {
        return targetBudget;
    }

    public void setTargetBudget(Double targetBudget) {
        this.targetBudget = targetBudget;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "CampaignInfo{" +
                "name='" + name + '\'' +
                ", organization='" + organization + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", countDonation=" + countDonation +
                ", moneyDonation=" + moneyDonation +
                ", targetBudget=" + targetBudget +
                ", status=" + status +
                '}';
    }
}
