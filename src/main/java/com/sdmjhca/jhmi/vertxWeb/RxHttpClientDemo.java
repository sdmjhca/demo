package com.sdmjhca.jhmi.vertxWeb;

import io.vertx.rxjava.core.Vertx;
import io.vertx.rxjava.core.buffer.Buffer;
import io.vertx.rxjava.ext.web.client.HttpResponse;
import io.vertx.rxjava.ext.web.client.WebClient;
import rx.Single;

/**
 * @author JHMI on 2017/10/20.
 */
public class RxHttpClientDemo {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        WebClient webClient = WebClient.create(vertx);
        /**
         * 通过rxsend发送请求，并发布请求结果消息
         */
        Single<HttpResponse<Buffer>> single = webClient.get(8080,"localhost","/get/client/")
        .rxSend();

        /**
         * 订阅请求的结果消息
         */
        single.subscribe(httpresponse->{
            System.out.println("请求服务端结果="+httpresponse.body());
        },throwable -> {
            System.out.println("检测到请求服务异常");
            throwable.printStackTrace();
        });
    }
}
