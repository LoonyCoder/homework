package com.lagou.filter;

import com.lagou.bean.GateWayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;


public class IPFilter implements GatewayFilter, Ordered {

    GateWayConfig gateWayConfig;


    private HashMap<String, LinkedBlockingQueue> map  = new HashMap<String, LinkedBlockingQueue> ();

    public IPFilter(GateWayConfig gateWayConfig){

        this.gateWayConfig =gateWayConfig;

        Executors.newScheduledThreadPool(1).scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                long min = Long.valueOf(gateWayConfig.getLimit_min());
                long duration = min*60*1000;
                System.out.println("limit_count :" + gateWayConfig.getLimit_count() +"  limit_min:"+gateWayConfig.getLimit_min());
                long time_ago = System.currentTimeMillis() - duration ;
                for (Map.Entry<String, LinkedBlockingQueue> entry : map.entrySet()) {
                    System.out.println("Key = " + entry.getKey() + "  size:"+entry.getValue().size ());
                    while (  entry.getValue().size () > 0 &&
                            (long)entry.getValue().peek() < time_ago){
                        entry.getValue().poll();
                    }

                }
            }
        }, 1,1,TimeUnit.SECONDS);
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpResponse response = exchange.getResponse();
        String hostAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
        if( !map.containsKey(hostAddress)){
            LinkedBlockingQueue queue = new LinkedBlockingQueue();
            map.put(hostAddress,queue);
        }

        long count = Long.valueOf(gateWayConfig.getLimit_count());
        if (map.get(hostAddress).size() >= count){
            System.out.println("超过"+gateWayConfig.getLimit_count()+"次， 拒绝");
            String data = "Request rejected!";
            DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
            return response.writeWith(Mono.just(wrap));
        }else {
            try {
                map.get(hostAddress).put(System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
