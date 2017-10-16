package com.sdmjhca.jhmi.vert.xDemo.verticleTest;

import io.vertx.core.Vertx;

/**
 * @author JHMI on 2017/10/16.
 */
public class AsyncMain {
    private static String deploymentId = "";
    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(AsyncVerticle.class.getName(),req->{
            if(req.succeeded()){
                deploymentId = req.result();
                System.out.println("部署AsyncVerticle成功,id="+deploymentId);
            }
        });

        while ("".equals(deploymentId)){
            Thread.sleep(1000);
            System.out.println("休息一下");
        }

        System.out.println("休息结束");
        vertx.undeploy(deploymentId,res->{
            if(res.succeeded()){
                System.out.println("注销成功");
            }
        });
    }
}
