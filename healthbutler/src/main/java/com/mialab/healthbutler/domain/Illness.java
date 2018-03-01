package com.mialab.healthbutler.domain;

import java.io.Serializable;

/**
 * Created by hp on 2016/6/10.
 */
public class Illness implements Serializable {

    private int id;
    private int branch_id;
    private String illness_name;


    public Illness(int id, String illness_name) {
        this.id = id;
        this.illness_name = illness_name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(int branch_id) {
        this.branch_id = branch_id;
    }

    public String getIllness_name() {
        return illness_name;
    }

    public void setIllness_name(String illness_name) {
        this.illness_name = illness_name;
    }
}
