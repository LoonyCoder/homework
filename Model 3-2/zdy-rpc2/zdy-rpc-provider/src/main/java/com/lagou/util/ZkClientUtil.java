package com.lagou.util;

import org.I0Itec.zkclient.ZkClient;

public class ZkClientUtil {

    public  static ZkClient zkClient;

    public static ZkClient getZkClient() {
        return zkClient;
    }

    public static void setZkClient(ZkClient zkClient) {
        ZkClientUtil.zkClient = zkClient;
    }
}
