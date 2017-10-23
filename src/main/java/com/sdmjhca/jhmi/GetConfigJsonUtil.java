package com.sdmjhca.jhmi;

import io.vertx.core.json.JsonObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author JHMI on 2017/10/23.
 */
public class GetConfigJsonUtil {
    public static JsonObject getConfigJson() throws IOException {
        String path = System.getProperty("config","dev");
        System.out.println(path);
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream is = classLoader.getResourceAsStream(path+"/"+"config.json");
        //BufferedReader
        byte[] bytes = new byte[1024];
        StringBuffer sb = new StringBuffer();

        is.read(bytes);

        sb.append(bytes.toString());
        String s = new String(bytes,"UTF-8");
        System.out.println("---------"+s);

        JsonObject json = new JsonObject(s);
        return json;
    }
}
