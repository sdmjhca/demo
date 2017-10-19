package com.sdmjhca.jhmi.vert.xDemo.tcpServerAndClient;

import io.vertx.core.Vertx;
import io.vertx.core.net.NetClient;
import io.vertx.core.net.NetSocket;

/**
 * @author JHMI on 2017/10/12.
 */
public class TcpClient {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        NetClient netClient = vertx.createNetClient();
        netClient.connect(8080,"localhost",netSocketAsyncResult -> {
           if(netSocketAsyncResult.succeeded()){
               System.out.println("客户端连接服务器成功！");
               NetSocket netSocket = netSocketAsyncResult.result();
               netSocket.write("hello iam  clinet!");

           }
        });
    }

    /**
     * 创建Tcp客户端
     * @param vertx
     */
    public static void tcpClient(Vertx vertx){
        NetClient netClient = vertx.createNetClient();
        netClient.connect(8080,"localhost",netSocketAsyncResult -> {
            if(netSocketAsyncResult.succeeded()){
                System.out.println("客户端1连接服务器成功1！");
                NetSocket netSocket = netSocketAsyncResult.result();
                netSocket.write("hello iam  clinet 1111!");

                //客户端通过与服务器间的通道接受消息
                netSocket.handler(buffer -> {
                    System.out.println("客户端1收到的服务器消息-----------"+buffer.toString());
                });
            }
        });
    }
}
