package com.lagou.edu.client;

import com.lagou.edu.service.IUserService;

public class ClientBootStrap {
    public static final String providerName = "UserService#sayHello#";
    public static void main(String[] args) throws InterruptedException {
        RpcConsumer rpcConsumer = new RpcConsumer();
        IUserService proxy = (IUserService) rpcConsumer.createProxy(IUserService.class);
        while (true){
            Thread.sleep(2000);
            System.out.println(proxy.sayHello("are you ok?"));
        }
    }
}
