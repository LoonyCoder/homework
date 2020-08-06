package com.lagou.bean;

import com.lagou.service.AService;
import com.lagou.service.BService;
import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.stereotype.Component;

@Component
public class ComsumerComponet {
    @Reference
    private HelloService  helloService;

    @Reference
    private BService bService;

    @Reference
    private AService aService;

    public String  sayHello(String name ){
        return  helloService.sayHello(name);
    }

    public String  aService(String arg ){
        return  aService.aservice(arg);
    }

    public String  bService(String arg ){
        return  bService.bservice(arg);
    }



}
