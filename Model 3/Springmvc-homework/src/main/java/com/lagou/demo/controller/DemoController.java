package com.lagou.demo.controller;

import com.lagou.demo.service.IDemoService;
import com.lagou.edu.mvcframework.annotations.LgAutowired;
import com.lagou.edu.mvcframework.annotations.LgController;
import com.lagou.edu.mvcframework.annotations.LgRequestMapping;
import com.lagou.edu.mvcframework.annotations.LgSecurity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@LgController
@LgRequestMapping("/demo")
@LgSecurity({"zhangsan","lisi"})
public class DemoController {

    @LgAutowired
    private IDemoService demoService;


    /**
     * url:/demo/query
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LgRequestMapping("/query")
    @LgSecurity("zhangsan")
    public String query(HttpServletRequest request, HttpServletResponse response,String name){
        return  demoService.get(name);
    }



    /**
     * url:/demo/query
     * @param request
     * @param response
     * @param name
     * @return
     */
    @LgRequestMapping("/test")
    @LgSecurity({"lisi"})
    public String test(HttpServletRequest request, HttpServletResponse response,String name){
        return  demoService.get(name);
    }

}
