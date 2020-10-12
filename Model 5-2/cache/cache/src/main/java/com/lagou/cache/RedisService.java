package com.lagou.cache;

import com.alibaba.fastjson.JSONArray;
import com.lagou.dao.JobDao;
import com.lagou.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RedisService {

    @Autowired
    JobDao jobDao;

    JedisCluster jedisCluster;

    public RedisService(){
        System.out.println("=======================2===============");
        JedisPoolConfig config = new JedisPoolConfig();
        Set<HostAndPort> nodeList = new HashSet<>();
        nodeList.add(new redis.clients.jedis.HostAndPort("192.168.43.128", 7001));
        nodeList.add(new redis.clients.jedis.HostAndPort("192.168.43.128", 7002));
        nodeList.add(new redis.clients.jedis.HostAndPort("192.168.43.128", 7003));
        nodeList.add(new redis.clients.jedis.HostAndPort("192.168.43.128", 7004));

        jedisCluster = new JedisCluster(nodeList, 5000, config);
    }

    public List getHotJob(){


        List<String> result = jedisCluster.lrange("HOTJOB",0,-1L);
        if (result == null || result.size() ==0){
            System.out.println("===================here===================");
            List<Job> jobs = jobDao.selectAll();

            for (Job job : jobs){
                Object obj = JSONArray.toJSON(job);
                String json = obj.toString();
                jedisCluster.lpush("HOTJOB",json);

            }
            result= jedisCluster.lrange("HOTJOB",0,-1L);
            jedisCluster.expire("HOTJOB",10);
        }

        return result;
    };
}
