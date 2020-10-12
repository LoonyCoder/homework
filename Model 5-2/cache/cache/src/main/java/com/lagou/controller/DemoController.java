package com.lagou.controller;

import com.lagou.cache.GuavaService;
import com.lagou.cache.RedisService;
import com.lagou.dao.JobDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DemoController {

    @Autowired
    GuavaService guavaService;

    @RequestMapping(value = "/cache",method= RequestMethod.GET)
    public List handleLogin() throws Exception {
        return guavaService.getHotJobs();
    }


}
