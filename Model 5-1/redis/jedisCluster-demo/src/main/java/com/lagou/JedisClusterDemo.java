package com.lagou;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashSet;
import java.util.Set;

public class JedisClusterDemo {
    public static void main(String[] args) {
        JedisPoolConfig config = new JedisPoolConfig();

        Set<HostAndPort> nodeList = new HashSet<>();
        nodeList.add(new HostAndPort("192.168.91.147", 7001));
        nodeList.add(new HostAndPort("192.168.91.147", 7002));
        nodeList.add(new HostAndPort("1192.168.91.147", 7003));
        nodeList.add(new HostAndPort("1192.168.91.147", 7004));
        nodeList.add(new HostAndPort("192.168.91.147", 7005));
        nodeList.add(new HostAndPort("192.168.91.147", 7006));
        nodeList.add(new HostAndPort("1192.168.91.147", 7007));
        nodeList.add(new HostAndPort("1192.168.91.147", 7008));
        JedisCluster jedisCluster = new JedisCluster(nodeList, 3000, config);

        //jedisCluster.set("name:1", "mike");
        String value = jedisCluster.get("name:2");
        System.out.println(value);
    }
}
