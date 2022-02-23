package com.hiep.finalproject.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class VerificationToken {
    private static final int EXPIRATION = 60 * 24;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Verification_Token_Id")
    private Long id;

    private String token;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(nullable = false,name ="user_id", foreignKey = @ForeignKey(name = "FK_VERIFY_USER"))
    private Account account;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiryDate;

    public VerificationToken(String token, Account account) {
        this.token = token;
        this.account = account;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    private Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }

}
