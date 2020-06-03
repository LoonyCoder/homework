package com.lagou.test;

import com.lagou.dao.IUserDao;
import com.lagou.io.Resources;
import com.lagou.pojo.User;
import com.lagou.sqlsession.SqlSession;
import com.lagou.sqlsession.SqlSessionFactory;
import com.lagou.sqlsession.SqlSessionFactoryBuilder;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;

public class PersistenceTest {

    @Test
    public void test() throws Exception {

        InputStream resourcesAsStream = Resources.getResourcesAsStream("SqlMapConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(resourcesAsStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
//        List<User> users = sqlSession.selectList("user.selectAll");
        IUserDao userDao = sqlSession.getMapper(IUserDao.class);
        //测试查询所有用户
//        List<User> users = userDao.selectAll();
//
//        for (User user : users) {
//            System.out.println(user);
//        }
        //测试根据条件查询用户
//        User user1 = new User();
//        user1.setId(1);
//        user1.setUsername("tom");
//        User user = userDao.selectByCondition(user1);
//        System.out.println(user);
//        测试添加用户
//        User user1 = new User();
//        user1.setId(5);
//        user1.setUsername("abc");
//        userDao.insert(user1);

//        //测试修改用户
        User user2 = new User();
        user2.setId(1);
        user2.setUsername("dlrb");
        userDao.update(user2);
//
//        //测试删除用户
//        User user3 = new User();
//        user3.setId(0);
//        userDao.delete(user3);
    }
}
