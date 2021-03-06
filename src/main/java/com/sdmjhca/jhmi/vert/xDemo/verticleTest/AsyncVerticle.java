package com.sdmjhca.jhmi.vert.xDemo.verticleTest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author JHMI on 2017/10/16.
 */
public class AsyncVerticle extends AbstractVerticle {

    private int i = 0;
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        System.out.println("异步执行 开始注册AsyncVerticle");
        startFuture.setHandler(req->{
            if(req.succeeded()){
                i = i+1;
                System.out.println("------------------startFuture 执行完成");
            }
        });
        //部署一个新的Verticle
        vertx.deployVerticle("com.sdmjhca.jhmi.vert.xDemo.verticleTest.FirstVerticle",req->{
            if(req.succeeded()){
                System.out.println("部署 下一个verticle成功---FirstVerticle");
                //startFuture.complete();
            }
        });
        Future<String> future = Future.future();
        future.setHandler(res->{
            if(res.succeeded()){
                System.out.println("future ------------------ done");
            }

        });
        vertx.deployVerticle("com.sdmjhca.jhmi.vert.xDemo.verticleTest.FirstVerticle",
                new DeploymentOptions().setConfig(new JsonObject().put("name","test st")),future.completer());
    }

    @Override
    public void stop(Future<Void> stopFuture) throws Exception {
        System.out.println("=============="+i);
        super.stop(stopFuture);
    }
}
