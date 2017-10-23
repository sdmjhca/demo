package com.sdmjhca.jhmi.vert.xDemo.mongoTest;

import com.sdmjhca.jhmi.GetConfigJsonUtil;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * @author JHMI on 2017/10/23.
 */
public class MongoMain {


    public static void main(String[] args) throws IOException {

        Vertx vertx = Vertx.vertx();

        EventBus eb = vertx.eventBus();

        DeploymentOptions deploymentOptions = new DeploymentOptions();
        //获取config.json配置文件
        deploymentOptions.setConfig(GetConfigJsonUtil.getConfigJson());
        //注册verticle
        vertx.deployVerticle(MongoVerticle.class.getName(),deploymentOptions,res->{
            if(res.succeeded()){
                System.out.println("注册verticle成功");
                //设置需要新增的文档
                JsonObject json = new JsonObject();
                json.put("title", "The Hobbit");
                //59edabfcda7d022f94295ed8
                json.put("_id","1223");

                System.out.println("---"+MongoVerticle.class.getName());
                eb.send(MongoVerticle.class.getName(),json,res1->{
                    if(res1.succeeded()){
                        System.out.println("成功="+res1.result());
                    }else{
                        System.out.println("失败="+res1.cause());
                    }
                });
            }else{
                System.out.println("注册verticle失败");
            }
        });


    }
}
