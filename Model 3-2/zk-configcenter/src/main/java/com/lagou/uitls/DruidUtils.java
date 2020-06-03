package com.lagou.uitls;

import com.alibaba.druid.pool.DruidDataSource;
import com.lagou.listener.DataSourceChangeListener;
import org.I0Itec.zkclient.ZkClient;

public class DruidUtils {

    private DruidUtils(){
    }

    private static DruidDataSource druidDataSource;


    public static DruidDataSource getDruidDataSource() {
        return druidDataSource;
    }

    public static void setDruidDataSource(DruidDataSource druidDataSource) {
        DruidUtils.druidDataSource = druidDataSource;
    }

    public static DruidDataSource getInstance() {
        if (druidDataSource == null){
            druidDataSource = new DruidDataSource();
            ZkClient zkClient = ZkClientHandler.getZkClient();
            String driverClassName = zkClient.readData("/datasourceInfo/driver");
            String url = zkClient.readData("/datasourceInfo/url");
            String userName = zkClient.readData("/datasourceInfo/userName");
            String pwd = zkClient.readData("/datasourceInfo/pwd");

            druidDataSource.setDriverClassName(driverClassName);
            druidDataSource.setUrl(url);
            druidDataSource.setUsername(userName);
            druidDataSource.setPassword(pwd);

            zkClient.subscribeDataChanges("/datasourceInfo/url", new DataSourceChangeListener());
            zkClient.subscribeDataChanges("/datasourceInfo/userName", new DataSourceChangeListener());
            zkClient.subscribeDataChanges("/datasourceInfo/pwd", new DataSourceChangeListener());
        }

        return druidDataSource;
    }

}
