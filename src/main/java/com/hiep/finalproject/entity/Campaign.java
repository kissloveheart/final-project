package com.hiep.finalproject.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name ="Campaign")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Campaign_ID")
    private Long Id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="Organization_ID", nullable = false)
    private Organization organization;

    @Column(name="Name")
    private String name;

    @Column(name="Target_budget")
    private double targetBudget;

    @Column(name="Start_date")
    private Date startDate;

    @Column(name="End_date")
    private Date endDate;

    @Column(name= "Status")
    private boolean status;

    @Column(name="Description", length = 3000)
    private String description;

    @Lob
    @Column(name="Image")
    private byte[] image;



    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getTargetBudget() {
        return targetBudget;
    }

    public void setTargetBudget(double targetBudget) {
        this.targetBudget = targetBudget;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Campaign campaign = (Campaign) o;

        return Objects.equals(Id, campaign.Id);
    }

    @Override
    public int hashCode() {
        return Id != null ? Id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Campaign{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", targetBudget=" + targetBudget +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status=" + status +
                ", description='" + description + '\'' +
                '}';
    }
}
