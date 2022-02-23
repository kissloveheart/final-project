package com.hiep.finalproject.service;

import com.hiep.finalproject.dto.DonationDto;
import com.hiep.finalproject.repository.DonationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonationServiceImpl implements DonationService{
    @Autowired
    private DonationRepository donationRepository;
    @Autowired AccountService accountService;
    @Override
    public List<DonationDto> getAllDonationDtoByAccountId(Long id, Pageable pageable) {
        return donationRepository.getAllByAccountId(id,pageable).toList()
                .stream().map(DonationDto::new).collect(Collectors.toList());
    }

    @Override
    public List<DonationDto> getAllDonationDtoByCurrentAccount(Pageable pageable) {
        return getAllDonationDtoByAccountId(accountService.getCurrentAccount().getId(),pageable);
    }
}
