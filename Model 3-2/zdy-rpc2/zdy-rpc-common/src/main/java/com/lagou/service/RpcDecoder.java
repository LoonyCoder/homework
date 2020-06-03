package com.lagou.service;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcDecoder  extends ByteToMessageDecoder {

    private Class<?> clazz;

    private Serializer serializer;

    public RpcDecoder(Class<RpcRequest> rpcRequestClass, JSONSerializer jsonSerializer) {
        this.clazz = rpcRequestClass;
        this.serializer =jsonSerializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byte[] len = new byte[4] ;
        byteBuf.readBytes(len);

        byte[] data = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(data);
        RpcRequest deserialize = (RpcRequest) serializer.deserialize(clazz, data);
        list.add(deserialize);

    }


    public  int toInt(byte[] bytes){
        int number = 0;
        for(int i = 0; i < 4 ; i++){
            number += bytes[3-i] << i*8;
        }
        return number;
    }





}
