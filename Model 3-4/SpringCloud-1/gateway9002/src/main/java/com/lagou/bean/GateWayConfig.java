package com.lagou.bean;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class GateWayConfig {

    @Value("${ip_limit_min}")
    String  limit_min;

    @Value("${ip_limit_count}")
    String limit_count;


    public String getLimit_min() {
        return limit_min;
    }

    public void setLimit_min(String limit_min) {
        this.limit_min = limit_min;
    }

    public String getLimit_count() {
        return limit_count;
    }

    public void setLimit_count(String limit_count) {
        this.limit_count = limit_count;
    }
}
