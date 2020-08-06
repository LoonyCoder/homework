package com.lagou.filter;


import com.lagou.feign.UserServiceFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpCookie;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class TokenValidateFilter implements GlobalFilter, Ordered {

    @Autowired
    UserServiceFeignClient userServiceFeignClient;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String path= request.getPath().toString();
        if(path.startsWith("/api/user") || path.startsWith("/api/code") ){
            return chain.filter(exchange);
        }

        MultiValueMap<String, HttpCookie> cookies = request.getCookies();
        String data = "Request rejected！";
        DataBuffer wrap = response.bufferFactory().wrap(data.getBytes());
        String token =null;
        try {
           token = cookies.getFirst("token").getValue();
        }catch (Exception e){
            return response.writeWith(Mono.just(wrap));
        }
        if (token == null) {
            return response.writeWith(Mono.just(wrap));
        }

        String email = userServiceFeignClient.info(token);
        if( email == null){
            return response.writeWith(Mono.just(wrap));
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
