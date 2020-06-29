package com.lagou.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="mail-server")
public interface EmailFeignClient {

    @RequestMapping(value="/email/{email}/{code}",method= RequestMethod.GET)
    boolean sendMail(@PathVariable(value = "email") String email, @PathVariable(value = "code") String code);

}