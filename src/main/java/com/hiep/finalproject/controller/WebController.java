package com.hiep.finalproject.controller;

import com.hiep.finalproject.command.DonationCommand;
import com.hiep.finalproject.exceptions.BalanceTransactionException;
import com.hiep.finalproject.service.CampaignService;
import com.hiep.finalproject.service.DonationService;
import com.hiep.finalproject.validator.DonationValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WebController {
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private DonationValidator donationValidator;
    @Autowired
    private DonationService donationService;

    @InitBinder("donationCommand")
    public void customizeBinding(WebDataBinder binder) {
        Object target = binder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == DonationCommand.class) {
            binder.setValidator(donationValidator);
        }
    }

    @GetMapping({"/","/index"})
    public String index(Model model){
        model.addAttribute("campaignList",campaignService.getAllCampaign());
        return "index";
    }
    @GetMapping("/campaign/{id:\\d+}")
    public String campaign(Model model, @PathVariable Long id){
        model.addAttribute("campaign",campaignService.getFullCampaignCommandById(id));
        model.addAttribute("campaignList",campaignService.getAllCampaign(PageRequest.of(0,3)));
        DonationCommand donationCommand = new DonationCommand();
        donationCommand.setCampaignId(id);
        model.addAttribute("donationCommand", donationCommand);
        return "/web/campaign";
    }
    @PostMapping("/donation")
    public String donation(@ModelAttribute @Validated DonationCommand donationCommand, BindingResult result, RedirectAttributes redirectAttributes){
        String uri = "campaign/"+donationCommand.getCampaignId();
        if (result.hasErrors()) {
            return "redirect:"+uri;
        }
        try {
            donationService.donateCampaign(donationCommand);
            redirectAttributes.addFlashAttribute("message","Quyên góp thành công");
            return "redirect:"+uri;
        } catch (BalanceTransactionException e) {
            redirectAttributes.addFlashAttribute("error",e.getMessage());
            return "redirect:"+uri;
        }
    }
}
