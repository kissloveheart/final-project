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
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    @Column(unique = true)
    private String email;
    @Column(name = "password")
    private String encryptedPassword;
    private String phone;
    private String role = "USER";
    private boolean enable;
    @Lob @Basic(fetch = FetchType.LAZY)
    private byte[] image;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate = new Date();
    private Double balance = 0D;
    private String address;
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Donation> donationList;
    @OneToOne(mappedBy = "account",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    VerificationToken verificationToken;
}
