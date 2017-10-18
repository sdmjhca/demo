package com.sdmjhca.jhmi.vert.xDemo.vertxThreadTest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;

/**
 * @author JHMI on 2017/10/18.
 */
public class SecondVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req->{
            System.out.println(Thread.currentThread().getName()+"---收到HTTP请求1");
            EventBus eb = vertx.eventBus();
            eb.send("sdmjhca","msg",res->{
                System.out.println(Thread.currentThread().getName()+"---成功发送消息");
            });
        }).listen(8083);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
