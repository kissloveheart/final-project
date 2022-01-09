package com.hiep.finalproject.controller;

import com.hiep.finalproject.dao.CampaignDAO;
import com.hiep.finalproject.dao.DonationDAO;
import com.hiep.finalproject.dao.OrganizationDAO;
import com.hiep.finalproject.entity.Campaign;
import com.hiep.finalproject.entity.Organization;
import com.hiep.finalproject.form.CampaignForm;
import com.hiep.finalproject.form.OrganizationForm;
import com.hiep.finalproject.model.CampaignInfo;
import com.hiep.finalproject.model.OrganizationInfo;
import com.hiep.finalproject.pagination.PaginationResult;
import com.hiep.finalproject.validator.CampaignValidator;
import com.hiep.finalproject.validator.OrganizationValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.format.number.CurrencyStyleFormatter;
import org.springframework.format.number.NumberStyleFormatter;
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
import java.util.*;

@Controller
@SessionAttributes({"sort","filter","sortOrganization"})
public class AdminController {
    private static final Logger log = LoggerFactory.getLogger(AdminController.class);
    @Autowired
    private CampaignDAO campaignDAO;
    @Autowired
    private DonationDAO donationDAO;
    @Autowired
    private OrganizationDAO organizationDAO;

    @Autowired
    private CampaignValidator campaignValidator;

    @Autowired
    private OrganizationValidator organizationValidator;

    @InitBinder({"campaignForm","organizationForm"})
    public void customizeBinding (WebDataBinder binder) {

        DateFormatter dateFormatter = new DateFormatter();
        dateFormatter.setPattern("yyyy-MM-dd");
        binder.addCustomFormatter(dateFormatter, "startDate","endDate");

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
    @GetMapping(value={"/management/list-campaign/{pageStr}","/management/list-campaign" })
    public String listCampaign(Model model, @PathVariable(required = false) String pageStr,
                               @RequestParam(name = "sort",defaultValue = "") String sortStr,
                               @RequestParam(name = "filter",defaultValue = "") String filterStr,
                               @RequestParam(required = false) String notifySuccess,
                               @RequestParam(required = false) String notifyFail){
        //Handle notify
        if(notifySuccess != null){
            model.addAttribute("notifySuccess", notifySuccess);
        }
        if(notifyFail != null){
            model.addAttribute("notifyFail", notifyFail);
        }
        // Define parameter of pagination
        int MAX_RESULT = 8;
        int MAX_NAVIGATION_PAGE = 4;
        int page = 1;
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                log.info("The page number is not a number");
                e.printStackTrace();
            }
        }
        // Define sort of list
        Integer sort = 0;
        try {
            if(sortStr != null && !sortStr.equals("")){
                sort = Integer.parseInt(sortStr);
                model.addAttribute("sort",sort);
            } else {
                if(model.getAttribute("sort") != null){
                    sort = (Integer) model.getAttribute("sort");
                } else{
                    model.addAttribute("sort",sort);
                }
            }
        } catch (NumberFormatException e) {
            log.info("The sort is not a number");
            e.printStackTrace();
        }
        String orderBy;
        switch (sort){
            case 1:
                orderBy="c.startDate desc";
                break;
            case 2:
                orderBy = "count(d.Id) desc";
                break;
            case 3:
                orderBy = "sum(d.amount) desc";
                break;
            case 4:
                orderBy = "c.name";
                break;
            default:
                orderBy = "c.Id desc";
        }
        // Define filter of list
        Integer filter = 0;
        try {
            if(filterStr != null && !filterStr.equals("")){
                filter = Integer.parseInt(filterStr);
                model.addAttribute("filter",filter);
            } else {
                if(model.getAttribute("filter") != null){
                    filter = (Integer) model.getAttribute("filter");
                } else{
                    model.addAttribute("filter",filter);
                }
            }
        } catch (Exception e) {
            log.info("The filter is wrong");
            e.printStackTrace();
        }
        String filterBy;
        switch (filter){
            case 1:
                filterBy = "c.status = true";
                break;
            case 2:
                filterBy = "c.status = false";
                break;
            default:
                filterBy = "";
        }

        //Get result the list
        PaginationResult<CampaignInfo> pageResult = campaignDAO.getCampaignInfoList(orderBy,filterBy,page,MAX_RESULT,MAX_NAVIGATION_PAGE);
        model.addAttribute("campaignList",pageResult);

