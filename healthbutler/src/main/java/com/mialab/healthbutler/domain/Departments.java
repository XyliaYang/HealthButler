package com.mialab.healthbutler.domain;


/**
 * Created by hp on 2016/6/10.
 */
public class Departments {

    private int id;
    private String departments_name;

    public Departments(int id, String departments_name) {
        this.id = id;
        this.departments_name = departments_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setdepartments_name(String departments_name) {
        this.departments_name = departments_name;
    }

    public int getId() {
        return id;
    }

    public String getdepartments_name() {
        return departments_name;
    }
}
