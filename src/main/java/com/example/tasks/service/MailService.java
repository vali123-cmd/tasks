package com.example.tasks.service;


import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import com.resend.services.emails.model.CreateEmailResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.resend.*;


@Service
public class MailService {


    String supportEmail;
    private Resend resend;

    public MailService(@Value("${resend.api-key}") String resendApiKey, @Value("${resend.from-email}") String supportEmail) {
        this.resend = new Resend(resendApiKey);
        this.supportEmail = supportEmail;
    }

    public void sendEmail(String to, String subject, String html) throws ResendException {
        CreateEmailOptions sendEmailRequest = CreateEmailOptions.builder()
                .from(supportEmail)
                .to(to)
                .subject(subject)
                .html(html)
                .build();

        CreateEmailResponse response = resend.emails().send(sendEmailRequest);
        System.out.println(response);


    }


}
