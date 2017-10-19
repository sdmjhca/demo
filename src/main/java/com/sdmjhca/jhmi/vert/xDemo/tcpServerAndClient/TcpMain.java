package com.sdmjhca.jhmi.vert.xDemo.tcpServerAndClient;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;

/**
 * @author JHMI on 2017/10/19.
 */
public class TcpMain {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();

        //初始化TCP服务端
        TcpServer.tcpServer(vertx);
        //初始化TCP客户端
        TcpClient.tcpClient(vertx);
        TcpCliet2.tcpClient(vertx);

        //并行部署verticle
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        vertx.deployVerticle(TcpVerticle.class.getName(),res->{
            if(res.succeeded()){
                System.out.println("tcp verticle注册成功");
            }
        });
    }
}
