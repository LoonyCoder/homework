package com.lagou.listener;

import com.lagou.uitls.DruidUtils;
import org.I0Itec.zkclient.IZkDataListener;

public class DataSourceChangeListener implements IZkDataListener {
    @Override
    public void handleDataChange(String s, Object o) throws Exception {
        System.out.println("数据源已发生变更！");
        DruidUtils.setDruidDataSource(null);
        DruidUtils.getInstance();

    }

    @Override
    public void handleDataDeleted(String s) throws Exception {

    }
}
