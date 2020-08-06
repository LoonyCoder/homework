package com.lagou.filter;

import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;




@Activate(group = {CommonConstants.CONSUMER})
public class TransportIPFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        String ip = (String)invocation.getArguments()[1];
        RpcContext.getContext().setAttachment("ip",ip);
        System.out.println("Filter处理的ip:"+ ip);
        //RpcContext.getContext().getRemoteAddress();
        return invoker.invoke(invocation);
    }
}
