package com.lagou.service;

public interface CodeService {

    boolean generateCode(String email);

    int validate(String email, String code);
}
