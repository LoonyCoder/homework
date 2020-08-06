package com.lagou.edu.handler;

import com.lagou.edu.protocol.RpcRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.springframework.beans.BeansException;
import org.springframework.cglib.core.ReflectUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

/**
 * RpcRequest协议的处理器
 */
@Component
public class RpcRequestHandler extends ChannelInboundHandlerAdapter implements ApplicationContextAware {

    private ApplicationContext context;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RpcRequest) {
            RpcRequest request = (RpcRequest) msg;
            Class clazz = Thread.currentThread().getContextClassLoader().loadClass(request.getClassName());
            Object bean = context.getBean(clazz);
            ReflectionUtils.invokeMethod(ReflectionUtils.findMethod(clazz, request.getMethodName(), request.getParameterTypes()), bean, request.getParameters());
            String result = "success";
            ctx.writeAndFlush(result);
            return;
        }
        ctx.fireChannelRead(msg);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
