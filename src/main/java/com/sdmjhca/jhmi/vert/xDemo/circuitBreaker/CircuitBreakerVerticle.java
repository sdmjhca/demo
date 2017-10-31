package com.sdmjhca.jhmi.vert.xDemo.circuitBreaker;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.circuitbreaker.HystrixMetricHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author JHMI on 2017/10/31.
 */
public class CircuitBreakerVerticle extends AbstractVerticle{

    private AtomicInteger count = new AtomicInteger();
    private CircuitBreaker circuitBreaker;
    @Override
    public void start() throws Exception {
        CircuitBreakerOptions options = new CircuitBreakerOptions();
        options.setMaxFailures(3);//最大失败次数，如果超过该次数仍失败，则breaker open
        options.setFallbackOnFailure(true);//断路器open状态执行fallback回退逻辑
        options.setTimeout(10000);//设置请求的超时时间，超时则执行fallback
        options.setResetTimeout(10000);//如果断路器open，10秒后尝试变为half-open状态，根据下一次调用结果修改状态
        options.setNotificationAddress("sdmjhca.circuit");//设置断路器状态改变后广播的消息地址，
        // 如果改变setNotificationAddress需要创建HystrixMetricHandler时告知
        options.setNotificationPeriod(2000);//设置断路器广播状态的周期
        //options.setMaxRetries(3);//设置失败前的最大重试次数，如果第一次请求失败，则重复发送，默认为0
        //options.setMetricsRollingWindow(10000);

        //1/创建断路器
        circuitBreaker = CircuitBreaker.create("my-breaker",vertx,options);

        WebClient webClient = WebClient.create(vertx);

        EventBus eb = vertx.eventBus();

        eb.consumer("com.sdmjhca",message -> {
            count.incrementAndGet();
            System.out.println("计数="+count);
            Future cir = circuitBreaker.execute(future -> {
                // 调用依赖服务
            webClient.post(8080,"localhost","/circuit")
            .send(res->{
                //System.out.println("请求返回状态="+res.result().statusCode());
                if(res.succeeded()){
                    future.tryComplete("HTTP SUCCESS");
                }else {
                    future.tryFail("HTTP ERROR");
                }
            });

            });
            cir.setHandler(res->{
                System.out.println("circuit 执行结果="+res.toString());
            });
            circuitBreaker.fallback(res->{
                res.printStackTrace();
                System.out.println("执行fallback方法");
                return "fail back";
            });
        });


        for(int i = 0;i<10;i++){
            circuitBreaker.execute(future -> {
                throw new RuntimeException(" exception expected ");
            });

            circuitBreaker.fallback(func->{
                System.out.println("执行fall back");
                return "fail back";
            });
        }

        eb.consumer("sdmjhca.circuit",message -> {
            System.out.println("sdmjhca.circuit="+message.body());
        });

        Router router = Router.router(vertx);
        // Register the metric handler
        router.get("/metrics").handler(HystrixMetricHandler.create(vertx,"sdmjhca.circuit"));

        router.get("/test/circuit").handler(this::testCir);

        // Create the HTTP server using the router to dispatch the requests
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8081);

    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public void testCir(RoutingContext rc){

        Future res = circuitBreaker.executeWithFallback(future -> {
            //future.tryFail("future 执行失败");
            future.tryComplete("future success");
        },fallback->"fallback exeucute");
        res.setHandler(result->{
            rc.response().end(res.result().toString());
        });
    }
}
