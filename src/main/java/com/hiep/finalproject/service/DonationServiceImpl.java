package com.hiep.finalproject.service;

import com.hiep.finalproject.command.DonationCommand;
import com.hiep.finalproject.dto.DonationDto;
import com.hiep.finalproject.exceptions.BalanceTransactionException;
import com.hiep.finalproject.model.Account;
import com.hiep.finalproject.model.Donation;
import com.hiep.finalproject.repository.AccountRepository;
import com.hiep.finalproject.repository.DonationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class DonationServiceImpl implements DonationService {
    @Autowired
    private DonationRepository donationRepository;
    @Autowired
    private AccountService accountService;
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public List<DonationDto> getAllDonationDtoByAccountId(Long id, Pageable pageable) {
        return donationRepository.getAllByAccountId(id, pageable).toList()
                .stream().map(DonationDto::new).collect(Collectors.toList());
    }

    @Override
    public List<DonationDto> getAllDonationDtoByCurrentAccount(Pageable pageable) {
        return getAllDonationDtoByAccountId(accountService.getCurrentAccount().getId(), pageable);
    }

    @Override
    public List<DonationDto> getAllDonationDto(Pageable pageable) {
        return donationRepository.getAllDonationDto(pageable).toList().stream()
                .map(DonationDto::new).collect(Collectors.toList());
    }

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public Boolean deleteDonationIdList(String listId) {
        String[] idDelete = listId.split("-");
        List<Long> donationDeleteIdList = new ArrayList<>();
        if (idDelete.length > 0) {
            for (String id : idDelete) {
                try {
                    donationDeleteIdList.add(Long.parseLong(id));
                } catch (NumberFormatException e) {
                    log.warn("This is not a number");
                    return false;
                }
            }
        }
        donationRepository.deleteAllById(donationDeleteIdList);
        return true;
    }

    @Override
    public void saveDonation(DonationCommand donationCommand) {
        Donation donation = new Donation();
        if (donationCommand.getId() != null) {
            donation.setId(donationCommand.getId());
        }
        donation.setCampaign(campaignService.getCampaignById(donationCommand.getCampaignId()));
        donation.setAccount(accountService.getCurrentAccount());
        donation.setAmount(donationCommand.getAmount());
        donation.setDate(donationCommand.getDate() != null ? donationCommand.getDate() : new Date());
        donationRepository.save(donation);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = BalanceTransactionException.class)
    @Override
    public void donateCampaign(DonationCommand donationCommand) throws BalanceTransactionException {
        solveBalance(donationCommand.getAmount());
        saveDonation(donationCommand);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    @Override
    public void solveBalance(Double amount) throws BalanceTransactionException {
        Account account = accountService.getCurrentAccount();
        if (account == null) {
            log.info("User not login");
            throw new BalanceTransactionException("Bạn chưa đăng nhập để thực hiện giao dịch này");
        }
        if (account.getBalance() < amount) {
            log.info("the money is not enough");
            throw new BalanceTransactionException("Bạn không đủ tiền để thực hiện giao dịch này, vui lòng nạp tiền trong trang cá nhân");
        }
        account.setBalance(account.getBalance() - amount);
        accountRepository.save(account);
    }
}
