package com.lagou.config;

import com.lagou.io.Resources;
import com.lagou.pojo.Configruation;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.PropertyVetoException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

public class XMLConfigBuilder {

    private Configruation configruation;

    public XMLConfigBuilder() {
        this.configruation = new Configruation();
    }

    /**
     * 使用dom4j将配置文件进行解析，封装Configuration
     * @param in
     * @return
     */
    public Configruation parseConfig(InputStream in) throws DocumentException, PropertyVetoException {

        //解析数据源信息
        Document document = new SAXReader().read(in);
        Element rootElement = document.getRootElement();
        List<Element> list = rootElement.selectNodes("//property");
        Properties properties = new Properties();
        for (Element element : list) {
            String name = element.attributeValue("name");
            String value = element.attributeValue("value");
            properties.setProperty(name,value);
        }

        ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
        comboPooledDataSource.setDriverClass(properties.getProperty("driverClass"));
        comboPooledDataSource.setJdbcUrl(properties.getProperty("jdbcUrl"));
        comboPooledDataSource.setUser(properties.getProperty("username"));
        comboPooledDataSource.setPassword(properties.getProperty("password"));

        configruation.setDataSource(comboPooledDataSource);


        //解析mapper
        List<Element> mapperList = rootElement.selectNodes("//mapper");
        for (Element element : mapperList) {
            String mapperPath = element.attributeValue("resource");
            InputStream resourcesAsStream = Resources.getResourcesAsStream(mapperPath);
            XMLMapperBuilder xmlMapperBuilder = new XMLMapperBuilder(configruation);
            xmlMapperBuilder.parse(resourcesAsStream);
        }
        return configruation;
    }
}
