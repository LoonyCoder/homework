package com.lagou.controller;

import com.alibaba.druid.pool.DruidPooledConnection;
import com.lagou.uitls.DruidUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.ResultSet;
import java.sql.SQLException;

@RestController
public class MyController {

    @RequestMapping(value = "/getInfo",method= RequestMethod.GET)
    public String test(){

        String result="";
        try {
            DruidPooledConnection connection = DruidUtils.getInstance().getConnection();
            ResultSet resultSet = connection.prepareStatement("select * from resume limit 1").executeQuery();
            resultSet.next();
            result = resultSet.getNString("name") + " | " + resultSet.getNString("phone") + " | " + resultSet.getNString("education");
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }
}
