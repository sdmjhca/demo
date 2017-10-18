package com.sdmjhca.jhmi.vert.xDemo.tcpServerAndClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

/**
 * @author JHMI on 2017/10/12.
 */
public class TcpServer extends AbstractVerticle{

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        NetServer netServer = vertx.createNetServer();

        //监听连接建立后的处理
        netServer.connectHandler(netSocket -> {
            System.out.println("socket localadd="+netSocket.localAddress().toString());
            netSocket.handler(buffer -> {
                String msg = buffer.toString("UTF-8");
                System.out.println("监听接收到的消息="+msg);
                //netSocket.write("我已收到你的消息="+msg);
            }).exceptionHandler(throwable -> {
                System.out.println("监听到发生异常");
                throwable.printStackTrace();
            }).closeHandler(req->{
                System.out.println("监听到连接关闭");
                netServer.close(res->{
                    System.out.println("开始关闭TCP连接");
                    if(res.succeeded()){
                        System.out.println("成功关闭TCP链接");
                    }
                });
            });
        });

        /*netServer.close(res->{
            System.out.println("开始关闭TCP连接");
            if(res.succeeded()){
                System.out.println("成功关闭TCP链接");
            }
        });*/


        netServer.listen(8080,"localhost",req->{
            if(req.succeeded()){
                System.out.println("tcp服务器注册成功");
            }
        });

    }
}
