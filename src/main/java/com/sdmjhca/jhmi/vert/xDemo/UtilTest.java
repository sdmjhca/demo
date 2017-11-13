package com.sdmjhca.jhmi.vert.xDemo;


import com.google.common.base.Charsets;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * @author JHMI on 2017/10/23.
 */
public class UtilTest {
    //private static ResourceBundle bundle = ResourceBundle.getBundle("config");

    // 真实节点对应的虚拟节点数量
    private static int length = 160;
    // 虚拟节点信息

    // 虚拟内网ip
    private static TreeMap<Long, String> virtualInnerNodes;

    // 真实节点信息
    private List<String> realIMNodes;

    private static List<String> realInnerNodes = new ArrayList<>();
    public static void main(String[] args) {
        /*String s =  bundle.getString("mongo");
        System.out.println(s);*/
        realInnerNodes.add("10.0.7.52");
        //realInnerNodes.add("10.0.7.70");
        realInnerNodes.add("10.0.4.20");
        realInnerNodes.add("10.0.7.70");
        realInnerNodes.add("10.0.7.71");
        realInnerNodes.add("10.0.7.71");
        realInnerNodes.add("10.0.7.71");

        virtualInnerNodes = new TreeMap<Long, String>();
        for (int i = 0; i < realInnerNodes.size(); i++) {
            for (int j = 0; j < length; j++) {
                virtualInnerNodes.put(hash("aa" + i + j), realInnerNodes.get(i));
            }
        }

        Iterator iterator = virtualInnerNodes.entrySet().iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }

         Long hashedKey = hash("18518983654");
        System.out.println("key="+hashedKey);
         Map.Entry<Long, String> en = virtualInnerNodes.ceilingEntry(hashedKey);
         if (en == null) {
             System.out.println("en == null "+virtualInnerNodes.firstEntry().getValue());
         } else {
             System.out.println("en != null " + en.getValue());
         }
    }

   /* private static int hash(String key) {
        HashFunction hf = Hashing.murmur3_32();
        HashCode hc = hf.newHasher().putString(key, Charsets.UTF_8).hash();

        return hc.asInt();
    }*/

    private static Long hash(String key) {
        ByteBuffer buf = ByteBuffer.wrap(key.getBytes());
        int seed = 0x1234ABCD;

        ByteOrder byteOrder = buf.order();
        buf.order(ByteOrder.LITTLE_ENDIAN);

        long m = 0xc6a4a7935bd1e995L;
        int r = 47;

        long h = seed ^ (buf.remaining() * m);

        long k;
        while (buf.remaining() >= 8) {
            k = buf.getLong();

            k *= m;
            k ^= k >>> r;
            k *= m;

            h ^= k;
            h *= m;
        }

        if (buf.remaining() > 0) {
            ByteBuffer finish = ByteBuffer.allocate(8).order(ByteOrder.LITTLE_ENDIAN);
            // for big-endian version, do this first:
            // finish.position(8-buf.remaining());
            finish.put(buf).rewind();
            h ^= finish.getLong();
            h *= m;
        }

        h ^= h >>> r;
        h *= m;
        h ^= h >>> r;

        buf.order(byteOrder);
        return h;
    }
}