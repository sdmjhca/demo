package com.sdmjhca.jhmi.vert.xDemo.UDPtest;

import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;

/**
 * @author JHMI on 2017/10/16.
 */
public class UdpSocketClient {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DatagramSocket client = vertx.createDatagramSocket();
        Buffer conent = Buffer.buffer(" udp send something...");
        client.send(conent,8082,"localhost",datagramSocketAsyncResult -> {
            if(datagramSocketAsyncResult.succeeded()){
                System.out.println("消息发送成功。。。");
                datagramSocketAsyncResult.result().close();
            }else{
                System.out.println("消息发送失败。。。");
            }
        });
    }
}
