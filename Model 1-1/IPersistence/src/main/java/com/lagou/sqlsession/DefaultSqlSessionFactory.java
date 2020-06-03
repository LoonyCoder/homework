package com.lagou.sqlsession;

import com.lagou.pojo.Configruation;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configruation configruation;

    public DefaultSqlSessionFactory(Configruation configruation) {
        this.configruation = configruation;
    }

    @Override
    public SqlSession openSession() {

        return new DefaultSqlSession(configruation);
    }
}
