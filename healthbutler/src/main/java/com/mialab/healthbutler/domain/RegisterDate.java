package com.mialab.healthbutler.domain;

import java.io.Serializable;

/**
 * Created by hp on 2016/7/21.
 */
public class RegisterDate implements Serializable {

    private  int id;
    private String  register_time;
    private String register_intro;

    public RegisterDate(int id, String register_time, String register_intro) {
        this.id = id;
        this.register_time = register_time;
        this.register_intro = register_intro;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRegister_time() {
        return register_time;
    }

    public void setRegister_time(String register_time) {
        this.register_time = register_time;
    }

    public String getRegister_intro() {
        return register_intro;
    }

    public void setRegister_intro(String register_intro) {
        this.register_intro = register_intro;
    }
}
