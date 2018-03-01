package com.mialab.healthbutler.domain;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Wesly186 on 2016/6/8.
 */
public class City {

    private int id;
    @SerializedName("city_name")
    private String name;

    public City(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
