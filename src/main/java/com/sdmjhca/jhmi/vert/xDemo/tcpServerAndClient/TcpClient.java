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
}
