package com.lagou.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.nio.file.FileAlreadyExistsException;

/**
 * @author loonycoder
 * @data 2020年04月17日10:32:15
 * @description 自定义登录拦截器
 */

public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.equals("/sss/demo/toLogin") || uri.equals("/sss/demo/login")) {
            return true;
        }
        HttpSession session = request.getSession();
        Object username = session.getAttribute("username");
        if (username == null) {
            response.sendRedirect("/sss/demo/toLogin");
            return false;
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
