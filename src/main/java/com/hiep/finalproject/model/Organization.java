package com.hiep.finalproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="organization_id")
    private Long id;
    @Column(unique = true,columnDefinition = "nvarchar(255)")
    private String name;
    @Column(columnDefinition = "nvarchar(2555)")
    private String description;
    @Lob
    private byte[] image;
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Campaign> campaignList = new ArrayList<>();

}
