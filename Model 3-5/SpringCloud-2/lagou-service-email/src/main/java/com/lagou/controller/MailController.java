package com.lagou.controller;

import com.lagou.edu.service.SendMailServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {

    /*@Autowired
    SendMailServie sendMailServie;

    @RequestMapping("/email/{email}/{code}")
    public boolean sendMail(@PathVariable String email, @PathVariable String code){
        return sendMailServie.sendMail(email,code);
    }*/

}
