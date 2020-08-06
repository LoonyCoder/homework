package com.lagou.util;

import com.lagou.service.UserServiceImpl;

public class ResponseHandler implements Runnable{
    @Override
    public void run() {

            while (true){
                int i = TimeCounter.getAtomicInteger().decrementAndGet();
                Object o = ZkClientUtil.getZkClient().readData(UserServiceImpl.getServerPath());
                System.out.println("响应时间："+o);
                if (i == 0){
                    System.out.println("响应时间清零");
                    ZkClientUtil.getZkClient().writeData(UserServiceImpl.getServerPath(),0l);
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

    }
}
