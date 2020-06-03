package com.lagou;

import com.lagou.uitls.ZkClientHandler;
import org.I0Itec.zkclient.ZkClient;

public class ModifyDataSourceConfig {

    public static void main(String[] args) {
        ZkClient zkClient = ZkClientHandler.getZkClient();
        zkClient.writeData("/datasourceInfo/url","jdbc:mysql://localhost:3306/zk2");
        zkClient.writeData("/datasourceInfo/driver","com.mysql.jdbc.Driver");
        zkClient.writeData("/datasourceInfo/userName","root");
        zkClient.writeData("/datasourceInfo/pwd","root");
    }
}
