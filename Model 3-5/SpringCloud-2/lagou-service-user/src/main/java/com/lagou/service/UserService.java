package com.lagou.service;

import javax.servlet.http.HttpServletResponse;

public interface UserService {

    Integer register(String email, String password, String code, HttpServletResponse response);

    boolean isRegistered(String email);

    boolean login(String email, String password, HttpServletResponse response);
}
