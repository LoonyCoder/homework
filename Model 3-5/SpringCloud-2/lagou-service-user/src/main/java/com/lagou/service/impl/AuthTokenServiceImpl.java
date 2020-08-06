package com.lagou.service.impl;

import com.lagou.dao.TokenRepository;
import com.lagou.edu.service.AuthTokenService;
import com.lagou.pojo.LagouToken;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class AuthTokenServiceImpl implements AuthTokenService {


    @Autowired
    TokenRepository tokenRepository;


    @Override
    public String getMailByToken(String token){
        LagouToken authToken = tokenRepository.findByToken(token);
        if(authToken == null ){
            return null;
        }else {
            return authToken.getEmail();
        }
    }

}
