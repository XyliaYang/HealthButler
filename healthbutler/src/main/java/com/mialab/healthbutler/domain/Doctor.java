package com.mialab.healthbutler.domain;

import java.io.Serializable;

/**
 * Created by hp on 2016/6/10.
 */
public class Doctor implements Serializable{

    private int id;
    private String illness_name;
    private String doctor_name;
    private String hospital_name;
    private String head_image;
    private String personal_website;
    private String doctor_intro;


    public Doctor(int id, String illness_name, String hospital_name, String doctor_name, String head_image, String personal_website, String doctor_intro) {
        this.id = id;
        this.illness_name = illness_name;
        this.hospital_name = hospital_name;
        this.doctor_name = doctor_name;
        this.head_image = head_image;
        this.personal_website = personal_website;
        this.doctor_intro = doctor_intro;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIllness_name() {
        return illness_name;
    }

    public void setIllness_name(String illness_name) {
        this.illness_name = illness_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getPersonal_website() {
        return personal_website;
    }

    public void setPersonal_website(String personal_website) {
        this.personal_website = personal_website;
    }

    public String getDoctor_intro() {
        return doctor_intro;
    }

    public void setDoctor_intro(String doctor_intro) {
        this.doctor_intro = doctor_intro;
    }
}
