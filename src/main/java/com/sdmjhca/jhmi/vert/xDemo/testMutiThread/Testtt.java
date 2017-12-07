package com.sdmjhca.jhmi.vert.xDemo.testMutiThread;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author JHMI on 2017/11/23.
 */
public class Testtt {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions();
        options.setConfig(new JsonObject().put("config","1"));
        vertx.deployVerticle(ThreadVerticle.class.getName(),options);
    }
}
