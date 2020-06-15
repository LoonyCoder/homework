package com.lagou.service.impl;

import com.lagou.service.AService;
import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Service;

import java.util.Random;

@Service
public class AServiceImpl implements AService {
    Random random = new Random();

    @Override
    public String aservice(String name) {
        try {
            Thread.sleep(random.nextInt(100));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return "Hello "+name + "!";
    }
}
