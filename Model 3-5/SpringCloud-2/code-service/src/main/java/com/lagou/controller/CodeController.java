package com.lagou.controller;


import com.lagou.edu.service.CodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/code")
public class CodeController {

    @Autowired
    CodeService codeService;

    @RequestMapping("/validate/{email}/{code}")
    public int validate(@PathVariable String email,@PathVariable String code){
        return codeService.validate(email,code);
    }

    @RequestMapping("/create/{email}")
    public boolean generate(@PathVariable String email){

        return codeService.generateCode(email);
    }

}
