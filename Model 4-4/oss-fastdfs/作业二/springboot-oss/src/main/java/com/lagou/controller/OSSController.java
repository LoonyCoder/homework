package com.lagou.controller;

import com.lagou.bean.OSSResult;
import com.lagou.service.FileUpLoadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping("/oss")
public class OSSController {
    @Autowired
    private FileUpLoadService  fileUpLoadService;
    @PostMapping("/upload")
    @ResponseBody
    public OSSResult upload(@RequestParam("file") MultipartFile  multipartFile){
        return  fileUpLoadService.upload(multipartFile);
    }

    @GetMapping("/download")
    @ResponseBody
    public String download(@RequestParam("fileName") String  fileName,HttpServletResponse response) throws IOException {
        fileUpLoadService.download(fileName,response);
        return "done";
    }

    @PostMapping("/delete")
    @ResponseBody
    public OSSResult delete(@RequestParam("fileName") String  fileName ){
        return  fileUpLoadService.delete(fileName);
    }
}
