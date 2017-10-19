package com.sdmjhca.jhmi.vert.xDemo.tcpServerAndClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;


/**
 * @author JHMI on 2017/10/19.
 */
public class TcpVerticle extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        System.out.println("tcp verticle 开始注册");
        EventBus eb = vertx.eventBus();
        eb.consumer(TcpVerticle.class.getName(),msg->{
            MultiMap headers = msg.headers();
            String body = (String) msg.body();
            System.out.println("headers="+headers+",body="+body);

            System.out.println("verticle 接受到TCP server发送的handlerid="+body);
            Buffer buffer = Buffer.buffer("我是通过verticle发送消息0");
            //通过verticle发送消息给 TCP CLIENT 1
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            eb.send(body,buffer);
        });
    }

}
