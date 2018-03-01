package com.mialab.healthbutler.domain;

/**
 * Created by Wesly186 on 2016/6/9.
 */
public class Department {

    private int id;
    private String photo;
    private String name;
    private String desc;

    public Department(int id, String photo, String name, String desc) {
        this.id = id;
        this.photo = photo;
        this.name = name;
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
