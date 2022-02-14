package com.hiep.finalproject.form;

import com.hiep.finalproject.command.CampaignCommand;
import com.hiep.finalproject.model.Campaign;
import com.hiep.finalproject.model.Organization;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

@Data
@NoArgsConstructor
public class CampaignForm {
    private Long id;
    private String name;
    private String description;
    private Organization organization;
    private Date startDate;
    private Date endDate;
    private String targetBudget;
    private boolean status;
    private MultipartFile fileData;

    public CampaignForm(Campaign campaign) {
        id = campaign.getId();
        name = campaign.getName();
        description = campaign.getDescription();
        organization = campaign.getOrganization();
        startDate = campaign.getStartDate();
        endDate = campaign.getEndDate();
        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi","VN"));
        targetBudget = numberFormat.format(campaign.getTargetBudget());
        status = campaign.isStatus();
    }

}
