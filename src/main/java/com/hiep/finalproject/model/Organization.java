package com.hiep.finalproject.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="Organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="Organization_ID")
    private Long id;

    @Column(name="Name", length = 100, unique = true)
    private String name;

    @Column(name="Description", length = 1200)
    private String description;

    @Lob
    @Column(name="Image")
    private byte[] image;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Campaign> campaignList = new ArrayList<>();

}
