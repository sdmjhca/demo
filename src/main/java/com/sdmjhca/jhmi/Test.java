package com.sdmjhca.jhmi;

import java.util.HashMap;

/**
 * @author JHMI on 2017/11/23.
 */
public class Test {
    public static void main(String[] args) {
        HashMap map = new HashMap();
        String old = "";
        old = (String) map.put("a","1");
        System.out.println(old);
        old = (String) map.put("a","2");
        System.out.println(old);

        System.out.println(map.toString());

        if(map.remove("a","1")){
            System.out.println("success");
        }else{
            System.out.println("fail");
        }
        if(map.remove("a","2")){
            System.out.println("success--");
        }
        System.out.println(map.toString());
    }
}
