package com.lagou.mapper;

import com.lagou.pojo.Article;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface ArticleMapper {

    public List<Article> selectAll();
}
