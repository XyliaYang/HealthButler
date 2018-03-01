package com.mialab.healthbutler.domain;

import java.io.Serializable;

/**
 * Created by hp on 2016/6/10.
 */
public class DiseaseDepartments implements Serializable {

    private int id;
    private int department_id;
    private String disease_department_name;


    public DiseaseDepartments(int id, String disease_department_name) {
        this.id = id;
        this.disease_department_name = disease_department_name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getdepartment_id() {
        return department_id;
    }

    public void setdepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getIllness_name() {
        return disease_department_name;
    }

    public void setdisease_department_name(String disease_department_name) {
        this.disease_department_name = disease_department_name;
    }
}
