package com.sdmjhca.jhmi.vert.xDemo.loggerTest;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author JHMI on 2017/10/24.
 */
public class LoggerMain {

    @Test
    public void test(){
        Logger logger = LoggerFactory.getLogger(LoggerMain.class.getName());
        logger.info("hello world {},{}","paul","java");
    }

    @BeforeClass
    public static void before(){
        System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory.class.getName());
        LoggerFactory.initialise();
    }

}
