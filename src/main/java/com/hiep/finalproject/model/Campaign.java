package com.hiep.finalproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="campaign_id")
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.PERSIST})
    @JoinColumn(name="organization_id", nullable = false)
    private Organization organization;
    @Column(columnDefinition = "nvarchar(255)")
    private String name;
    private double targetBudget;
    private Date startDate;
    private Date endDate;
    private boolean status;
    @Column(columnDefinition = "nvarchar(2555)")
    private String description;
    @Lob
    private byte[] image;
    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Donation> donationList;

}
