package com.lagou.handler;

import com.lagou.service.RpcRequest;
import com.lagou.util.AppUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class UserServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        RpcRequest request = (RpcRequest)  msg;
        Object object = AppUtil.getObject(request.getClassName());
        Class aclass = null;
        try {
            aclass = Class.forName(request.getClassName());
            Method declaredMethod = aclass.getDeclaredMethod(request.getMethodName(),request.getParameterTypes());
            String  result = (String) declaredMethod.invoke(object, request.getParameters());
            ctx.writeAndFlush(result);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            ctx.writeAndFlush("fail");
        }


    }

}
