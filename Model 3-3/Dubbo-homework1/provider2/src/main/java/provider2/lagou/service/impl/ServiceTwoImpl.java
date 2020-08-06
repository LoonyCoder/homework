package provider2.lagou.service.impl;

import com.lagou.service.ServiceTwo;
import org.apache.dubbo.config.annotation.Service;
import org.apache.dubbo.rpc.RpcContext;

@Service
public class ServiceTwoImpl implements ServiceTwo {
    @Override
    public String doServiceTwo(String name, String extend) {
        String ip =(String) RpcContext.getContext().getAttachment("ip");
        System.out.println("Server1打印的客户端ip地址："+ip);
        return "Hello "+name + "!";
    }
}
