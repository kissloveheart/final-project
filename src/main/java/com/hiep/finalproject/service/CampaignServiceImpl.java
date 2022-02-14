package com.hiep.finalproject.service;

import com.hiep.finalproject.command.CampaignCommand;
import com.hiep.finalproject.converter.CampaignToCampaignCommand;
import com.hiep.finalproject.form.CampaignForm;
import com.hiep.finalproject.model.Campaign;
import com.hiep.finalproject.model.Organization;
import com.hiep.finalproject.repository.CampaignRepository;
import com.hiep.finalproject.repository.OrganizationRepository;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Slf4j
@Service
public class CampaignServiceImpl implements CampaignService{
    @Autowired
    private CampaignRepository campaignRepository;
    @Autowired
    private CampaignToCampaignCommand campaignToCampaignCommand;
    @Autowired
    private OrganizationRepository organizationRepository;


    @Override
    public Campaign getCampaignById(Long id) {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(id);
        if(optionalCampaign.isEmpty()){
            return null;
        }
        return optionalCampaign.get();
    }

    @Override
    public CampaignCommand getCampaignCommandById(Long id) {
        Optional<Campaign> optionalCampaign = campaignRepository.findById(id);
        if(optionalCampaign.isEmpty()){
            return null;
        }
        return campaignToCampaignCommand.convert(optionalCampaign.get());
    }

    @Override
    public List<CampaignCommand> getAllCampaign() {
        List<CampaignCommand> campaignCommandList = new ArrayList<>();
        campaignRepository.findAll().iterator()
                .forEachRemaining(campaign -> campaignCommandList.add(campaignToCampaignCommand.convert(campaign)));
        return campaignCommandList;
    }

    @Override
    public Boolean deleteCampaignIdList(String listId) {
        String[] idDelete = listId.split("-");
        List<Long> campaignDeleteIdList = new ArrayList<>();

        if(idDelete.length > 0){
            for (String id : idDelete) {
                try {
                    campaignDeleteIdList.add(Long.parseLong(id));
                } catch (NumberFormatException e) {
                    log.warn("This is not a number");
                    return false;
                }
            }
        }
        campaignRepository.deleteAllById(campaignDeleteIdList);
        return true;
    }

    @Override
    public void saveCampaign(CampaignForm campaignForm) throws ParseException, IOException{
        Campaign campaign;
        Optional<Organization> organization = organizationRepository.findById(campaignForm.getOrganization().getId());
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        double targetBudget = numberFormat.parse(campaignForm.getTargetBudget()).doubleValue();;

        if(campaignForm.getId() != null){
            campaign = campaignRepository.findById(campaignForm.getId()).get();
        } else {
            campaign = new Campaign();
        }
        campaign.setId(campaignForm.getId());
        campaign.setName(campaignForm.getName());
        campaign.setDescription(campaignForm.getDescription());
        campaign.setStartDate(campaignForm.getStartDate());
        campaign.setEndDate(campaignForm.getEndDate());
        campaign.setStatus(campaignForm.isStatus());
        campaign.setTargetBudget(targetBudget);
        campaign.setOrganization(organization.get());

        if(campaignForm.getFileData() != null){
            byte[] image = campaignForm.getFileData().getBytes();
            if(image.length > 0){
                campaign.setImage(image);
            }
        }
        campaignRepository.save(campaign);
    }

}
