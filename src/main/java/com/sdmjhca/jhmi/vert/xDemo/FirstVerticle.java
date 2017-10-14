package com.sdmjhca.jhmi.vert.xDemo;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerRequest;

/**
 * @author JHMI on 2017/10/11.
 */
public class FirstVerticle extends AbstractVerticle{
    @Override
    public void start() throws Exception {
        //收到一个HTTP请求的相应处理
        vertx.createHttpServer().requestHandler(
                new Handler<HttpServerRequest>() {
                    @Override
                    public void handle(HttpServerRequest httpServerRequest) {
                        httpServerRequest.response().putHeader("content-type","text/plain")
                                .end("Hello World!");
                    }
                }
        ).listen(8080);

        //获取配置JSON属性
        String jsonString = context.config().getString("name");
        System.out.println("--------------------获取配置JSON属性="+jsonString);
        vertx.createHttpServer().requestHandler(req->{
            req.response().putHeader("content-type","text/plain").end("hello world!!!");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).listen(8081);
    }
}
