package com.simplerpa.cloudservice.utils;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;

public class EmailUtilSingleton {
    private static volatile EmailUtilSingleton instance;

    private final JavaMailSenderImpl javaMailSender;
    private static final String MAIL_FROM = "346904028@qq.com";

    private EmailUtilSingleton(){
        javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.qq.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("346904028@qq.com");
        javaMailSender.setPassword("zelsxaxylxpgcaed");
        javaMailSender.setDefaultEncoding("utf-8");
    }

    public static EmailUtilSingleton getInstance(){
        if (instance == null) {
            synchronized (EmailUtilSingleton.class) {
                if (instance == null) {
                    instance = new EmailUtilSingleton();
                }
            }
        }
        return instance;
    }

    public void sendMail(String to, String content){
        sendMail(to, content, "Simple RPA Notify(No Reply)");
    }

    public void sendMail(String to, String content, String subject){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(MAIL_FROM);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(content);
        javaMailSender.send(message);
    }

    public void sendMail(String to, String content, File[] files) throws MessagingException {
        try{
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(MAIL_FROM);
            helper.setTo(to);
            helper.setSubject("Simple RPA Notify(No Reply)");
            helper.setText(content);
            if(files != null && files.length > 0){
                for(File file : files){
                    helper.addAttachment(file.getName(), file);
                }
            }
            javaMailSender.send(message);
        }catch (MessagingException e){
            e.printStackTrace();
            throw e;
        }
    }
}
