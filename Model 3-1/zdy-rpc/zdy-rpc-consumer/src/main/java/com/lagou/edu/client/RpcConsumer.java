package com.lagou.edu.client;

import com.lagou.edu.encoder.RpcEncoder;
import com.lagou.edu.protocol.RpcRequest;
import com.lagou.edu.serializer.JSONSerializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcConsumer {

    //创建一个线程池对象
    private static ExecutorService executor =
            Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static UserClinetHandler userClinetHandler;
    //1.创建一个代理对象

    /**
     *
     * @param serviceClass
//     * @param providerName  UserService#sayHello are you ok?
     * @return
     */
    public Object createProxy(final Class<?> serviceClass){
            //借助jdk动态代理生成代理对象
       return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //（1）调用初始化Netty客户端的方法
                if(userClinetHandler == null) {
                    initClinet();
                }

                //封装RpcRequest对象
                RpcRequest request = new RpcRequest();
                request.setClassName(serviceClass.getName());
                request.setMethodName(method.getName());
                request.setParameters(args);
                request.setRequestId(UUID.randomUUID().toString());
                // (2) 设置参数
//                userClinetHandler.setPara(providerName+args[0]);
                userClinetHandler.setRpcRequest(request);
                // (3) 去服务端请求数据
                return executor.submit(userClinetHandler).get();
            }
        });

    }
    //2.初始化netty客户端
    private static void initClinet() throws InterruptedException {
        userClinetHandler = new UserClinetHandler();
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group).channel(NioSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY,true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel sc) throws Exception {
                        ChannelPipeline pipeline = sc.pipeline();
                        pipeline.addLast(new RpcEncoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast(new StringDecoder());
                        pipeline.addLast(userClinetHandler);
                    }
                });

        //建立链接
        bootstrap.connect("127.0.0.1",8990).sync();
    }

}
