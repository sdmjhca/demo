package com.sdmjhca.jhmi.vert.xDemo.circuitBreaker;

import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;

/**
 * @author JHMI on 2017/10/31.
 * vertx 断路器的使用
 * 添加依赖vertx-circuit-breaker
 */
public class CircuitBreakerTest {
    public static void main(String[] args) throws InterruptedException {
        Vertx vertx = Vertx.vertx();

        vertx.deployVerticle(CircuitBreakerVerticle.class.getName());
        vertx.deployVerticle(HystrixDashBoard.class.getName());

        EventBus eb = vertx.eventBus();
        /*Thread.sleep(1000);
        for(int i=0;i<10;i++){
            eb.send("com.sdmjhca","cir ",res->{
               if(res.succeeded()){
                   System.out.println("发送成功");
               }else{
                   System.out.println("发送失败");
               }
            });
        }*/
    }
}
