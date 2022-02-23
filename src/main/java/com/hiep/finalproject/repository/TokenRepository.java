package com.hiep.finalproject.repository;

import com.hiep.finalproject.model.VerificationToken;
import org.springframework.data.repository.CrudRepository;

public interface TokenRepository extends CrudRepository<VerificationToken,Long> {
    VerificationToken findByToken(String verificationToken);
}
