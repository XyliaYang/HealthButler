package com.mialab.healthbutler.domain;


import java.io.Serializable;

/**
 * Created by hp on 2016/6/10.
 */
public class Branch  implements Serializable {

    private int id;
    private String branch_name;

    public Branch(int id, String branch_name) {
        this.id = id;
        this.branch_name = branch_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDept_name(String dept_name) {
        this.branch_name = dept_name;
    }

    public int getId() {
        return id;
    }

    public String getBranch_name() {
        return branch_name;
    }
}
