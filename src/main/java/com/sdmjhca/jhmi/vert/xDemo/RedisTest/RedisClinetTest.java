package com.sdmjhca.jhmi.vert.xDemo.RedisTest;

import io.vertx.core.Vertx;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * @author JHMI on 2017/12/1.
 */
public class RedisClinetTest {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        RedisOptions redisOptions = new RedisOptions();
        redisOptions.setHost("10.10.10.170");
        redisOptions.setPort(6379);
        redisOptions.setAuth("lbstest");
        RedisClient redisClient = RedisClient.create(vertx,redisOptions);

        /*redisClient.set("sdmjhca","sdmjhca1",res->{
           if(res.succeeded()){
               System.out.println("done");
           }else{
               res.cause().printStackTrace();
           }
        });*/

        /*redisClient.setex("sdmjhca-time",1000,"sdmjhca-value",res->{
            if(res.succeeded()){
                System.out.println("done");
                System.out.println(res.result());
            }else{
                res.cause().printStackTrace();
            }
        });*/

        redisClient.setnx("sdmjhca-time1","sdmjhca-value",res->{
            if(res.succeeded()){
                System.out.println("done");
                System.out.println(res.result());
            }else{
                res.cause().printStackTrace();
            }
        });

        redisClient.expire("sdmjhca-time1",1000,res->{
            if(res.succeeded()){
                System.out.println("done");
                System.out.println(res.result());
            }else{
                res.cause().printStackTrace();
            }
        });
    }
}
