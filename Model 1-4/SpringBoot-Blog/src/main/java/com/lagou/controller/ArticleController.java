package com.lagou.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.lagou.mapper.ArticleMapper;
import com.lagou.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ArticleController {


    @Autowired
    private ArticleMapper articleMapper;

    @RequestMapping("/list")
    public String selectAll(Model model,@RequestParam(required=true,defaultValue="1")Integer pageNum){
        //启动分页插件（pageNum为当前页码，3为每页记录条数）
        PageHelper.startPage(pageNum,2);

        List<Article> articles = articleMapper.selectAll();

        PageInfo<Article> pageInfo=new PageInfo<Article>(articles);
        //将数据放进model中
        model.addAttribute("pageInfo",pageInfo);
        model.addAttribute("requestPreUrl","/list");
        model.addAttribute("articles",articles);
        return "client/index";
    }
}
