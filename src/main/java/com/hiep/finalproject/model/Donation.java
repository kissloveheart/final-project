package com.hiep.finalproject.model;


import lombok.*;

import javax.persistence.*;
import java.util.Date;
@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
@Table(name="Donation")
public class Donation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="Donation_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Campaign_ID")
    @ToString.Exclude
    private Campaign campaign;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="User_ID")
    @ToString.Exclude
    private Account account;

    @Column(name="Date")
    private Date date;

    @Column(name="Amount")
    private double amount;

}
