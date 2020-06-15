package com.lagou.controller;

import com.lagou.bean.ComsumerComponet;
import org.apache.yetus.audience.InterfaceAudience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
public class HelloController {

    @Autowired
    ComsumerComponet service;

    @RequestMapping("/dubbo")
    public String sayHi(HttpServletRequest request){
        String  hello = service.sayHello("dubbo",request.getRemoteAddr());
        String  serviceTwo = service.serviceTwo("dubbo",request.getRemoteAddr());
        return hello;

    }
}
