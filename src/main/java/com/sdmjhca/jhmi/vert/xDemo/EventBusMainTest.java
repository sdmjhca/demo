package com.sdmjhca.jhmi.vert.xDemo;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.MessageCodec;
import io.vertx.core.eventbus.impl.codecs.JsonObjectMessageCodec;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

import java.util.Iterator;
import java.util.Map;

/**
 * @author JHMI on 2017/10/12.
 * 每一个 Vert.x 实例都有一个单独的 Event Bus 实例
 * Event Bus可形成跨越多个服务器节点和多个浏览器的点对点的分布式消息系统。
 * Event Bus支持发布/订阅、点对点、请求/响应的消息通信方式。
 * Event Bus的API很简单。基本上只涉及注册处理器、撤销处理器和发送和发布消息。
 */
public class EventBusMainTest {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        //获取event bus
        EventBus eb = vertx.eventBus();


        //注册处理器，收到消息的后续处理
        //attr1 处理器注册地址 全限定类名，attr2 消息
        eb.consumer("com.sdmjhca.ca",message -> {
            MultiMap map = message.headers();//消息的头
            Iterator iterator = map.iterator();
            while (iterator.hasNext()){
                Map.Entry entry = (Map.Entry) iterator.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                System.out.println("消息的请求头: name="+name+",value="+value);
            }
            JsonObject json = (JsonObject) message.body();
            MyPOJO myPOJO = Json.decodeValue(json.toString(),MyPOJO.class);
            System.out.println("接收到的消息="+myPOJO.toString());

            message.reply(message.body());
        }).completionHandler(req->{
            if(req.succeeded()){//注册完成后通知
                System.out.println("处理器注册完成！");
            }
        });



        //使用vert.x提供 编解码器
        MessageCodec messageCodec1 = new JsonObjectMessageCodec();

        //发布消息，消息的发送者
        //attr1 处理器地址，attr2 消息内容
        //地址上注册过的所有处理器，都会收到消息
        //eb.publish("com.sdmjhca.ca","测试消息");
        //点对点发送消息
        //设置消息头
        DeliveryOptions deliveryOptions = new DeliveryOptions();
        deliveryOptions.addHeader("header","header-name");
        //如果超过30秒没有收到响应，则返回fail
        deliveryOptions.setSendTimeout(30000);//设置发送消息的超时时间/毫秒/默认30秒

        MyPOJO myPOJO = new MyPOJO();
        myPOJO.setName("sdmjhca");
        myPOJO.setSex("boy");
        eb.send("com.sdmjhca.ca",JsonObject.mapFrom(myPOJO),deliveryOptions,req->{
            if(req.succeeded()){
                System.out.println("收到接收者的回付消息="+req.result().body());
            }else{
                System.out.println("消息发送失败，超时没有回执！");
            }

        });


        //MyPOJO myPOJO = new MyPOJO();
        //myPOJO.setName("jhmi");
        //通过指定的消息编解码器，发送消息

        //eb.send("com.sdmjhca.ca",myPOJO,deliveryOptions);
        //eb.send("com.sdmjhca.ca","msgasdasd",deliveryOptions);
        //注销解码器
        //eb.unregisterCodec(messageCodec.name());


    }
}
