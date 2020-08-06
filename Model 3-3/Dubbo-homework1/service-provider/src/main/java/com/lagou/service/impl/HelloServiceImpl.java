package com.lagou.service.impl;

import com.lagou.service.HelloService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Service
public class HelloServiceImpl   implements HelloService {


    @Override
    public String sayHello(String name, String arg) {
        String ip =(String) RpcContext.getContext().getAttachment("ip");
        System.out.println("Server1打印的客户端ip地址："+ip);
        return "SHello "+name + "!";
    }
}
