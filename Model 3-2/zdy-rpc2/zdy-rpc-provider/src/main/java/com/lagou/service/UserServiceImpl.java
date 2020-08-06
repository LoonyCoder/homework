package com.lagou.service;

import com.lagou.handler.UserServerHandler;
import com.lagou.util.ZkClientUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import org.I0Itec.zkclient.ZkClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service()
public class UserServiceImpl implements UserService {


    static String  zkHost;

    @Value("${zk.host}")
    public void setZkHost(String zkHost) {
        UserServiceImpl.zkHost= zkHost;
    }

    static String  registerPath;
    static String  serverPath;

    public static String getServerPath() {
        return serverPath;
    }

    @Value("${zk.provider-path}")
    public void setregisterPath(String registerPath) {
        UserServiceImpl.registerPath= registerPath;
    }

    private  static int port;
    public String sayHello(String word) {
        ZkClientUtil.getZkClient().writeData(serverPath,String.valueOf(System.currentTimeMillis()));
        System.out.println("连接服务器端口："+ port +"成功！参数："+word);
        return "连接服务器端口："+ port +"成功！参数："+word;
    }

    //hostName:ip地址  port:端口号
    public static void startServer(String hostName,int port) throws InterruptedException {
        UserServiceImpl.port =port;
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast( new RpcDecoder(RpcRequest.class, new JSONSerializer()));
                       // pipeline.addLast( new RpcEncoder(RpcRequest.class, new JSONSerializer()));
                        pipeline.addLast( new StringEncoder());
                        pipeline.addLast(new UserServerHandler());

                    }
                });
        serverBootstrap.bind(hostName,port).sync();
        System.out.println("成功连接到zookeeper服务器，地址:"+zkHost);
        ZkClient zkClient = new ZkClient(zkHost+":2181");
        ZkClientUtil.setZkClient(zkClient);
        if(! zkClient.exists(registerPath)){
            zkClient.createPersistent(registerPath);
        }
        serverPath = registerPath+"/"+hostName+":"+port;
        zkClient.createEphemeral(serverPath,0l);

    }

}
