package com.lagou.sqlsession;

import com.lagou.config.XMLConfigBuilder;
import com.lagou.pojo.Configruation;
import org.dom4j.DocumentException;

import java.beans.PropertyVetoException;
import java.io.InputStream;

public class SqlSessionFactoryBuilder {
    public SqlSessionFactory build(InputStream in) throws DocumentException, PropertyVetoException {
        //1.使用dom4j解析配置文件，将解析出来的内容封装到Configruation中
        XMLConfigBuilder xmlConfigBuilder = new XMLConfigBuilder();
        Configruation configruation = xmlConfigBuilder.parseConfig(in);

        //2.创建SqlSessionFactory对象
        DefaultSqlSessionFactory sqlSessionFactory  = new DefaultSqlSessionFactory(configruation);

        return sqlSessionFactory;
    }
}
