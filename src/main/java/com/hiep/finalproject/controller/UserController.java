package com.hiep.finalproject.controller;

import com.hiep.finalproject.command.AccountCommand;
import com.hiep.finalproject.form.AccountForm;
import com.hiep.finalproject.form.ChangePasswordForm;
import com.hiep.finalproject.listener.OnRegistrationCompleteEvent;
import com.hiep.finalproject.model.Account;
import com.hiep.finalproject.model.VerificationToken;
import com.hiep.finalproject.repository.AccountRepository;
import com.hiep.finalproject.service.AccountService;
import com.hiep.finalproject.service.DonationService;
import com.hiep.finalproject.validator.AccountValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.Calendar;
import java.util.Optional;

@Controller
@Slf4j
public class UserController {
    @Autowired
    private AccountService accountService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    private AccountValidator accountValidator;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DonationService donationService;


    @InitBinder("accountForm")
    public void customizeBinding(WebDataBinder binder) {
        Object target = binder.getTarget();
        if (target == null) {
            return;
        }
        if (target.getClass() == AccountForm.class) {
            binder.setValidator(accountValidator);
        }
    }

    @GetMapping("/login")
    public String login(){
        return "/user/login";
    }

    @GetMapping("/user_info")
    public String userInfo(Model model) {
        model.addAttribute("userInfo", accountService.getAccountCommand());
        model.addAttribute("donationList",
                donationService.getAllDonationDtoByCurrentAccount(PageRequest.of(0,20)));
        return "/user/userInfo";
    }

    @GetMapping("/logoutSuccessful")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "/user/logoutSuccessfulPage";
    }

    @GetMapping("/regis")
    public String regisPage(Model model) {
        model.addAttribute("accountForm", new AccountForm());
        return "/user/registration";
    }

    @PostMapping("/regis")
    public String regis(@ModelAttribute("accountForm") @Validated AccountForm accountForm
            , BindingResult result, HttpServletRequest request, Model model) throws IOException {
        // Validate result
        if (result.hasErrors()) {
            return "/user/registration";
        }
        AccountCommand saveApp = accountService.saveAccount(accountForm);
        //
        String appUrl = String.valueOf(request.getRequestURL());
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(saveApp,
                request.getLocale(), appUrl, null));
        model.addAttribute("message",
                "Đăng ký thành công, vui lòng kích hoạt tài khoản bằng đường dẫn trong email đăng ký!");
        return "/user/registration";
    }

    @GetMapping("/regis/confirm/{token:[A-Za-z0-9-]{36}}")
    public String confirmRegis(Model model, @PathVariable String token) {
        // Verify token
        VerificationToken verificationToken = accountService.getVerificationToken(token);

        if (verificationToken == null) {
            model.addAttribute("message", "Token không hợp lệ");
            return "/user/registration";
        }

        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("message", "Token đã hết hạn, vui lòng đăng ký lại!");
            return "/user/registration";
        }
        accountService.setEnableAccount(verificationToken);
        model.addAttribute("message", "Kích hoạt thành công, bạn có thể đăng nhập!");
        return "/user/login";
    }

    @GetMapping("/forget_password")
    public String forgetPassword(){
        return "/user/forgetPassword";
    }

    @PostMapping("/forget_password")
    public String forgetPassword(@RequestParam String email,Model model,HttpServletRequest request){
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        if (accountOptional.isEmpty()){
            model.addAttribute("error","Email không tồn tại");
            return "/user/forgetPassword";
        }
        String appUrl = String.valueOf(request.getRequestURL());
        accountService.sendForgetPasswordEmail(accountOptional.get(), appUrl);
        model.addAttribute("message","Vui lòng kiểm tra email để lấy lại mật khẩu");
        return "/user/forgetPassword";
    }


    @GetMapping("/forget_password/confirm/{token:[A-Za-z0-9-]{36}}")
    public String confirmForgetPassword(Model model, @PathVariable String token) {
        // Verify token
        VerificationToken verificationToken = accountService.getVerificationToken(token);

        if (verificationToken == null) {
            model.addAttribute("error", "Token không hợp lệ");
            return "/user/forgetPassword";
        }

        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0) {
            model.addAttribute("error", "Token đã hết hạn, vui lòng lấy lại mật khẩu");
            return "/user/forgetPassword";
        }
        AccountForm accountForm = new AccountForm(verificationToken.getAccount());
        model.addAttribute("resetForm",accountForm);
        log.info(accountForm.toString());
        return "/user/resetPassword";
    }

    @PostMapping("/reset_password")
    public String resetPassword(@ModelAttribute("resetForm") @Validated AccountForm accountForm
            , BindingResult result,Model model) throws IOException {
        log.info(accountForm.toString());
        // Validate result
        if (result.hasErrors()) {
            return "/user/resetPassword";
        }
        accountService.saveAccount(accountForm);
        model.addAttribute("message","Thay đổi mật khẩu thành công!");
        return "/user/login";
    }

    @GetMapping("/change_password")
    public String resetPassword(Model model){
        ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        model.addAttribute("changePasswordForm",changePasswordForm);
        model.addAttribute("userInfo", accountService.getAccountCommand());
        return "/user/changePassword";
    }
    @PostMapping("/change_password")
    public String resetPassword(@ModelAttribute("changePasswordForm") @Valid
                                            ChangePasswordForm changePasswordForm, Model model,RedirectAttributes redirectAttributes){
        if (changePasswordForm == null){
            model.addAttribute("error","Có lỗi");
            return "/user/changePassword";
        }
        if(!accountService.checkPassword(changePasswordForm)){
            model.addAttribute("error","Mật khẩu không đúng");
            return "/user/changePassword";
        }
        if(changePasswordForm.getNewRePass().equals(changePasswordForm.getOldPass())){
            model.addAttribute("error","Mật khẩu mới không được trùng với mật khẩu cũ");
            return "/user/changePassword";
        }
        accountService.saveNewPassword(changePasswordForm.getNewPass());
        redirectAttributes.addAttribute("message","Thay đổi mật thành công");
        return "redirect:user_info";
    }

    @GetMapping("/edit_profile")
    public String editProfile(Model model, Principal principal){
        if(principal != null){
            User user = (User) ((Authentication) principal).getPrincipal();
            AccountForm accountForm = new AccountForm(accountRepository.findByEmail(user.getUsername()).get());
            model.addAttribute("userInfo",accountForm);
        }
        return "/user/editUserInfo";
    }

    @PostMapping("/edit_profile")
    public String editProfile(@ModelAttribute("userInfo") AccountForm accountForm, RedirectAttributes redirectAttributes)
            throws IOException {
        if(accountForm != null){
            accountService.saveAccount(accountForm);
            log.info("save info account successfully");
            redirectAttributes.addAttribute("message","Cập nhật thông tin thành công");
            return "redirect:user_info";
        }
        redirectAttributes.addAttribute("error","Cập nhật thông tin bị lỗi");
        return "redirect:user_info";
    }

    @GetMapping("/403")
    public String accessDenied(Model model, Principal principal) {

        if (principal != null) {
            String message = "Hi " + principal.getName() //
                    + "<br> You do not have permission to access this page!";
            model.addAttribute("message", message);
        }
        return "/user/403Page";
    }

/*    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFound(){

        log.error("Handling not found exception");

        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("404error");

        return modelAndView;
    }*/

}
