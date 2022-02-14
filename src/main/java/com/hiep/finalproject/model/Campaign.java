package com.hiep.finalproject.model;

import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name ="Campaign")
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Campaign_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST,CascadeType.PERSIST})
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

    @OneToMany(mappedBy = "campaign", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Donation> donationList;

}
