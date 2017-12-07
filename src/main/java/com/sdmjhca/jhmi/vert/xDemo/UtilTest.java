package com.sdmjhca.jhmi.vert.xDemo;


import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;
import io.vertx.core.json.JsonObject;
import org.apache.commons.lang.math.NumberUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author JHMI on 2017/10/23.
 */
public class UtilTest {

    public static void main(String[] args) {
        /*String s =  bundle.getString("mongo");
        System.out.println(s);*/
        String num = "13811221709" ;
        getInnerNode(num);
    }

    private static int hash(String key) {
        HashFunction hf = Hashing.murmur3_32();
        HashCode hc = hf.newHasher().putString(key, Charsets.UTF_8).hash();

        return hc.asInt();
    }

    public static JsonObject getInnerNode(String key) {
        JsonObject result = new JsonObject();
        // int hashedKey = hash(key);
        // Entry<Integer, String> en = virtualInnerNodes.ceilingEntry(hashedKey);
        // if (en == null) {
        // result.put("host", virtualInnerNodes.firstEntry().getValue());
        // } else {
        // result.put("host", en.getValue());
        // }
        List<String> realInnerNodes = new ArrayList<>();
        realInnerNodes.add("10.0.7.52");
        realInnerNodes.add("10.0.7.70");
        realInnerNodes.add("10.0.4.20");
        //[10.0.7.52, 10.0.7.70, 10.0.4.20]
        /*int i = Integer.parseInt(key) % 3;
        System.out.println("i="+i);
        System.out.println(realInnerNodes.get(i));*/
        //result.put("host", realInnerNodes.get(i));

        System.out.println(key);
        //System.out.println(Long.valueOf(key));

//        System.out.println(new BigDecimal(key));

        key = key.replace("\"","");
        System.out.println(key);

        long l =  (NumberUtils.toLong(key) % 3);
        System.out.println("l="+l);
        System.out.println(realInnerNodes.get(Integer.parseInt(String.valueOf(l))));

        System.out.println("-------------------");
        int h = 0;
        h = (h = key.hashCode()) ^ (h >>> 3);
        System.out.println(h);
        return result;
    }
}