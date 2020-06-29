package com.lagou.service.impl;

import com.lagou.dao.TokenRepository;
import com.lagou.pojo.LagouToken;
import com.lagou.service.AuthTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;

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
