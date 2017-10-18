package com.sdmjhca.jhmi.vert.xDemo.vertxThreadTest;

import com.sdmjhca.jhmi.vert.xDemo.verticleTest.AsyncVerticle;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;

/**
 * @author JHMI on 2017/10/18.
 */
public class ThirdVerticle extends AsyncVerticle {
    @Override
    public void start(Future<Void> startFuture) throws Exception {
        System.out.println(Thread.currentThread().getName()+"---开始注册ThirdVerticle");
        vertx.createHttpServer().requestHandler(req->{
            System.out.println(Thread.currentThread().getName()+"---收到HTTP请求2");
        }).listen(8084);
        EventBus eb = vertx.eventBus();
        eb.consumer("sdmjhca",res->{
            System.out.println(Thread.currentThread().getName()+"---收到一条消息");
        });
    }

    @Override
    public void start() throws Exception {
        System.out.println(Thread.currentThread().getName()+"---开始注册ThirdVerticle");
        vertx.createHttpServer().requestHandler(req->{
            System.out.println(Thread.currentThread().getName()+"---收到HTTP请求2");
        }).listen(8084);
        EventBus eb = vertx.eventBus();
        eb.consumer("sdmjhca",res->{
            System.out.println(Thread.currentThread().getName()+"---收到一条消息");
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
