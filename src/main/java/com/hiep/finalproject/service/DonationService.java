package com.hiep.finalproject.service;

import com.hiep.finalproject.dto.DonationDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonationService {
    List<DonationDto> getAllDonationDtoByAccountId(Long id, Pageable pageable);
    List<DonationDto> getAllDonationDtoByCurrentAccount(Pageable pageable);

}
