package com.lagou.dao;

import com.lagou.entity.Job;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface JobDao {

    public List<Job> selectAll();
}
