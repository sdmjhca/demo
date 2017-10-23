package com.sdmjhca.jhmi.vert.xDemo.blockThreadTest;

import io.vertx.core.Vertx;

/**
 * @author JHMI on 2017/10/23.
 * 参考原文地址：https://www.baidu.com/home/news/data/newspage?source=pc&nid=3372996810810266041&n_type=1&p_from=3
 * 通过代码探测VERTX的阻塞线程模型
 * 输出结果：
 * timer1 =vert.x-worker-thread-1
timer1 =vert.x-worker-thread-2
timer2 =vert.x-worker-thread-0
timer1 =vert.x-worker-thread-3
timer1 =vert.x-worker-thread-4
timer2 =vert.x-worker-thread-0
 * timer1每次都是使用不同的线程：
 * timer1的线程sleep time < timer，在下一个定时周期开始时，阻塞的任务已经完成，
 * 从FIXEDTHREADPOOL线程池中重新获取线程，执行下一个定时任务
 *
 * timer2每次都是同一个线程，阻塞线程默认是顺序执行，下一个定时周期开始时，上一个的任务还未处理完成，
 * 这时新来的任务在阻塞队列里排队等待处理，上一个任务完成后，当前线程查看阻塞队列里的任务，如果有任务，
 * 则继续执行；没有任务则释放当前线程。下次收到任务重新获取线程。
 *
 */
public class WorkerThreadMain {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.setPeriodic(1000,req->{
            vertx.executeBlocking(handler->{
                try {

                    Thread.sleep(200);
                    System.out.println("timer1 ="+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },res->{});
        });

        vertx.setPeriodic(1000,req->{
            vertx.executeBlocking(hand->{
                try {
                    Thread.sleep(2000);
                    System.out.println("timer2 ="+Thread.currentThread().getName());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            },res->{});
        });
    }
}
