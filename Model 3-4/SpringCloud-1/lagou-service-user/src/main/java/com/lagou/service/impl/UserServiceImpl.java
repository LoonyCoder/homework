package com.lagou.service.impl;

import com.lagou.dao.TokenRepository;
import com.lagou.dao.UserRepository;
import com.lagou.feign.CodeServiceFeignLient;
import com.lagou.pojo.LagouToken;
import com.lagou.pojo.LagouUser;
import com.lagou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    CodeServiceFeignLient codeServiceFeignLient;

    @Override
    public Integer register(String email, String password, String code, HttpServletResponse response) {


        int result = codeServiceFeignLient.validate(email,code);
        if( result != 0 ) return  result;

        String token = UUID.randomUUID().toString();  // 模拟token
        LagouToken lagouToken = new LagouToken();
        lagouToken.setEmail(email);
        lagouToken.setToken(token);

        LagouUser lagouUser = new LagouUser();
        lagouUser.setEmail(email);
        lagouUser.setPassword(password);

        userRepository.save(lagouUser);

        tokenRepository.save(lagouToken);

        Cookie cookie = new Cookie("token",token);
        cookie.setPath("/");
        response.addCookie(cookie);

        Cookie cookie2 = new Cookie("email",email);
        cookie2.setPath("/");
        response.addCookie(cookie2);

        return 0;
    }

    @Override
    public boolean isRegistered(String email) {
        LagouToken lagouToken = tokenRepository.findByEmail(email);
        if(lagouToken == null){
            return false;
        }
        return true ;
    }

    @Override
    public boolean login(String email, String password, HttpServletResponse response) {
        LagouUser user = userRepository.findByEmailAndPwd(email, password);
        if (user == null) return  false;

        String token = UUID.randomUUID().toString();  // 模拟token
        Cookie cookie = new Cookie("token",token);
        cookie.setPath("/");
        response.addCookie(cookie);

        Cookie cookie2 = new Cookie("email",email);
        cookie2.setPath("/");
        response.addCookie(cookie2);

        return  true;
    }
}
