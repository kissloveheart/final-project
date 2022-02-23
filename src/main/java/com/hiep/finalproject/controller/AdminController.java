package com.hiep.finalproject.controller;

import com.hiep.finalproject.command.OrganizationCommand;
import com.hiep.finalproject.form.CampaignForm;
import com.hiep.finalproject.form.OrganizationForm;
import com.hiep.finalproject.model.Campaign;
import com.hiep.finalproject.model.Organization;
import com.hiep.finalproject.service.AccountService;
import com.hiep.finalproject.service.CampaignService;
import com.hiep.finalproject.service.OrganizationService;
import com.hiep.finalproject.validator.CampaignValidator;
import com.hiep.finalproject.validator.OrganizationValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;

@Slf4j
@Controller
@SessionAttributes({"sort", "filter", "sortOrganization"})
public class AdminController {
    @Autowired
    private CampaignService campaignService;
    @Autowired
    private CampaignValidator campaignValidator;
    @Autowired
    private OrganizationValidator organizationValidator;
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private AccountService accountService;

    @InitBinder({"campaignForm", "organizationForm"})
    public void customizeBinding(WebDataBinder binder) {

        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd");
        binder.addCustomFormatter(dateFormatter, "startDate", "endDate");

        Object target = binder.getTarget();
        if (target == null) {
            return;
        }

        if (target.getClass() == CampaignForm.class) {
            binder.setValidator(campaignValidator);
        }
        if (target.getClass() == OrganizationForm.class) {
            binder.setValidator(organizationValidator);
        }
    }

    /*-------- Handle campaign----------*/
    @GetMapping("/management/list-campaign")
    public String listCampaign(Model model, @RequestParam(required = false) String notifySuccess,
                               @RequestParam(required = false) String notifyFail) {
        //Handle notify
        if (notifySuccess != null) {
            model.addAttribute("notifySuccess", notifySuccess);
        }
        if (notifyFail != null) {
            model.addAttribute("notifyFail", notifyFail);
        }
        model.addAttribute("campaignList", campaignService.getAllCampaign());
        return "/management/listCampaign";
    }

    @GetMapping("/management/delete/{stringId}")
    public String deleteCampaign(@PathVariable String stringId, RedirectAttributes redirectAttributes) {
        Boolean isDelete = campaignService.deleteCampaignIdList(stringId);
        if (!isDelete) {
            redirectAttributes.addAttribute("notifyFail", "Xóa đợt quyên góp không thành công");
            return "redirect:/management/list-campaign";
        }
        log.info("Delete campaign successfully");
        redirectAttributes.addAttribute("notifySuccess", "Đã xóa thành công đợt quyên góp");
        return "redirect:/management/list-campaign";
    }

    @GetMapping("/management/campaign/new")
    public String newCampaign(Model model) {
        model.addAttribute("campaignForm", new CampaignForm());
        model.addAttribute("organizationList", organizationService.getAllOrganization());
        return "/management/createCampaign";
    }

    @GetMapping("/management/campaign/edit/{Id:\\d+}")
    public String editCampaign(Model model, @PathVariable String Id, RedirectAttributes redirectAttributes) {
        Campaign campaign = campaignService.getCampaignById(Long.parseLong(Id));

        if (campaign == null) {
            log.warn("Can not find the campaign of id: " + Id);
            redirectAttributes.addAttribute("notifyFail", "Cập nhật đợt quyên góp không thành công!");
            return "redirect:/management/list-campaign";
        }

        CampaignForm campaignForm = new CampaignForm(campaign);
        model.addAttribute("campaignForm", campaignForm);
        model.addAttribute("organizationList", organizationService.getAllOrganization());
        return "/management/createCampaign";
    }

    @PostMapping("/management/campaign/new")
    public String saveCampaign(@ModelAttribute("campaignForm") @Validated CampaignForm campaignForm,
                               BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        // Validate result
        if (result.hasErrors()) {
            model.addAttribute("organizationList", organizationService.getAllOrganization());
            if (campaignForm.getId() == null) {
                return "/management/createCampaign";
            }
            return "redirect:/management/campaign/edit/" + campaignForm.getId();
        }

        try {
            campaignService.saveCampaign(campaignForm);
            log.info("save successfully");
            if (campaignForm.getId() == null) {
                redirectAttributes.addAttribute("notifySuccess", "Thêm đợt quyên góp thành công");
            } else {
                redirectAttributes.addAttribute("notifySuccess", "Cập nhật đợt quyên góp thành công");
            }
        } catch (IOException | ParseException e) {
            if (campaignForm.getId() == null) {
                redirectAttributes.addAttribute("notifyFail", "Thêm đợt quyên góp không thành công");
            } else {
                redirectAttributes.addAttribute("notifyFail", "Cập nhật đợt quyên góp không thành công");
            }
            log.warn("save unsuccessfully", e);
        }
        return "redirect:/management/list-campaign";
    }

