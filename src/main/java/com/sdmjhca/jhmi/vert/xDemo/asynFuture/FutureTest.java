package com.sdmjhca.jhmi.vert.xDemo.asynFuture;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.function.Function;

/**
 * @author JHMI on 2017/10/14.
 * vert.x异步协调 可以协调多个异步操作的执行结果
 * future 代码详解
 */
public class FutureTest {
    public static void main(String[] args) {
        //1/create a future
        //创建尚未完成的future，并在return之前将结果传递给handeler。
        //泛型<JsonObject>指定处理完成后 返回指定类型
        Future<JsonObject> future = Future.future();
        //2/set a handler for the result.
        //If the future has already been completed it will be called immediately.
        // Otherwise it will be called when the future is completed.
        //sethandler跟setcomplete没有先后顺序的要求，比方说：你先标记为完成了，后设置了处理器同样会被调用
        future.setHandler(req->{
            //通过req获取处理结果
            if(req.succeeded()){
                System.out.println("处理成功，处理结果="+req.result());
            }else{
                System.out.println("处理失败="+req.cause().getMessage());
            }
        });

        //3/判断future是否完成
        if(future.isComplete()){
            System.out.println("future 已经完成");
        }else{
            System.out.println("future 未完成");
        }

        //4/标记future为已完成，完成后会调用handler
        future.complete(new JsonObject().put("test","test消息已经完成"));//参数设置返回结果
        //5/标记为已完成，结果是fail，如果已经完成了，不能再次标记为已完成
        if(!future.isComplete()){
            future.fail("卧槽失败了");
        }

        //6/尝试执行complete，如果已经完成，返回false
        if(future.tryComplete()){
            System.out.println("成功try执行完成");
        }else{
            System.out.println("try失败，因为已经完成");
        }

        //7/指定下一个执行的future，顺序执行
        Future<String> nextFuture = Future.future();
        nextFuture.setHandler(req->{
            if(req.succeeded()){
                System.out.println("next future success");
            }else {
                System.out.println("next future fail");
            }
        });
        Future<String> nnFuture = Future.future();
        nnFuture.setHandler(req->{
            nnFuture.handle(req);
            System.out.println("nnfuture = "+req.result());
        });
        Future reqFuture = future.compose(req->{
            System.out.println("顺序执行="+req.toString());
            //nextFuture.complete();
            nnFuture.complete("nn 先执行");
        },nextFuture);



        reqFuture.tryComplete("what the next");




    }
}
