package com.sdmjhca.jhmi.vert.xDemo.vertxThreadTest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;

/**
 * @author JHMI on 2017/10/18.
 */
public class SecondVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        System.out.println("执行start");
        vertx.createHttpServer().requestHandler(req->{
            System.out.println(Thread.currentThread().getName()+"---收到HTTP请求1");
            EventBus eb = vertx.eventBus();
            Future<Message<String>> future = Future.future();
            future.setHandler(res->{
                System.out.println(Thread.currentThread().getName()+"----成功发送消息with future");
            });
            eb.send("sdmjhca","msg",res->{
                System.out.println(Thread.currentThread().getName()+"---成功发送消息no future");
            });
            eb.send("sdmjhca","msg future",future.completer());
        }).listen(8083);
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        System.out.println("执行startFuture");
        super.start(startFuture);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
