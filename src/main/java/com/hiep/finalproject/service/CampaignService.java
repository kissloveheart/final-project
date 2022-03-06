package com.hiep.finalproject.service;

import com.hiep.finalproject.command.CampaignCommand;
import com.hiep.finalproject.form.CampaignForm;
import com.hiep.finalproject.model.Campaign;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface CampaignService {
    Campaign getCampaignById(Long id);
    CampaignCommand getCampaignCommandById(Long id);
    CampaignCommand getFullCampaignCommandById(Long id);
    List<CampaignCommand> getAllCampaign();
    List<CampaignCommand> getAllCampaign(Pageable pageable);
    Boolean deleteCampaignIdList(String listId);
    void saveCampaign(CampaignForm campaignForm) throws ParseException, IOException;
}
