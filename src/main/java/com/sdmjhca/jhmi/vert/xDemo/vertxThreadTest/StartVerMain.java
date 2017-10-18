package com.sdmjhca.jhmi.vert.xDemo.vertxThreadTest;

import io.vertx.core.Vertx;

/**
 * @author JHMI on 2017/10/18.
 */
public class StartVerMain {
    static final Vertx vertx = Vertx.vertx();
    public static void main(String[] args) {

        vertx.deployVerticle(SecondVerticle.class.getName());
        System.out.println(ThirdVerticle.class.getName());
        vertx.deployVerticle(ThirdVerticle.class.getName());
    }
}
