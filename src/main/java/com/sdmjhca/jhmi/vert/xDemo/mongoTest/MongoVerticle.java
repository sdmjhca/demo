package com.sdmjhca.jhmi.vert.xDemo.mongoTest;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;

/**
 * @author JHMI on 2017/10/23.
 */
public class MongoVerticle extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        //设置mongo client的连接属性
        JsonObject config  = config().getJsonObject("mongo");
        /*config.put("host","localhost");
        config.put("host","localhost");*/
        MongoClient mongoClient = MongoClient.createShared(vertx,config);
        EventBus eb = vertx.eventBus();

        System.out.println("---"+MongoVerticle.class.getName());
        eb.consumer(MongoVerticle.class.getName(),message -> {
            //获取需要操作的文档
            JsonObject document = (JsonObject) message.body();
            mongoClient.insert("books",document,res->{
                //如果不给消息发送者回执，mongomain消息发送者会按照超时没有收到响应处理
                message.reply(res.result());
                if(res.succeeded()){
                    System.out.println("操作数据库成功");
                    String id = res.result();
                    System.out.println("插入数据库的ID="+id);
                }else{
                    System.out.println("插入数据失败="+res.cause());
                }
            });

            JsonObject query = new JsonObject().put("title", "The Hobbit");

            JsonObject update = new JsonObject();
            update.put("title","The Hobbit update by jhmi");
            JsonObject updateJ = new JsonObject().put("$set",update);
            //执行更新操作
            mongoClient.update("books",query,updateJ,res->{
                message.reply(res.result());
                if(res.succeeded()){
                    System.out.println("update 数据 成功"+res.result());
                }else{
                    System.out.println("update 数据库失败"+res.cause());
                }
            });
        });
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
