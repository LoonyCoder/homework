package com.lagou.config;

import com.lagou.bean.GateWayConfig;
import com.lagou.filter.IPFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RefreshScope
@Configuration
public class RegisterRequestConfig {

    @Autowired
    GateWayConfig gateWayConfig;


    @RefreshScope
    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder){
        //System.out.println("===========>>>>>>>>"+gateWayConfig.getLimit_min());

        return routeLocatorBuilder.routes().route(r -> r.path("/api/user/register/**")
                .filters(f -> f.stripPrefix(1)
                .filter(new IPFilter(gateWayConfig)))
                .uri("http://127.0.0.1:8084")
                .order(0)
                .id("register-service-router")
        ).build();
    }
}
