package com.sdmjhca.jhmi.vert.xDemo.circuitBreaker;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.circuitbreaker.HystrixMetricHandler;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;

/**
 * @author JHMI on 2017/10/31.
 * 监控circuit breaker的状态
 */
public class HystrixDashBoard extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        CircuitBreakerOptions options = new CircuitBreakerOptions();
        options.setMaxFailures(3);//最大失败次数，如果超过该次数仍失败，则breaker open
        options.setFallbackOnFailure(true);//断路器open状态执行fallback回退逻辑
        options.setTimeout(10000);//设置请求的超时时间，超时则执行fallback
        options.setResetTimeout(10000);//如果断路器open，10秒后尝试变为half-open状态，根据下一次调用结果修改状态
        //options.setNotificationAddress("sdmjhca.circuit");//设置断路器状态改变后广播的消息地址
        options.setNotificationPeriod(2000);//设置断路器广播状态的周期
        //options.setMaxRetries(3);//设置失败前的最大重试次数，如果第一次请求失败，则重复发送，默认为0
        //options.setMetricsRollingWindow(10000);

        //1/创建断路器
        CircuitBreaker circuitBreaker = CircuitBreaker.create("sdmjhca-breaker",vertx,options);

        Router router = Router.router(vertx);
// Register the metric handler
        router.get("/metrics").handler(HystrixMetricHandler.create(vertx));

// Create the HTTP server using the router to dispatch the requests
        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8080);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