    @GetMapping({"/campaign/{id:\\d+}/image", "/organization/{id:\\d+}/image","/account/{id:\\d+}/image"})
    public void getImageFromDB(@PathVariable String id, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        Campaign campaign = null;
        Organization organization = null;
        String requestURL = request.getRequestURI();
        String regexCampaign = "/campaign/\\d+/image";
        String regexOrganization = "/organization/\\d+/image";
        String regexAccount = "/account/\\d+/image";

        if (requestURL.matches(regexCampaign)) {
            if (id != null) {
                campaign = campaignService.getCampaignById(Long.parseLong(id));
            }
            if (campaign != null && campaign.getImage() != null) {
                response.setContentType("image/jpg");
                response.getOutputStream().write(campaign.getImage());
            }
        }

        if (requestURL.matches(regexOrganization)) {
            if (id != null) {
                organization = organizationService.getOrganizationById(Long.parseLong(id));
            }
            if (organization != null && organization.getImage() != null) {
                response.setContentType("image/jpg");
                response.getOutputStream().write(organization.getImage());
            }
        }
        if (requestURL.matches(regexAccount) && id != null) {
            byte[] image;
            image= accountService.getAccountAvatar(Long.parseLong(id));
            if (image!= null && image.length > 0) {
                response.setContentType("image/jpg");
                response.getOutputStream().write(image);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        }
        response.getOutputStream().close();
    }

    /*-------- Handle organization----------*/
    @GetMapping(value = {"/management/list-organization"})
    public String listOrganization(Model model,
                                   @RequestParam(required = false) String notifySuccess,
                                   @RequestParam(required = false) String notifyFail) {
        //Handle notify
        if (notifySuccess != null) {
            model.addAttribute("notifySuccess", notifySuccess);
        }
        if (notifyFail != null) {
            model.addAttribute("notifyFail", notifyFail);
        }

        model.addAttribute("organizationList", organizationService.getAllOrganizationDto());

        return "/management/listOrganization";
    }

    @GetMapping("/management/organization/delete/{checkId}")
    public String deleteOrganization(@PathVariable String checkId,
                                     RedirectAttributes redirectAttributes) {
        Boolean isDelete = organizationService.deleteOrganizationIdList(checkId);
        if (!isDelete) {
            redirectAttributes.addAttribute("notifyFail", "Xóa đơn vị tổ chức không thành công");
        }
        log.info("Delete organization successfully");
        redirectAttributes.addAttribute("notifySuccess", "Đã xóa thành công đơn vị tổ chức");
        return "redirect:/management/list-organization";
    }

    @GetMapping("/management/organization/new")
    public String newOrganization(Model model) {
        model.addAttribute("organizationForm", new OrganizationForm());
        return "/management/createOrganization";
    }

    @GetMapping("/management/organization/edit/{id:\\d+}")
    public String editOrganization(Model model, @PathVariable String id, RedirectAttributes redirectAttributes) {
        OrganizationCommand organizationCommand = organizationService.getOrganizationCommandById(Long.parseLong(id));

        if (organizationCommand == null) {
            log.warn("Can not find the organization of id: " + id);
            redirectAttributes.addAttribute("notifyFail", "Cập nhật đơn vị tổ chức không thành công!");
            return "redirect:/management/list-organization";
        }

        OrganizationForm organizationForm = new OrganizationForm(organizationCommand);
        model.addAttribute("organizationForm", organizationForm);
        return "/management/createOrganization";
    }

    @PostMapping("/management/organization/new")
    public String saveOrganization(@ModelAttribute("organizationForm") @Validated OrganizationForm organizationForm,
                                   BindingResult result, RedirectAttributes redirectAttributes) {
        // Validate result
        if (result.hasErrors()) {
            if (organizationForm.getId() == null) {
                return "/management/createOrganization";
            }
            return "redirect:/management/organization/edit/" + organizationForm.getId();
        }

        try {
            organizationService.saveOrganization(organizationForm);
            log.info("Save organization successfully");
            if (organizationForm.getId() == null) {
                redirectAttributes.addAttribute("notifySuccess", "Thêm đơn vị tổ chức thành công");
            } else {
                redirectAttributes.addAttribute("notifySuccess", "Cập nhật đơn vị tổ chức thành công");
            }
        } catch (IOException | ParseException e) {
            if (organizationForm.getId() == null) {
                redirectAttributes.addAttribute("notifyFail", "Thêm đơn vị tổ chức không thành công");
            } else {
                redirectAttributes.addAttribute("notifyFail", "Cập nhật đơn vị tổ chức không thành công");
                log.warn("save unsuccessfully", e);
            }
        }
        return "redirect:/management/list-organization";
    }

    @GetMapping("/management/list-user")
    public String getAllListUser(@RequestParam(required = false) String role,
                                 @RequestParam(required = false) Boolean enabled,Model model){
        model.addAttribute("userList",accountService.getAllAccountList(role,enabled));
        model.addAttribute("role",role);
        model.addAttribute("enabled",enabled);
        return "/management/listUser";
    }


}
