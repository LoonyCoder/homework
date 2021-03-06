package com.lagou;

import com.lagou.service.UserServiceImpl;
import com.lagou.util.ResponseHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ServerBootstrapNew {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(ServerBootstrapNew.class, args);
        UserServiceImpl.startServer("127.0.0.1",8991);
        ResponseHandler responseHandler = new ResponseHandler();
        responseHandler.run();
    }



}
