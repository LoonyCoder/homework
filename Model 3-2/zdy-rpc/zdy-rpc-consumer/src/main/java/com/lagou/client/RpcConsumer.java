package com.lagou.client;

import com.lagou.service.JSONSerializer;
import com.lagou.service.RpcEncoder;
import com.lagou.service.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.ZkClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RpcConsumer {

    //创建线程池对象
    private static ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    public static Map<String, UserClientHandler> clientHandlerMap =new HashMap<String,UserClientHandler>();


    private static Integer count=0 ;



    //1.创建一个代理对象
    public Object createProxy(final Class<?> serviceClass){
        //借助JDK动态代理生成代理对象
        return  Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class<?>[]{serviceClass}, new InvocationHandler() {
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //（1）调用初始化netty客户端的方法

                if(clientHandlerMap.size()==0){
                 initClient();
                }

                RpcRequest rpcRequest = new RpcRequest();

                Class<?>[] parameterTypes = method.getParameterTypes();
                rpcRequest.setMethodName(method.getName());
                rpcRequest.setClassName(serviceClass.getName());
                rpcRequest.setParameterTypes(parameterTypes);
                rpcRequest.setParameters(args);


                // 设置参数
                int length = clientHandlerMap.keySet().toArray().length;
                String key = (String) clientHandlerMap.keySet().toArray()[count % length];
                UserClientHandler userClientHandler = clientHandlerMap.get(key);
                userClientHandler.setRpcRequest(rpcRequest);

                // 去服务端请求数据
                count ++;
                return executor.submit(userClientHandler).get();
            }
        });
    }

    private synchronized static void establishConnection(List<String> hostport ){
        for(int i=0;i<hostport.size();i++){
            if(clientHandlerMap.containsKey(hostport.get(i))){
                continue;
            }
            String[] split = hostport.get(i).split(":");
            String host= split[0];
            Integer port = Integer.valueOf(split[1]);
            final UserClientHandler client = new UserClientHandler();
            EventLoopGroup group = new NioEventLoopGroup();
            client.setChannelName(hostport.get(i));
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY,true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {

                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast( new RpcEncoder(RpcRequest.class, new JSONSerializer()));
                            pipeline.addLast( new StringDecoder());
                            pipeline.addLast(client);

                        }
                    });

            try {
                bootstrap.connect(host,port).sync();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            clientHandlerMap.put(hostport.get(i), client);
        }
    }


    //2.初始化netty客户端
    public static  void initClient() throws InterruptedException {
        ZkClient zkClient = new ZkClient("127.0.0.1:2181");
        List<String> children = zkClient.getChildren("/rpc-provider");


        establishConnection(children);

        zkClient.subscribeChildChanges("/rpc-provider", new IZkChildListener() {
            @Override
            public void handleChildChange(String s, List<String> list) throws Exception {
                establishConnection(list);
            }
        });
    }
}
