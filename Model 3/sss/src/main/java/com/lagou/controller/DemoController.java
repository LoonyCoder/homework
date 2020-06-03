package com.lagou.controller;

import com.lagou.pojo.Resume;
import com.lagou.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author loonycoder
 * @data 2020年04月17日10:36:46
 * @description
 */
@Controller
@RequestMapping("/demo")
public class DemoController {

    @Autowired
    private ResumeService resumeService;

    @RequestMapping("/findAll")
    public String findAll(Model module) {
        List<Resume> list = resumeService.findAll();
        module.addAttribute("resumeList",list);
        return "list";
    }

    @RequestMapping("/toEdit")
    public String toEdit(Long id,Model model) {
        Resume resume = resumeService.findById(id);
        model.addAttribute("resume",resume);
        return "info";
    }

    @RequestMapping("/save")
    public String save(Resume resume) {
        resumeService.save(resume);
        return "redirect:findAll";
    }

    @RequestMapping("/delete")
    public String delete(Long id) {
        resumeService.deleteById(id);
        return "redirect:findAll";
    }

    @RequestMapping("/toAdd")
    public String toAdd() {
        return "add";
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request,String username,String password) {
        if ("admin".equals(username) && "admin".equals(password)) {
            request.getSession().setAttribute("username","admin");
            return "redirect:findAll";
        }
        return "loginFail";
    }

    @RequestMapping("/toLogin")
    public String toLogin(HttpServletRequest request,String username,String password) {
        return "login";
    }

}
