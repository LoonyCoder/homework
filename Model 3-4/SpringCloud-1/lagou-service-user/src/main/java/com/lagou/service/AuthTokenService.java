package com.lagou.service;

import javax.servlet.http.HttpServletResponse;

public interface AuthTokenService {



   String getMailByToken(String token);
}
