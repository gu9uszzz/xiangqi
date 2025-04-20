package com.example.springboot.controller;


import com.example.springboot.common.Result;
import com.example.springboot.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class EmailController {

    @Autowired
    private JavaMailSender sender;

    private SimpleMailMessage mail = new SimpleMailMessage();

    private String subject = "CHESS验证码";

    private String text = "你的验证码是cnaofnwag";

    private String from = "1021074244@qq.com";


    @PostMapping("/email")
    public void email(@RequestBody User user) {
        mail.setSubject(subject);
        mail.setText(text);
        mail.setTo(user.getEmail());
        mail.setFrom(from);
        sender.send(mail);

    }
}
