package com.lagou;

import com.lagou.bean.ComsumerComponet;
import com.lagou.util.CounterThread;
import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AnnotationConsumerMain  {
    public static void main(String[] args) throws  Exception {
        System.out.println("-------------");
        AnnotationConfigApplicationContext   context = new AnnotationConfigApplicationContext(ConsumerConfiguration.class);
        context.start();
        // 获取消费者组件

        //CounterThread

        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(5);

        scheduledThreadPool.scheduleAtFixedRate(new CounterThread(),5,5, TimeUnit.SECONDS);

        ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
        while(true){
            fixedThreadPool.submit(new Runnable() {
                @Override
                public void run() {
                    ComsumerComponet  service = context.getBean(ComsumerComponet.class);
                    service.sayHello("dubbo");
                    service.aService("a");
                    service.bService("b");
                }
            });
            Thread.sleep(20);
        }


    }
    @Configuration
    @PropertySource("classpath:/dubbo-consumer.properties")
    @ComponentScan(basePackages = "com.lagou.bean")
    @EnableDubbo
    static  class  ConsumerConfiguration{

    }

}
