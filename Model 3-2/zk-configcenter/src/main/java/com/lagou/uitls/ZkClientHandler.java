package com.lagou.uitls;

import org.I0Itec.zkclient.ZkClient;

public class ZkClientHandler {

    static ZkClient zkClient ;
    private ZkClientHandler(){};

    public static ZkClient getZkClient() {
        if(zkClient == null){
            zkClient = new ZkClient("127.0.0.1:2181");
        }
        return zkClient;
    }
}
