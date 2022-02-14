package com.hiep.finalproject.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="Account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "User_ID")
    private Long id;

    @Column(name="Email")
    private String email;

    @Column(name="Password")
    private String password;

    @Column(name="Phone")
    private String phone;

    @Column(name = "Role")
    private String role;

    @Column(name="Enable")
    private boolean enable;

    @Column(name="Image")
    private byte[] image;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Donation> donationList;

}
