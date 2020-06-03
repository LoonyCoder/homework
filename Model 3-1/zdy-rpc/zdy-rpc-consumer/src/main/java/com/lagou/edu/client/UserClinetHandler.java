package com.lagou.edu.client;

import com.lagou.edu.protocol.RpcRequest;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.concurrent.EventExecutorGroup;

import java.util.concurrent.Callable;

public class UserClinetHandler extends ChannelInboundHandlerAdapter implements Callable {

    private ChannelHandlerContext context;
    private String result;
    private String para;

    private RpcRequest rpcRequest;

    public void channelActive(ChannelHandlerContext ctx) {
        context = ctx;
    }
    /**
     * 收到服务端数据，唤醒等待线程
     */
    public synchronized void channelRead(ChannelHandlerContext ctx, Object
            msg) {
        result = msg.toString();
        notify();
    }
    /**
     * 写出数据，开始等待唤醒
     */
    public synchronized Object call() throws InterruptedException {
        context.writeAndFlush(rpcRequest);
        wait();
        return result;
    }


    /**
     * 设置参数
     * @param para
     */
    void setPara(String para) {
        this.para = para;
    }

    public void setRpcRequest(RpcRequest rpcRequest) {
        this.rpcRequest = rpcRequest;
    }
}
