package com.sdmjhca.jhmi.vertxWeb;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.LanguageHeader;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.client.HttpRequest;

/**
 * @author JHMI on 2017/10/20.
 * web 的核心概念Router路由器
 */
public class HttpServerDemo {
    private static final Vertx vertx = Vertx.vertx();
    public static void main(String[] args) {
        //HttpServerDemo.specifyRoute(vertx);
        //HttpServerDemo.testHttpMethod(vertx);
        //HttpServerDemo.testMimeTypes(vertx);
        HttpServerDemo.testHttpClient(vertx);
    }

    /**
     * core 接受HTTP请求的写法
     * @param vertx
     */
    public static void coreCreateHttpSer(Vertx vertx){
        vertx.createHttpServer().requestHandler(req->{
            req.response().end("hello world");
        }).listen(8080);
    }

    public static void defaultRoute(Vertx vertx){
        //vertx core创建HTTP服务
        HttpServer httpServer = vertx.createHttpServer();
        //vertx web创建HTTP服务
        Router router = Router.router(vertx);
        //使用默认路由
        router.route().handler(routingContext -> {
            routingContext.response().end("hello vertx web");
        });

        httpServer.requestHandler(req->{
            router.accept(req);
        }).listen(8080);
    }

    /**
     * 指定/some/path/请求路由到本方法
     * @param vertx
     */
    public static void specifyRoute(Vertx vertx){
        //vertx core创建HTTP服务
        HttpServer httpServer = vertx.createHttpServer();
        //vertx web创建HTTP服务
        //创建公用路由器
        Router router = Router.router(vertx);
        /**
         * 路由可以通过order方法指定执行顺序
         */
        //创建路由1
        Route route1 = router.route("/some/path/").handler(routingContext -> {
            //routingContext.response().end("hello vertx web");
            //设置允许分块响应Output
            HttpServerResponse response = routingContext.response();
            response.setChunked(true);
            response.write("rout1---\n");

            /**
             * 设置定时器执行下一个路由
             */
            vertx.setTimer(5000,req->{
                routingContext.next();
            });

            //routingContext.vertx().setTimer(3000,req->routingContext.next());
            //routingContext.next();

        });

        //创建路由2
        Route route2 = router.route("/some/path/").handler(routingContext -> {
            System.out.println("rout2 收到");

            //routingContext.request().setExpectMultipart(true);

            routingContext.response().write("vertx route2---\n");

            routingContext.next();
        }).failureHandler(ctx->{
            System.out.println("rout2 发生异常");
            ctx.reroute("/some/excep/");
        });
        /**
         * blockingHandler 在同一个vertx实例中顺序执行，如果不需要按顺序执行，可以使用
         * attr2 set false
         */
        Route rout3 = router.route("/some/path/").blockingHandler(routingContext -> {
            System.out.println(" do something block....");
            routingContext.response().write("do block th").end("end");
        },false);

        httpServer.requestHandler(req->{
            router.accept(req);
            //router1.accept(req);
        }).listen(8080);
    }

    /**
     * 接受指定的HTTP请求
     * @param vertx
     */
    public static void testHttpMethod(Vertx vertx){

        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        //接受get请求
        Route route = router.route(HttpMethod.GET,"/get/test/")
                .method(HttpMethod.PUT).handler(ctx->{
            System.out.println("接收到HTTP请求");
            String s = ctx.request().getParam("type");
            System.out.println("收到的参数="+s);
            ctx.response().end("收到get请求");
        });
        //接受post请求，请求之参数占位符:
        Route route1 = router.post("/post/test/:type/").handler(ctx->{
            System.out.println("收到post请求");
            String s = ctx.request().getParam("type");
            System.out.println("post请求参数="+s);
            ctx.response().end("收到post请求");
        });

        httpServer.requestHandler(req->{
            router.accept(req);
        }).listen(8080);
    }
    /**
     * 基于请求媒体类型（MIME types）的路由
     */
    public static void testMimeTypes(Vertx vertx){
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        //接受get请求
        Route route = router.route("/get/language/").handler(ctx->{
            System.out.println("收到text/html请求");
            String s = ctx.request().getParam("type");

            /**
             * 获取客户端语言环境
             */
            for(LanguageHeader temp:ctx.acceptableLanguages()){
                System.out.println(temp.tag());
            }
            /**
             * 设置错误码
             */
            ctx.fail(400);

            ctx.put("ss","ss");
            String ss = ctx.get("ss");
            System.out.println("post请求参数="+s);
            ctx.response().end("收到text/html请求");
        }).failureHandler(ctx->{
            System.out.println("捕获到异常状态吗="+ctx.statusCode());
        });

        httpServer.requestHandler(req->{
            router.accept(req);
        }).listen(8080);
    }
    /**
     * 接受http client的请求
     */
    public static void testHttpClient(Vertx vertx){
        HttpServer httpServer = vertx.createHttpServer();

        Router router = Router.router(vertx);

        //接受get请求
        Route route = router.route("/get/client/").handler(ctx->{
            System.out.println("收到client请求");


            HttpServerRequest request = ctx.request();

            MultiMap multiMap = request.params();
            System.out.println("server 收到的请求="+multiMap.toString());

            /**
             * 用于接收xxx-formed格式参数
             */
            String s = ctx.request().getParam("type");
            String name = ctx.request().getParam("name");

            /**
             * 用于接受json 格式参数
             */
            request.bodyHandler(body->{
                System.out.println("收到客户端请求json参数="+body);
            });


            JsonObject json = new JsonObject();
            json.put("type",s).put("name",name);

            System.out.println("xxx-formed格式请求参数="+json.toString());
            ctx.response().end("收到client请求");
        }).failureHandler(ctx->{
            System.out.println("捕获到异常状态吗="+ctx.statusCode());
        });

        httpServer.requestHandler(req->{
            router.accept(req);
        }).listen(8080);
    }
}
