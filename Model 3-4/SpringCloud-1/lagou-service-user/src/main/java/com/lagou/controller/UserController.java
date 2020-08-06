package com.lagou.controller;


import com.lagou.service.AuthTokenService;
import com.lagou.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthTokenService authTokenService;


    @RequestMapping("/login/{email}/{password}")
    public String login(@PathVariable String email, @PathVariable String password, HttpServletResponse response) {

        boolean result = userService.login(email, password, response);
        // try {
        if (result) {
            return "success";
            //response.sendRedirect("http://springcloud.com/static/welcome.html");
        } else {
            //response.sendRedirect("http://springcloud.com/static/login.html");
        }
        // } catch (IOException e) {
        //e.printStackTrace();
        //}
        return "登陆失败, 邮箱或密码不正确";
    }


    @RequestMapping("/isRegistered/{email}")
    public Boolean isRegistered(@PathVariable String email) {
        return userService.isRegistered(email);
    }


    @RequestMapping("/info/{token}")
    public String info(@PathVariable String token) {
        return authTokenService.getMailByToken(token);
    }

    @RequestMapping("/register/{email}/{password}/{code}")
    public Integer register(@PathVariable String email,
                            @PathVariable String password,
                            @PathVariable String code, HttpServletResponse response) {

        Integer register_res = userService.register(email, password, code, response);

        if (register_res == 0) {
            try {
                response.sendRedirect("http://springcloud.com/static/welcome.html");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return  register_res;


    }


}
