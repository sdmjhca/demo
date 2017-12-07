package com.sdmjhca.jhmi.vert.xDemo;

import java.io.Serializable;

/**
 * @author JHMI on 2017/10/12.
 */
public class MyPOJO implements Serializable{

    private String name;
    private String sex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    @Override
    public String toString() {
        return "MyPOJO{" +
                "name='" + name + '\'' +
                ", sex='" + sex + '\'' +
                '}';
    }
}
