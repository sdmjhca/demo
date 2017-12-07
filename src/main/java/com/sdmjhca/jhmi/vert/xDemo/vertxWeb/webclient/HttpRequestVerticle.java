package com.sdmjhca.jhmi.vert.xDemo.vertxWeb.webclient;

import io.vertx.circuitbreaker.CircuitBreaker;
import io.vertx.circuitbreaker.CircuitBreakerOptions;
import io.vertx.core.*;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.client.WebClient;

/**
 * 发起HTTP请求操作
 * @author JHMI on 2017/11/10.
 */
public class HttpRequestVerticle extends AbstractVerticle {

    private static final Logger logger = LoggerFactory.getLogger(HttpRequestVerticle.class.getName());
    private WebClient webClient;
    private CircuitBreaker circuitBreaker;
    private EventBus eb;
    private JsonObject config;

    public interface HttpMethod{
        String GET_DRIVERNAME = "getDriverName";
    }

    public interface HttpResCode{
        String SUCCESS_CODE = "0000";
        String FAIL_CODE = "9999";
    }

    @Override
    public void start() throws Exception {
        eb = vertx.eventBus();
        webClient = WebClient.create(vertx);
        config = vertx.getOrCreateContext().config();
        circuitBreaker = this.createCircuitBreaker(vertx,vertx.getOrCreateContext().config());

        eb.<JsonObject>consumer(HttpRequestVerticle.class.getName(), req->{
            String action = req.headers().get("action");
            JsonObject reqJson = req.body();
            logger.info("HTTP vert收到请求，action = "+action+"req = "+req.toString());
            switch (action){
                case HttpMethod.GET_DRIVERNAME:
                    String driverTel = reqJson.getString("driverTel");
                    this.getDriverName(config,driverTel,res->{
                        req.reply(res.result());
                    });
                    break;
                default:
                    logger.error("请求action不能为空");
                    break;
            }

        });
    }

    /**
     * 根据司机手机号 获取 司机姓名
     * @param config 请求上下文
     * @param driverTel 手机号
     * @param handler 回调
     */
    public void getDriverName(JsonObject config, String driverTel, Handler<AsyncResult<JsonObject>> handler){
        JsonObject jsonObject = config.getJsonObject("driver.api");
        String host = jsonObject.getString("host");
        String path = jsonObject.getString("path");

        //封装请求URL
        StringBuilder reqString = new StringBuilder();
        reqString.append(host).append(path).append("?phone=").append(driverTel);

        //查询司机端结果
        JsonObject response = new JsonObject();

        Future<String> cir = circuitBreaker.execute(future -> {
            // 调用依赖服务
            logger.info("请求url= "+reqString.toString());
            webClient.getAbs(reqString.toString())
                    .send(res->{
                        if(res.result().statusCode() == 200){
                            JsonObject resJson = res.result().bodyAsJsonObject();
                            logger.info("请求driver api 成功，返回报文="+resJson.toString());
                            future.complete(resJson.toString());
                        }else{
                            logger.error("请求driver api 失败，code="+res.result().statusCode()+"msg="+res.result().statusMessage());
                            future.fail(res.result().statusCode()+res.result().statusMessage());
                        }
                    });

        });
        //断路器的回调
        cir.setHandler(res->{
            if(res.succeeded()){
                JsonObject resJson = new JsonObject(res.result());
                logger.info("请求driver api 成功，返回报文="+resJson.toString());
                String msg = resJson.getString("msg");
                int code = resJson.getInteger("code");
                if(0 == code){
                    String driverName = resJson.getJsonObject("data").getString("name");
                    response.put("code", HttpResCode.SUCCESS_CODE);
                    response.put("result",driverName);
                    handler.handle(Future.succeededFuture(response));
                }else{
                    response.put("code", HttpResCode.FAIL_CODE);
                    response.put("result",msg);
                    handler.handle(Future.succeededFuture(response));
                }
            }else{
                logger.error("请求driver api 失败="+res.result());
                response.put("code", HttpResCode.FAIL_CODE);
                response.put("result",res.result());
                handler.handle(Future.failedFuture(response.toString()));
            }
        });
        //回退逻辑
        circuitBreaker.<JsonObject>fallback(res->{
            res.printStackTrace();
            logger.error("请求司机端失败，执行fallback方法");
            response.put("code", HttpResCode.FAIL_CODE);
            response.put("result",res.getMessage());
            return response;
        });
    }

    private CircuitBreaker createCircuitBreaker(Vertx vertx, JsonObject config) {
        JsonObject circuitObject = config.getJsonObject("circuit-breaker");
        JsonObject cbOptions = circuitObject != null ? circuitObject : new JsonObject();
        CircuitBreakerOptions options = new CircuitBreakerOptions();
        options.setMaxFailures(cbOptions.getInteger("max-failures", 3));
        options.setTimeout(cbOptions.getLong("timeout", 3000L));
        options.setFallbackOnFailure(true);
        options.setResetTimeout(cbOptions.getLong("reset-timeout", 30000L));
        String name = cbOptions.getString("name", "circuit-breaker");

        return CircuitBreaker.create(name, vertx, options);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}