        return "/management/listCampaign";
    }
    @GetMapping("/management/delete/{checkId}")
    public String deleteCampaign(@PathVariable String checkId,
                                 RedirectAttributes redirectAttributes){
        String[] idDelete = checkId.split("-");
        List<Long> campaignIdListDelete = new ArrayList<>();
        List<Long> campaignIdList = campaignDAO.getCampaignIdList();
        Integer affectRow;
        if(idDelete != null && idDelete.length > 0){
            for (String s : idDelete) {
                try {
                    if (campaignIdList.contains(Long.parseLong(s))) {
                        campaignIdListDelete.add(Long.parseLong(s));
                    }
                } catch (NumberFormatException e) {
                    log.warn("This is not a number");
                    redirectAttributes.addAttribute("notifyFail", "Xóa đợt quyên góp không thành công");
                    return "redirect:/management/list-campaign";
                }
            }
        }
        if (campaignIdListDelete != null && campaignIdListDelete.size() != 0){
            affectRow = campaignDAO.deleteCampaign(campaignIdListDelete);
            log.info(String.valueOf(affectRow));
            if( affectRow < 1){
                log.warn("Delete campaign unsuccessfully");
                redirectAttributes.addAttribute("notifyFail","Xóa đợt quyên góp không thành công");
                return "redirect:/management/list-campaign";
            }
        }
        log.info("Delete campaign successfully");
        redirectAttributes.addAttribute("notifySuccess", "Đã xóa thành công "+idDelete.length+" đợt quyên góp" );
        return "redirect:/management/list-campaign";
    }
    @GetMapping("/management/campaign/new")
    public String newCampaign(Model model){
        model.addAttribute("campaignForm", new CampaignForm());
        model.addAttribute("organizationList", organizationDAO.getOrganizationList());
        return "/management/createCampaign";
    }

    @GetMapping("/management/campaign/edit/{Id:\\d+}")
    public String editCampaign(Model model, @PathVariable String Id, RedirectAttributes redirectAttributes){
        Campaign campaign = campaignDAO.findCampaign(Long.parseLong(Id));

        if(campaign == null){
            log.warn("Can not find the campaign of id: "+Id);
            redirectAttributes.addAttribute("notifyFail","Cập nhật đợt quyên góp không thành công!");
            return "redirect:/management/list-campaign";
        }

        CampaignForm campaignForm = new CampaignForm(campaign);
        model.addAttribute("campaignForm", campaignForm);
        model.addAttribute("organizationList", organizationDAO.getOrganizationList());
        return "/management/createCampaign";
    }

    @PostMapping("/management/campaign/new")
    public String saveCampaign(@ModelAttribute("campaignForm") @Validated CampaignForm campaignForm,
            BindingResult result, RedirectAttributes redirectAttributes, Model model)
            throws ParseException {
        // Validate result
        if (result.hasErrors()) {
            model.addAttribute("organizationList", organizationDAO.getOrganizationList());
            if(campaignForm.getId() == null) {
                return "/management/createCampaign";
            }
            return "redirect:/management/campaign/edit/" + campaignForm.getId();
        }

       Long campaignId = campaignDAO.saveCampaign(campaignForm);
        if(campaignId != null && campaignId > 0){
            log.info("save successfully");
            if (campaignForm.getId() == null){
                redirectAttributes.addAttribute("notifySuccess" , "Thêm đợt quyên góp thành công");
            } else{
                redirectAttributes.addAttribute("notifySuccess" , "Cập nhật đợt quyên góp thành công");
            }
        } else {
            log.warn("save unsuccessfully");
            if (campaignForm.getId() == null){
                redirectAttributes.addAttribute("notifyFail" , "Thêm đợt quyên góp không thành công");
            } else{
                redirectAttributes.addAttribute("notifyFail" , "Cập nhật đợt quyên góp không thành công");
            }
        }
        return "redirect:/management/list-campaign";
    }

    @GetMapping({"/campaign/{Id:\\d+}/image","/organization/{Id:\\d+}/image"})
    public void getImageFromDB(@PathVariable String Id, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        Campaign campaign =null;
        Organization organization = null;
        String requestURL = request.getRequestURI();
        String regexCampaign = "/campaign/\\d+/image";
        String regexOrganization = "/organization/\\d+/image";

        if (requestURL.matches(regexCampaign)) {
            if(Id != null){
                campaign = campaignDAO.findCampaign(Long.parseLong(Id));
            }
            if (campaign!= null && campaign.getImage() != null){
                response.setContentType("image/jpg");
                response.getOutputStream().write(campaign.getImage());
            }
        }

        if(requestURL.matches(regexOrganization)){
            if(Id != null){
                organization = organizationDAO.findOrganization(Long.parseLong(Id));
            }
            if (organization!= null && organization.getImage() != null){
                response.setContentType("image/jpg");
                response.getOutputStream().write(organization.getImage());
            }
        }

        response.getOutputStream().close();
    }

    /*-------- Handle organization----------*/
    @GetMapping(value={"/management/list-organization/{pageStr}","/management/list-organization" })
    public String listOrganization(Model model, @PathVariable(required = false) String pageStr,
                               @RequestParam(name = "sort",defaultValue = "") String sortStr,
                               @RequestParam(required = false) String notifySuccess,
                               @RequestParam(required = false) String notifyFail){
        //Handle notify
        if(notifySuccess != null){
            model.addAttribute("notifySuccess", notifySuccess);
        }
        if(notifyFail != null){
            model.addAttribute("notifyFail", notifyFail);
        }
        // Define parameter of pagination
        int MAX_RESULT = 5;
        int MAX_NAVIGATION_PAGE = 5;
        int page = 1;
        if (pageStr != null) {
            try {
                page = Integer.parseInt(pageStr);
            } catch (NumberFormatException e) {
                log.info("The page number is not a number");
                e.printStackTrace();
            }
        }
        // Define sort of list
        Integer sort = 0;
        try {
            if(sortStr != null && !sortStr.equals("")){
                sort = Integer.parseInt(sortStr);
                model.addAttribute("sortOrganization",sort);
            } else {
                if(model.getAttribute("sortOrganization") != null){
                    sort = (Integer) model.getAttribute("sortOrganization");
                } else{
                    model.addAttribute("sortOrganization",sort);
                }
            }
        } catch (NumberFormatException e) {
            log.info("The sort is not a number");
            e.printStackTrace();
        }
        String orderBy;
        switch (sort){
            case 1:
                orderBy = "count(c.Id) desc";
                break;
            case 2:
                orderBy = "sum(d.amount) desc";
                break;
            case 3:
                orderBy = "o.name";
                break;
            default:
                orderBy = "o.Id desc";
        }

        //Get result the list
        PaginationResult<OrganizationInfo> pageResult = organizationDAO.getOrganizationInfoList(orderBy,page,MAX_RESULT,MAX_NAVIGATION_PAGE);
        model.addAttribute("organizationList",pageResult);

        return "/management/listOrganization";
    }

    @GetMapping("/management/organization/delete/{checkId}")
    public String deleteOrganization(@PathVariable String checkId,
                                 RedirectAttributes redirectAttributes){
        String[] idDelete = checkId.split("-");
        List<Long> organizationIdListDelete = new ArrayList<>();
        List<Long> organizationIdList = organizationDAO.getOrganizationIdList();
        Integer affectRow;

        if(idDelete != null && idDelete.length > 0){
            for (String s : idDelete) {
                try {
                    if (organizationIdList.contains(Long.parseLong(s))) {
                        organizationIdListDelete.add(Long.parseLong(s));
                    }
                } catch (NumberFormatException e) {
                    log.warn("This is not a number");
                    redirectAttributes.addAttribute("notifyFail", "Xóa đơn vị tổ chức không thành công");
                    return "redirect:/management/list-organization";
                }
            }
        }

        if (organizationIdListDelete != null && organizationIdListDelete.size() != 0){
            affectRow = organizationDAO.deleteOrganization(organizationIdListDelete);
            log.info(String.valueOf(affectRow));
            if( affectRow < 1){
                log.warn("Delete organization unsuccessfully");
                redirectAttributes.addAttribute("notifyFail","Xóa đơn vị tổ chức không thành công");
                return "redirect:/management/list-organization";
            }
        }

        log.info("Delete organization successfully");
        redirectAttributes.addAttribute("notifySuccess", "Đã xóa thành công "+idDelete.length+" đơn vị tổ chức" );
        return "redirect:/management/list-organization";
    }

    @GetMapping("/management/organization/new")
    public String newOrganization(Model model){
        model.addAttribute("organizationForm", new OrganizationForm());
        return "/management/createOrganization";
    }

    @GetMapping("/management/organization/edit/{Id:\\d+}")
    public String editOrganization(Model model, @PathVariable String Id, RedirectAttributes redirectAttributes){
        Organization organization = organizationDAO.findOrganization(Long.parseLong(Id));

        if(organization == null){
            log.warn("Can not find the organization of id: "+Id);
            redirectAttributes.addAttribute("notifyFail","Cập nhật đơn vị tổ chức không thành công!");
            return "redirect:/management/list-organization";
        }

        OrganizationForm organizationForm = new OrganizationForm(organization);
        model.addAttribute("organizationForm", organizationForm);
        return "/management/createOrganization";
    }

    @PostMapping("/management/organization/new")
    public String saveOrganization(@ModelAttribute("organizationForm") @Validated OrganizationForm organizationForm,
                               BindingResult result, RedirectAttributes redirectAttributes, Model model)
            throws ParseException {
        // Validate result
        if (result.hasErrors()) {
            if(organizationForm.getId() == null) {
                return "/management/createOrganization";
            }
            return "redirect:/management/organization/edit/" + organizationForm.getId();
        }

        Long organizationId = organizationDAO.saveOrganization(organizationForm);
        if(organizationId != null && organizationId > 0){
            log.info("save successfully");
            if (organizationForm.getId() == null){
                redirectAttributes.addAttribute("notifySuccess" , "Thêm đơn vị tổ chức thành công");
            } else{
                redirectAttributes.addAttribute("notifySuccess" , "Cập nhật đơn vị tổ chức thành công");
            }
        } else {
            log.warn("save unsuccessfully");
            if (organizationForm.getId() == null){
                redirectAttributes.addAttribute("notifyFail" , "Thêm đơn vị tổ chức không thành công");
            } else{
                redirectAttributes.addAttribute("notifyFail" , "Cập nhật đơn vị tổ chức không thành công");
            }
        }
        return "redirect:/management/list-organization";
    }


}
