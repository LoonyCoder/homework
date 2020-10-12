package com.lagou.cache;


import com.google.common.cache.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

@Service
public class GuavaService {


    @Autowired
    RedisService redisService;

    LoadingCache<String,Object> cache;

    public GuavaService(){
        System.out.println("=======================1===============");
        cache = CacheBuilder.newBuilder()
                // 最大3个       //同时支持CPU核数线程写缓存
                .maximumSize(30).concurrencyLevel(Runtime.getRuntime().availableProcessors()).refreshAfterWrite(10, TimeUnit.SECONDS)
                .recordStats().removalListener(new RemovalListener<Object, Object>() {
                    public void onRemoval(RemovalNotification<Object, Object> notification){
                        System.out.println("removal: "+notification.getKey()+":"+notification.getCause());
                    }
                })
                .build(
                        new CacheLoader<String, Object>() {
                            @Override
                            public List load(String s) throws Exception {

                                return redisService.getHotJob();
                            }
                        }
                );

    }


    public List getHotJobs() throws Exception{
        List value= (List) cache.get("HOTJOBS", new Callable() {
            @Override
            public Object call() throws Exception {
                Object v = redisService.getHotJob();
                cache.put("HOTJOBS",redisService.getHotJob());
                return v;
            }
        });
        return value;
    }
}
