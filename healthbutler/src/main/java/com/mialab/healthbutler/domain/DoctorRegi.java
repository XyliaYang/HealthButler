package com.mialab.healthbutler.domain;

import java.io.Serializable;

/**
 * Created by hp on 2016/7/21.
 */
public class DoctorRegi implements Serializable {

    private int id;
    private String illness_name;
    private String doctor_name;
    private String hospital_name;
    private String head_image;
    private String register_telephone;
    private String doctor_intro;


    public DoctorRegi(int id, String illness_name, String doctor_name, String hospital_name, String head_image, String register_telephone, String doctor_intro) {
        this.id = id;
        this.illness_name = illness_name;
        this.doctor_name = doctor_name;
        this.hospital_name = hospital_name;
        this.head_image = head_image;
        this.register_telephone = register_telephone;
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

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getHospital_name() {
        return hospital_name;
    }

    public void setHospital_name(String hospital_name) {
        this.hospital_name = hospital_name;
    }

    public String getHead_image() {
        return head_image;
    }

    public void setHead_image(String head_image) {
        this.head_image = head_image;
    }

    public String getRegister_telephone() {
        return register_telephone;
    }

    public void setRegister_telephone(String register_telephone) {
        this.register_telephone = register_telephone;
    }

    public String getDoctor_intro() {
        return doctor_intro;
    }

    public void setDoctor_intro(String doctor_intro) {
        this.doctor_intro = doctor_intro;
    }
}
