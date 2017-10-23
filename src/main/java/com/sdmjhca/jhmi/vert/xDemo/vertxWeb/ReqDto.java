package com.sdmjhca.jhmi.vert.xDemo.vertxWeb;

import java.io.Serializable;

/**
 * @author JHMI on 2017/10/20.
 */
public class ReqDto implements Serializable{

    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
