package com.hiep.finalproject.model;


import com.hiep.finalproject.entity.Campaign;

import java.util.List;

public class OrganizationInfo {
    private Long Id;
    private String name;
    private String description;
    private Long countCampaign;
    private Double moneyDonation;
    private List<Campaign> campaignList;

    public OrganizationInfo() {
    }

    public OrganizationInfo(Long id, String name, String description, Long countCampaign, Double moneyDonation) {
        Id = id;
        this.name = name;
        this.description = description;
        this.countCampaign = countCampaign;
        this.moneyDonation = moneyDonation;
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

    public void setCountCampaign(Long countCampaign) {
        this.countCampaign = countCampaign;
    }

    public Long getCountCampaign() {
        return countCampaign;
    }

    public Double getMoneyDonation() {
        return moneyDonation;
    }

    public void setMoneyDonation(Double moneyDonation) {
        this.moneyDonation = moneyDonation;
    }

    public List<Campaign> getCampaignList() {
        return campaignList;
    }

    public void setCampaignList(List<Campaign> campaignList) {
        this.campaignList = campaignList;
    }

    @Override
    public String toString() {
        return "OrganizationInfo{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", countCampaign=" + countCampaign +
                ", moneyDonation=" + moneyDonation +
                '}';
    }
}
