package com.sdmjhca.jhmi.vert.xDemo.kotlin_coroutine;

import io.vertx.core.Vertx;

/**
 * @author JHMI on 2017/11/1.
 */
public class KotlinCoroutineTest {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(KotlinCoroutineVerticle.class.getName());
        System.out.println("main==="+Thread.currentThread().getName());
    }
}
