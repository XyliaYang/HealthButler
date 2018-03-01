package com.mialab.healthbutler.domain;

import java.io.Serializable;

/**
 * Created by hp on 2016/6/10.
 */
public class Disease implements Serializable{

    private int id;
    private String illness_name;
    private String introduction_content;
    private String reminder;
    private String symptom_website;
    private String curemethod_website;


    public Disease(int id, String illness_name, String introduction_content, String reminder, String symptom_website, String curemethod_website) {
        this.id = id;
        this.illness_name = illness_name;
        this.introduction_content = introduction_content;
        this.reminder = reminder;
        this.symptom_website = symptom_website;
        this.curemethod_website = curemethod_website;
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

    public String getintroduction_content() {
        return introduction_content;
    }

    public void setintroduction_content(String introduction_content) {
        this.introduction_content = introduction_content;
    }

    public String getreminder() {
        return reminder;
    }

    public void setreminder(String reminder) {
        this.reminder = reminder;
    }

    public String getsymptom_website() {
        return symptom_website;
    }

    public void setsymptom_website(String symptom_website) {
        this.symptom_website = symptom_website;
    }

    public String getcuremethod_website() {
        return curemethod_website;
    }

    public void setcuremethod_website(String curemethod_website) {
        this.curemethod_website = curemethod_website;
    }
}
