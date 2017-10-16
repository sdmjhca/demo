package com.sdmjhca.jhmi.vert.xDemo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.http.HttpServerRequest;

/**
 * @author JHMI on 2017/10/11.
 */
public class FirstVerticle extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        System.out.println(Thread.currentThread().getName()+"---------verticle");
        //监听8082端口，接受UDP消息
        DatagramSocket datagramSocket = vertx.createDatagramSocket();
        datagramSocket.listen(8082,"localhost",req->{
            if(req.succeeded()){
                System.out.println("注册UDP服务监听8082端口成功");
                /*req.result().handler(datagramPacket -> {
                    System.out.println("接收到的消息为=-===="+datagramPacket.data());
                });*/
            }else{
                System.out.println("接受消息失败");
            }
        });
        datagramSocket.handler(datagramPacket -> {
            //注册处理器，用于后续收到消息的处理
            System.out.println("接收到消息，进行后续处理="+datagramPacket.data());
        });

        //收到一个HTTP请求的相应处理
        vertx.createHttpServer().requestHandler(
                new Handler<HttpServerRequest>() {
                    @Override
                    public void handle(HttpServerRequest httpServerRequest) {
                        System.out.println(Thread.currentThread().getName()+"收到HTTP请求");
                        httpServerRequest.response().putHeader("content-type","text/plain")
                                .end("Hello World!");
                        vertx.executeBlocking(future -> {
                            System.out.println(Thread.currentThread().getName()+"收到http请求进行处理");
                        },req->{
                            System.out.println(Thread.currentThread().getName()+"梳理结果="+req.result());
                        });
                    }
                }
        ).listen(8080);

        //获取配置JSON属性
        String jsonString = context.config().getString("name");
        System.out.println("--------------------获取配置JSON属性="+jsonString);
        vertx.createHttpServer().requestHandler(req->{
            req.response().putHeader("content-type","text/plain").end("hello world!!!");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).listen(8081);
    }
}
