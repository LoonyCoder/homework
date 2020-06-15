package com.lagou.filter;

import com.lagou.util.CounterMap;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;




@Activate(group = {CommonConstants.CONSUMER})
public class TPMonitorFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        Long start= System.currentTimeMillis();

        try{

            return invoker.invoke(invocation);

        }finally {
            Long duration = System.currentTimeMillis() -start;
            String method = invocation.getMethodName();
            if(!CounterMap.getIndexMap().containsKey(method)){
                CounterMap.initMap(method);
            }
            CounterMap.getIndexMap().get(method).add(duration);
        }




    }
}
