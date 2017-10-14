package com.sdmjhca.jhmi.nettydemo;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author JHMI on 2017/10/9.
 * 世界上最简单的协议DISCARD
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        StringBuffer sb = new StringBuffer();
        String s = byteBuf.toString(CharsetUtil.UTF_8);
        sb.append(s);
        System.out.println("收到的信息="+sb.toString());
        ctx.write(byteBuf);
        //byteBuf.release();//丢弃信息
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("------------捕获到最后一条消息--------");
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("------------------捕获到异常消息关闭channel------------");
        cause.printStackTrace();//打印异常信息
        ctx.close();//异常就关闭链接
    }
}
