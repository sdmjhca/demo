package com.sdmjhca.jhmi.vert.xDemo;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.datagram.DatagramSocket;
import io.vertx.core.json.JsonObject;


/**
 * @author JHMI on 2017/10/11.、
 * 1、vertx是事件驱动，当你监听的事情发生时，以事件通知你
 * 2、非阻塞
 */
public class VerticleMain {
    public static void main(String[] args) {
        /*Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(FirstVerticle.class.getName());

        //触发一个周期性定时器
        vertx.setPeriodic(1000,req->{
            //每隔一秒处理一次
            System.out.println("timer fired"+"==="+req);
        });
        //一次性定时器
        vertx1.setTimer(1000,req->{
            System.out.println(" timer 是在一秒中之后执行的 --------"+System.currentTimeMillis());
        });
        System.out.println("我先执行的，timer会在一秒钟之后执行"+System.currentTimeMillis());
        //取消计时器
        vertx.cancelTimer(timerID);

        */

        System.out.println("当前线程="+Thread.currentThread().getName());
        //VertxOptions对象有很多配置，包括集群、高可用、池大小等。在Javadoc中描述了所有配置的细节。
        VertxOptions vertxOptions = new VertxOptions();
        //vertxOptions.setClustered(true);
        vertxOptions.setHAEnabled(true);
        vertxOptions.setWorkerPoolSize(50);
        //设置Event Loop的超时时间
        vertxOptions.setMaxEventLoopExecuteTime(1000);
        //设置阻塞线程的检查时间
        vertxOptions.setBlockedThreadCheckInterval(3000);


        Vertx vertx1 = Vertx.vertx(vertxOptions);

        //一次性定时器
        vertx1.setTimer(1000,req->{
            System.out.println(Thread.currentThread().getName()+" timer 是在一秒中之后执行的 --------"+System.currentTimeMillis());
        });
        System.out.println(Thread.currentThread().getName()+"我先执行的，timer会在一秒钟之后执行"+System.currentTimeMillis());

        //设置一个standard verticle
        //vertx1.deployVerticle(FirstVerticle.class.getName());
        //当verticle部署完成时，接受异步通知
        //attr1 Java类全限定类名 attr2 异步通知
        vertx1.deployVerticle(FirstVerticle.class.getName(),res->{
            if(res.succeeded()){
                System.out.println(Thread.currentThread().getName()+"first verticle deployed"+ res.result());
            }else{
                System.out.println("first verticle failed");
            }
        });


        //设置verticle的属性
        JsonObject jsonObject = new JsonObject().put("name","sdmjhca");
        DeploymentOptions deploymentOptions = new DeploymentOptions().setWorker(true)
                .setInstances(16)//指定您想要部署的 Verticle 实例的数量
                .setConfig(jsonObject);//设置josn格式的配置
        vertx1.deployVerticle(FirstVerticle.class.getName(),deploymentOptions);

        //设置一个worker verticle
        vertx1.executeBlocking(future -> {

            future.setHandler(req->{
                if(req.succeeded()){
                    System.out.println(Thread.currentThread().getName()+" worker thread" +
                            "阻塞操作执行完成，执行结果="+req.result());
                }else{
                    System.out.println("阻塞操作执行失败");
                }
            });

            String result = "we are done!";
            System.out.println(Thread.currentThread().getName()+"worker thread 执行一些阻塞的操作！");
            System.out.println("阻塞操作异步执行中...");
            //执行结束返回future
            future.complete(result);

        },res->{
            //获取执行的结果
            System.out.println(Thread.currentThread().getName()+" verticle thread execute result = "+res.result());
        });

        //vertx1.close();//推出vert。x环境
        //Context对象
        Context context = vertx1.getOrCreateContext();
        context.runOnContext(handler->{
            System.out.println(Thread.currentThread().getName()+"-----------context 异步执行 处理器");
        });
        System.out.println("isEventLoopContext="+context.isEventLoopContext());
        System.out.println("isMultiThreadedWorkerContext="+context.isMultiThreadedWorkerContext());
        System.out.println("isWorkerContext="+context.isWorkerContext());
        System.out.println("Context.isOnEventLoopThread()="+Context.isOnEventLoopThread());
        String name = context.config().getString("name");
        System.out.println(Thread.currentThread().getName()+"context-------------------获取verticle配置属性"+name);

        WorkerExecutor workerExecutor = vertx1.createSharedWorkerExecutor("my-test-poll");
        workerExecutor.executeBlocking(new Handler<Future<String>>() {
            @Override
            public void handle(Future<String> future) {
                System.out.println(Thread.currentThread().getName()+"-----------start block");
                future.complete("okokokok");
            }
        }, new Handler<AsyncResult<String>>() {
            @Override
            public void handle(AsyncResult<String> tAsyncResult) {
                System.out.println(Thread.currentThread().getName()+"-----------"+tAsyncResult.result());
            }
        });
        //设置集群，相关配置
        /*Vertx.clusteredVertx(vertxOptions, new Handler<AsyncResult<Vertx>>() {
            @Override
            public void handle(AsyncResult<Vertx> vertxAsyncResult) {
                if(vertxAsyncResult.succeeded()){
                    //集群设置成功，获取相应的VERTICLE
                    Vertx vertx2 = vertxAsyncResult.result();
                    System.out.println("集群设置成功");
                }else{
                    //集群生成失败，进行相关处理
                    System.out.println("集群失败");
                }
            }
        });

        Vertx.clusteredVertx(vertxOptions,req ->{
            if(req.succeeded()){
                Vertx vertx2 = req.result();
                //do something
            }else{
                //do error
            }
        }
        );*/

        //发送UDP数据报文
        DatagramSocket datagramSocket = vertx1.createDatagramSocket();
        Buffer buffer = Buffer.buffer("UDP test 数据");
        datagramSocket.send(buffer,8082,"localhost",req->{
            if (req.succeeded()){
                System.out.println("发送数据成功");
            }else {
                System.out.println("发送数据失败");

                System.out.println("commit from ide test");

            }
        });
    }
}
