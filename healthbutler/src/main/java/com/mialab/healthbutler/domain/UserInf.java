package com.mialab.healthbutler.domain;


/**
 * 用户资料注册数据javabean
 *
 * @author Wesly
 */
public class UserInf {

    private String mUserName;
    private String mPassword;
    private String mPhoneNum;

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmPhoneNum() {
        return mPhoneNum;
    }

    public void setmPhoneNum(String mPhoneNum) {
        this.mPhoneNum = mPhoneNum;
    }
}
