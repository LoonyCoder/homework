package com.lagou.bean;

import com.lagou.service.HelloService;
import com.lagou.service.ServiceTwo;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class ComsumerComponet {
    @Reference
    private HelloService  helloService;

    @Reference
    private ServiceTwo serviceTwo;

    public String  sayHello(String name , String extend){
        return  helloService.sayHello(name,extend);
    }

    public String  serviceTwo(String name , String extend){
        return  serviceTwo.doServiceTwo(name,extend);
    }

}
