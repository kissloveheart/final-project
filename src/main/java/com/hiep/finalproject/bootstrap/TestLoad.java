package com.hiep.finalproject.bootstrap;

import com.hiep.finalproject.dao.AccountDAO;
import com.hiep.finalproject.dao.CampaignDAO;
import com.hiep.finalproject.dao.DonationDAO;
import com.hiep.finalproject.dao.OrganizationDAO;
import com.hiep.finalproject.entity.Account;
import com.hiep.finalproject.entity.Campaign;
import com.hiep.finalproject.model.CampaignInfo;
import com.hiep.finalproject.model.OrganizationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

@Component
public class TestLoad implements CommandLineRunner {
    private static  final Logger log = LoggerFactory.getLogger(TestLoad.class);

    @Autowired
    private CampaignDAO campaignDAO;
    @Autowired
    private OrganizationDAO organizationDAO;
    @Autowired
    private DonationDAO donationDAO;
    @Autowired
    private AccountDAO accountDAO;

    @Override
    public void run(String... args) throws Exception {
/*        Campaign id = campaignDAO.findCampaign(1L);
       donationDAO.getDonationList(1L).forEach(Donation -> log.info(Donation.toString()));
        log.info(campaignDAO.getCampaignInfo(3L).toString());
       campaignDAO.getCampaignInfoList("","",1,2,2).getList().forEach(
               CampaignInfo -> log.info(CampaignInfo.toString()));
       log.info(organizationDAO.getOrganizationInfo(1L).toString());
        organizationDAO.getOrganizationInfoList("o.name desc",1,5,2)
                .getList().forEach(OrganizationInfo ->log.warn(OrganizationInfo.toString()));*/
        Account account =  accountDAO.findAccount("admin1@admin.com");
        log.info(account.toString());
    }

    public static void main(String[] args) throws ParseException {
        String regex = "/campaign/\\d+/image";
        String s = "/campaign/35/image";
        System.out.println(s.matches(regex));
    }
}
