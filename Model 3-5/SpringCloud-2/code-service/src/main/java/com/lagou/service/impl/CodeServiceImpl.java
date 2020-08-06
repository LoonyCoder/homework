package com.lagou.service.impl;

import com.lagou.dao.AutoCodeRepository;
import com.lagou.edu.service.CodeService;
import com.lagou.edu.service.SendMailServie;
import com.lagou.pojo.AuthCode;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.Date;

@Service
public class CodeServiceImpl implements CodeService {


    @Autowired
    AutoCodeRepository autoCodeRepository;

    @Reference
    SendMailServie sendMailServie;

    long effectiveTime = 2 * 60 *1000;


    public boolean generateCode(String email) {

        int code = (int) ((Math.random() * 9 + 1) * 100000);
        AuthCode authCode = new AuthCode();
        authCode.setCode(String.valueOf(code));
        authCode.setEmail(email);

        authCode.setCreatetime(new Date(System.currentTimeMillis()));
        authCode.setExpiretime(new Date(System.currentTimeMillis()+effectiveTime));
        autoCodeRepository.save(authCode);
        boolean result = sendMailServie.sendMail(email,String.valueOf(code));
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
