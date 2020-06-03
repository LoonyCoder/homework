package com.lagou.edu.decoder;
import com.lagou.edu.serializer.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;
public class RpcDecoder extends ByteToMessageDecoder {

    private Class<?> clazz;

    private Serializer serializer;

    public RpcDecoder(Class<?> clazz, Serializer serializer) {
        this.clazz = clazz;
        this.serializer = serializer;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        byteBuf.markReaderIndex();
        // 读取消息长度
        int length = byteBuf.readInt();
        // 如果可以读的数据小于协议中的数据，需要重置一下读的下标，重新读取
        if (byteBuf.readableBytes() < length) {
            byteBuf.resetReaderIndex();
            return;
        }
        byte[] bytes = new byte[length];
        // 将读取的字节填充到空的数组中
        byteBuf.readBytes(bytes);
        // 将传输的字节数组转换为RpcRequest对象传递给下一个入站handler类处理
        list.add(serializer.deserialize(clazz, bytes));
    }
}
