package com.sdmjhca.jhmi.vert.xDemo.vertxWeb;

/**
 * @author JHMI on 2017/10/20.
 */
public class RespDto {
    private String key;

    @Override
    public String toString() {
        return "RespDto{" +
                "key='" + key + '\'' +
                '}';
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
