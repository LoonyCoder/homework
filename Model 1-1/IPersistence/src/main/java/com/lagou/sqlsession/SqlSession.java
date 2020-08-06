package com.lagou.sqlsession;


import java.util.List;

public interface SqlSession {

    //查询所有
    public <E> List<E> selectList(String statementId,Object... params) throws Exception;


    //根据条件查询单个用户
    public <T> T selectOne(String statementId,Object... params) throws Exception;

    //为dao接口生成代理实现类
    public <T> T getMapper(Class<?> clazz);

    public Object update(String statementId, Object... params) throws Exception;

}
