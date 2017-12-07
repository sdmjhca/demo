package com.sdmjhca.jhmi.vert.xDemo.testMutiThread;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;

/**
 * @author JHMI on 2017/11/23.
 */
public class ThreadVerticle extends AbstractVerticle{
    public String test = "";

    private JsonObject jsonObject = new JsonObject();
    @Override
    public void start() throws Exception {
        System.out.println(vertx);
        String config = config().getString("config");
        if("1".equals(config)){
            test = "thread 1";
            jsonObject.put("code","1");

            Thread.sleep(3000);
        }else{
            test = "thread 2";
            jsonObject.put("code","2");
            jsonObject.put("msg","2");
        }
        System.out.println(Thread.currentThread().getName()+"---"+test);

        System.out.println(jsonObject.toString());
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        DeploymentOptions options = new DeploymentOptions();
        options.setConfig(new JsonObject().put("config","1"));
        vertx.deployVerticle(ThreadVerticle.class.getName(),options);

        options.setConfig(new JsonObject().put("config","2"));
        vertx.deployVerticle(ThreadVerticle.class.getName(),options);
    }
}
