package com.lagou.sqlsession;

import com.lagou.pojo.Configruation;
import com.lagou.pojo.MappedStatement;

import java.lang.reflect.*;
import java.util.List;
import java.util.Map;

public class DefaultSqlSession implements SqlSession {

    private Configruation configruation;

    public DefaultSqlSession(Configruation configruation) {
        this.configruation = configruation;
    }

    @Override
    public <E> List<E> selectList(String statementId, Object... params) throws Exception {
       //将要完成对simpleExecutor里的query方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configruation.getMappedStatementMap().get(statementId);
        List<Object> list = simpleExecutor.query(configruation, mappedStatement, params);
        return (List<E>) list;
    }

    @Override
    public <T> T selectOne(String statementId, Object... params) throws Exception {
        List<Object> objects = selectList(statementId, params);
        if(objects.size() == 1){
            return (T) objects.get(0);
        }else{
            throw new RuntimeException("查询结果为空或不唯一！");
        }

    }
    @Override
    public Object update(String statementId, Object... params) throws Exception {
        //将要完成对simpleExecutor里的update方法的调用
        SimpleExecutor simpleExecutor = new SimpleExecutor();
        MappedStatement mappedStatement = configruation.getMappedStatementMap().get(statementId);
        return simpleExecutor.update(configruation, mappedStatement, params);

    }


    @Override
    public <T> T getMapper(Class<?> clazz) {

        // jdk动态代理生产dao接口的代理实现类对象
        Object proxyInstance = Proxy.newProxyInstance(DefaultSqlSession.class.getClassLoader(), new Class[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Map<String, MappedStatement> mappedStatementMap = configruation.getMappedStatementMap();
                String methodName = method.getName();
                String className = method.getDeclaringClass().getName();
                String statementId = className+"."+methodName;
                MappedStatement mappedStatement = mappedStatementMap.get(statementId);
                String sql = mappedStatement.getSql();
                if(sql.startsWith("select")){
                    // 获取被调用方法的返回值类型
                    Type genericReturnType = method.getGenericReturnType();
                    // 判断是否进行了 泛型类型参数化
                    if(genericReturnType instanceof ParameterizedType){
                        List<Object> objects = selectList(statementId, args);
                        return objects;
                    }else{
                        return selectOne(statementId,args);
                    }
                }else if(sql.startsWith("insert")){
                    return update(statementId,args);
                }else if(sql.startsWith("update")){
                    return update(statementId,args);
                }else{
                    return update(statementId,args);
                }





            }
        });

        return (T) proxyInstance;
    }




}
