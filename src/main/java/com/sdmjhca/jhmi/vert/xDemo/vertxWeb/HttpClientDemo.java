package com.sdmjhca.jhmi.vert.xDemo.vertxWeb;

import io.netty.handler.ssl.OpenSsl;
import io.netty.handler.ssl.SslProvider;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;

/**
 * @author JHMI on 2017/10/20.
 */
public class HttpClientDemo {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        WebClient webClient = WebClient.create(vertx);

        ReqDto reqDto = new ReqDto();
        reqDto.setName("sdmjhca");
        reqDto.setType("sexy boy");

        JsonObject json = JsonObject.mapFrom(reqDto);


        webClient.get(8080,"localhost","/get/client/")
                .as(BodyCodec.json(RespDto.class))//转换为Java POJO
                //将响应信息写入缓存，避免一次写入内存
                //.as(BodyCodec.pipe(WriteStream))
                .sendJson(json,res ->{
                    if(res.succeeded()){
                        System.out.println("发送HTTP请求成功");
                        System.out.println("请求参数="+json.toString());

                        /*JsonObject jsonObject = res.result().body();

                        RespDto respDto = jsonObject.mapTo(RespDto.class);*/

                        RespDto respDto = res.result().body();
                        System.out.println("收到服务器响应报文="+respDto.toString());
                    }
                });


        /*webClient.post(8080,"localhost","/get/client/")
                .sendJsonObject(json,res ->{
                    if(res.succeeded()){
                        System.out.println("发送HTTP post请求成功");
                        System.out.println("请求参数="+json.toString());
                        System.out.println("收到服务器响应报文="+res.result().body());
                    }
                });*/


        final SslProvider sslProvider;

        if (OpenSsl.isAvailable()) {
            if (OpenSsl.isAlpnSupported()) {
                sslProvider = SslProvider.OPENSSL;
            } else {
                sslProvider = SslProvider.JDK;
            }
        } else {
            sslProvider = SslProvider.JDK;
        }
    }
}
