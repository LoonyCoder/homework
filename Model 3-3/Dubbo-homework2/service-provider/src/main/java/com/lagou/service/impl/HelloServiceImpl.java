package com.lagou.service.impl;

import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Random;

@Service
public class HelloServiceImpl   implements HelloService {
    Random random = new Random();
    @Override
    public String sayHello(String name) {

        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Hello "+name + "!";
    }
}
