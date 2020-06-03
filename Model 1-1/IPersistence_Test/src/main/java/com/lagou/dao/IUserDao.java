package com.lagou.dao;

import com.lagou.pojo.User;

import java.util.List;

public interface IUserDao {
    //查询所有用户
    public List<User> selectAll();

//    根据条件进行用户查询
    public User selectByCondition(User user);

    public int insert(User user);

    public int update(User user);

    public int delete(User user);


}
