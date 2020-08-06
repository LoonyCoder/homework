package com.lagou.service.impl;

import com.lagou.dao.AutoCodeRepository;
import com.lagou.feign.EmailFeignClient;
import com.lagou.pojo.AuthCode;
import com.lagou.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class CodeServiceImpl implements CodeService {


    @Autowired
    AutoCodeRepository autoCodeRepository;

    @Autowired
    EmailFeignClient emailFeignClient;

    long effectiveTime = 2 * 60 *1000;


    public boolean generateCode(String email) {

        int code = (int) ((Math.random() * 9 + 1) * 100000);
        AuthCode authCode = new AuthCode();
        authCode.setCode(String.valueOf(code));
        authCode.setEmail(email);

        authCode.setCreatetime(new Date(System.currentTimeMillis()));
        authCode.setExpiretime(new Date(System.currentTimeMillis()+effectiveTime));
        autoCodeRepository.save(authCode);
        boolean result = emailFeignClient.sendMail(email,String.valueOf(code));
        if (! result )  return  false;


        System.out.println("生成验证码：" + code);
        return true;
    }

    @Override
    public int validate(String email, String code) {
        AuthCode authCode = autoCodeRepository.findLatestByEmail(email);
        if (authCode ==null ){
            return  1;
        }

        if (! authCode.getCode().equals(code)){
            return 1;  // 1: 错误
        }else if(authCode.getExpiretime().before(new Date(System.currentTimeMillis()))) {
            return 2 ; // 超时
        }
        return 0;
    }
}
