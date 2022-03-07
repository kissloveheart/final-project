package com.hiep.finalproject.service;

import com.hiep.finalproject.command.DonationCommand;
import com.hiep.finalproject.dto.DonationDto;
import com.hiep.finalproject.exceptions.BalanceTransactionException;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DonationService {
    List<DonationDto> getAllDonationDtoByAccountId(Long id, Pageable pageable);
    List<DonationDto> getAllDonationDtoByCurrentAccount(Pageable pageable);
    List<DonationDto> getAllDonationDto(Pageable pageable);
    Boolean deleteDonationIdList(String listId);
    void saveDonation(DonationCommand donationCommand);
    void donateCampaign(DonationCommand donationCommand)  throws BalanceTransactionException;
    void solveBalance(Double amount) throws BalanceTransactionException;

}
