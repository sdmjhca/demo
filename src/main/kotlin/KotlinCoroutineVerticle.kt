package com.sdmjhca.jhmi.vert.xDemo.kotlin_coroutine

import io.vertx.core.AsyncResult
import io.vertx.core.Handler
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.http.HttpServer
import io.vertx.kotlin.coroutines.*
import kotlinx.coroutines.experimental.launch

/**
 * @author JHMI on 2017/11/2.
 * 使用CoroutineVerticle 需要vertx版本是3.5.0
 *
 * awaitEvent方法暂停执行协程，直到定时器返回结果给handler
 */
class KotlinCoroutineVerticle : CoroutineVerticle(){

    suspend override fun start() {

        //test one-shot event
        awaitEventExample()
        //test awaitResult
        awaitResultExample()
        //test try catch
        awaitResultException()
        //test strams of event
        streamEventsExample()
        //test launch run method in coroutine
        testLaunchHandlerWithCoroutine()
    }

    suspend override fun stop() {
        super.stop()
    }

    /**
      *awaitEvent方法暂停执行协程，直到定时器返回结果给handler
     */
    suspend fun awaitResultExample(){
        val eb = vertx.eventBus()
        //注册消息的监听
        val message : MessageConsumer<String> = eb.consumer<String>("com.sdmjhca")
        message.handler { message ->
            println("msg received ： ${message.body()}"+System.currentTimeMillis())
            message.reply("地瓜，你好，收到")
        }

        //开始发送消息，等待响应
        val res = awaitResult<Message<String>> { handler ->
            println(Thread.currentThread().name+"开始发送消息")
            //eb.send("com.sdmjhca","土豆，你好，收到请回复")
            //调用eb发送消息，并将发送的结果，付给handler
            eb.send("com.sdmjhca","土豆，你好，收到请回复",handler)
        }
        println("收到土豆的回复 : ${res.body()}"+System.currentTimeMillis())
    }

    /**
     * test awaitResult try/catch
     */
    suspend fun awaitResultException(){
        val eb = vertx.eventBus()
        //注册消息的监听
        val message : MessageConsumer<String> = eb.consumer<String>("com.sdmjhca.try")
        message.handler { message ->
            println("msg received ： ${message.body()}"+System.currentTimeMillis())
            message.fail(0,"i'm done")
        }

        //开始发送消息，等待响应
        try {
            val res = awaitResult<Message<String>> { handler ->
                println(Thread.currentThread().name+"开始发送消息")
                //eb.send("com.sdmjhca","土豆，你好，收到请回复")
                //调用eb发送消息，并将发送的结果，付给handler
                eb.send("com.sdmjhca.try","土豆，你好，收到请回复",handler)
            }
            println("收到土豆的回复 : ${res.body()}"+System.currentTimeMillis())
        }catch (e:Exception){
            //e.printStackTrace()
            println("receive from 土豆: ${e.message}")
        }

    }

    /**
     * process one-shot event
     * Calling launch allows running Vert.x handlers on a coroutine
     * with no suspend
     */
     fun awaitEventExample(){
        launch(vertx.dispatcher()){
            var timerId = awaitEvent<Long> { h: Handler<Long> ->
                vertx.setTimer(1000,h)
                println("定时器将在1秒后执行="+System.currentTimeMillis())
            }

            println("定时器执行完成Event fired from timer with id $timerId -------------"+System.currentTimeMillis())
        }
    }

    /**
     * test streams of event
     */
    suspend fun streamEventsExample(){
        val eb  = vertx.eventBus()
        val address = "stream.events"

        //2 receive 15 messages
        val msgConsumer = eb.consumer<String>(address)
        //3 create channelHandler to receive msg
        val adapter = vertx.receiveChannelHandler<Message<String>>()
        //4 msgConsumer 绑定 handler
        msgConsumer.handler(adapter)

        //1 send 15 messages
        for (i in 1..15){
            eb.send(address,"test stream events num[$i]")
        }

        // 5 receive msgs
        for (i in 1..15){
            //接受Message
            val msg = adapter.receive()
            println("receive msg = ${msg.body()}")
        }
    }

    /**
     * test fun with launch allows fun run in coroutine
     */
    fun testLaunchHandlerWithCoroutine(){
        //vertx.createHttpServer().requestHandler { param:Handler<HttpRequest> }
        val httpserver = vertx.createHttpServer().requestHandler {
            launch (vertx.dispatcher()){
                //coroutine allow do something asyn , when thing is done then syn return
                val timerid = awaitEvent<Long> {
                    vertx.setTimer(2000,it)
                }
                it.response().end("nice to meet you $timerid")
            }
        }.listen(8080)
    }
}
