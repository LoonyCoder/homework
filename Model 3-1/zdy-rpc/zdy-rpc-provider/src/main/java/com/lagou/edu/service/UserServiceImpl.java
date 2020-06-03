package com.lagou.edu.service;

import com.lagou.edu.decoder.RpcDecoder;
import com.lagou.edu.handler.RpcRequestHandler;
import com.lagou.edu.handler.UserServerHandler;
import com.lagou.edu.protocol.RpcRequest;
import com.lagou.edu.serializer.JSONSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class UserServiceImpl implements IUserService{

    @Autowired
    private RpcRequestHandler rpcRequestHandler;


    public String sayHello(String word) {
        System.out.println("调用成功--参数：" + word);
        return "调用成功--参数：" + word;
    }




    @PostConstruct
    public void startServer() throws InterruptedException {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(new StringEncoder());
                        pipeline.addLast(rpcRequestHandler);
                    }
                });
        serverBootstrap.bind("127.0.0.1",8990).sync();


    }

}
