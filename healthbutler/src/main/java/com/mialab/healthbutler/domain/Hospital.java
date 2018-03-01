package com.mialab.healthbutler.domain;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Wesly186 on 2016/6/8.
 */
public class Hospital implements Serializable {

    private int id;
    @SerializedName("city_id")
    private int cityId;
    @SerializedName("hospital_name")
    private String name;

    public Hospital(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
