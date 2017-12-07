package com.sdmjhca.jhmi.vert.xDemo.tcpServerAndClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.net.NetServer;
import io.vertx.core.net.NetServerOptions;

/**
 * @author JHMI on 2017/10/12.
 */
public class TcpServer extends AbstractVerticle{

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        NetServerOptions netServerOptions = new NetServerOptions();
        //This determines if a connection will timeout and be closed if no data is received within the timeout.
        netServerOptions.setIdleTimeout(10);//设置连接的闲置时间1s,1s后如果收不到客户端消息自动关闭连接

        NetServer netServer = vertx.createNetServer(netServerOptions);

        //监听连接建立后的处理
        netServer.connectHandler(netSocket -> {
            System.out.println("socket localadd="+netSocket.localAddress().toString());
            netSocket.handler(buffer -> {
                String msg = buffer.toString("UTF-8");
                System.out.println("监听接收到的消息="+msg+"---"+System.currentTimeMillis());
                //netSocket.write("我已收到你的消息="+msg);
            }).exceptionHandler(throwable -> {
                System.out.println("监听到发生异常");
                throwable.printStackTrace();
            }).closeHandler(req->{
                System.out.println("监听到连接关闭---"+System.currentTimeMillis());
                /*netServer.close(res->{
                    System.out.println("开始关闭TCP连接");
                    if(res.succeeded()){
                        System.out.println("成功关闭TCP链接");
                    }
                });*/
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

    /**
     * 创建tcp服务
     * @param vertx
     */
    public static void tcpServer(Vertx vertx){
        NetServerOptions netServerOptions = new NetServerOptions();
        //This determines if a connection will timeout and be closed if no data is received within the timeout.
        netServerOptions.setIdleTimeout(0);//设置连接的闲置时间1s,1s后如果收不到客户端消息自动关闭连接

        NetServer netServer = vertx.createNetServer(netServerOptions);

        EventBus eb = vertx.eventBus();
        //监听连接建立后的处理
        netServer.connectHandler(netSocket -> {
            String handlerid = netSocket.writeHandlerID();
            System.out.println("socket localadd="+netSocket.localAddress().toString());

            //连接建立后，向通道写入 handlerid
            netSocket.write(handlerid);

            //服务端通过netSocket接受消息
            netSocket.handler(buffer -> {
                String msg = buffer.toString("UTF-8");
                System.out.println("服务器监听接收到的消息="+msg+"---"+System.currentTimeMillis());

                if(msg.contains("1")){
                    Buffer buffer1 = Buffer.buffer("tcp server 通过socket.write发送消息到客户端111 MSG A");
                    netSocket.write(buffer1);
                    //如果是客户端1，则调用TcpVerticle,进行消息处理
                    //需要把handlerid传给verticle，通过verticle进行下一步的消息分发
                    eb.send(TcpVerticle.class.getName(),handlerid);
                }else{
                    Buffer buffer1 = Buffer.buffer("tcp server 通过socket.write发送消息到客户端222 MSG A");
                    netSocket.write(buffer1);
                }

                Buffer buffer1 = Buffer.buffer("tcp server 通过eb发送消息到客户端 MSG A");
                //服务器向socket通道当中写入数据,和socket.write有异曲同工之妙
                eb.send(handlerid,buffer1);//在Verticle中可能没有socket实例，可以通过eb找到handlerid写入数据


            }).exceptionHandler(throwable -> {
                System.out.println("监听到发生异常");
                throwable.printStackTrace();
            }).closeHandler(req->{
                System.out.println("监听到连接关闭---"+System.currentTimeMillis());
            });
        });


        netServer.listen(8080,"localhost",req->{
            if(req.succeeded()){
                System.out.println("tcp服务器注册成功");
            }
        });

    }
}
