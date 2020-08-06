package com.lagou.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(name="code-server")
public interface CodeServiceFeignLient {

    @RequestMapping(value="/code/validate/{email}/{code}",method= RequestMethod.GET)
    public int validate(@PathVariable(value = "email") String email,
                        @PathVariable(value = "code") String code);
}
