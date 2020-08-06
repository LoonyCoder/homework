package com.lagou.sqlsession;

import com.lagou.pojo.Configruation;
import com.lagou.pojo.MappedStatement;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;

public interface Executor {

    public <E> List<E> query(Configruation configruation, MappedStatement mappedStatement,Object... params) throws Exception;

    public Object update(Configruation configruation, MappedStatement mappedStatement,Object... params) throws Exception;

}
