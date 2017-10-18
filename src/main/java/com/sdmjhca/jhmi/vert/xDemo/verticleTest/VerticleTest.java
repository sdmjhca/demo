package com.sdmjhca.jhmi.vert.xDemo.verticleTest;

import io.vertx.core.AbstractVerticle;

/**
 * @author JHMI on 2017/10/11.
 */
public class VerticleTest extends AbstractVerticle {
    /**
     * verticle 部署时调用
     * @throws Exception
     */
    @Override
    public void start() throws Exception {
        /**
         * 有些时候您的 Verticle 启动会耗费一些时间，
         * 您想要在这个过程做一些事，并且您做的这些事并不想等到Verticle部署完成过后再发生。
         * 如：您想在 start 方法中部署其他的 Verticle。
         */
        super.start();
    }

    /**
     * verticle 停止时调用
     * @throws Exception
     */
    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
