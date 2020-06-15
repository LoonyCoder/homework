package com.lagou.service.impl;

import com.lagou.service.BService;
import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Service;

import java.util.Random;

@Service
public class BServiceImpl implements BService {
    Random random = new Random();
    @Override
    public String bservice(String name) {

        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "b:"+name;
    }
}
