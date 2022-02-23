package com.hiep.finalproject.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

@Service
@Slf4j
public class MailService {

    private static final String EMAIL_REGIS_TEMPLATE_NAME = "html/email-simple";
    private static final String EMAIL_FORGET_TEMPLATE_NAME = "html/email-forget";
    private static final String EMAIL_INLINEIMAGE_TEMPLATE_NAME = "html/email-inlineimage";

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    @Qualifier("emailTemplateEngine")
    private TemplateEngine htmlTemplateEngine;

    @Autowired
    private Environment env;

    /*
     * Send HTML mail (simple)
     */
    public void sendRegisMail(
            String recipientName, String linkToken, String recipientEmail, Locale locale)
            throws MessagingException {

        // Prepare the evaluation context
        Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("link", linkToken);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Vui lòng xác nhận đăng ký");
        message.setFrom(env.getProperty("support.email"));
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        String htmlContent = htmlTemplateEngine.process(EMAIL_REGIS_TEMPLATE_NAME, ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        mailSender.send(mimeMessage);
        log.info("Email sent successfully to: " + recipientEmail);
    }

    public void sendForgetMail(
            String recipientName, String linkToken, String recipientEmail)
            throws MessagingException {

        // Prepare the evaluation context
        Context ctx = new Context();
        ctx.setVariable("name", recipientName);
        ctx.setVariable("link", linkToken);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        message.setSubject("Lấy lại mật khẩu");
        message.setFrom(env.getProperty("support.email"));
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        String htmlContent = htmlTemplateEngine.process(EMAIL_FORGET_TEMPLATE_NAME, ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Send email
        mailSender.send(mimeMessage);
        log.info("Email sent successfully to: " + recipientEmail);
    }

    /*
     * Send HTML mail with inline image
     */
    public void sendMailWithInline(
            final String recipientName, final String recipientEmail, final String imageResourceName,
            final byte[] imageBytes, final String imageContentType, final Locale locale)
            throws MessagingException {

        // Prepare the evaluation context
        final Context ctx = new Context(locale);
        ctx.setVariable("name", recipientName);
        ctx.setVariable("subscriptionDate", new Date());
        ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
        ctx.setVariable("imageResourceName", imageResourceName); // so that we can reference it from HTML

        // Prepare message using a Spring helper
        final MimeMessage mimeMessage = this.mailSender.createMimeMessage();
        final MimeMessageHelper message
                = new MimeMessageHelper(mimeMessage, true /* multipart */, "UTF-8");
        message.setSubject("Example HTML email with inline image");
        message.setFrom("thymeleaf@example.com");
        message.setTo(recipientEmail);

        // Create the HTML body using Thymeleaf
        final String htmlContent = this.htmlTemplateEngine.process(EMAIL_INLINEIMAGE_TEMPLATE_NAME, ctx);
        message.setText(htmlContent, true /* isHtml */);

        // Add the inline image, referenced from the HTML code as "cid:${imageResourceName}"
        final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        message.addInline(imageResourceName, imageSource, imageContentType);

        // Send mail
        this.mailSender.send(mimeMessage);
    }

}
