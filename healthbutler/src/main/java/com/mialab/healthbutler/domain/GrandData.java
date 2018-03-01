package com.mialab.healthbutler.domain;

import java.util.ArrayList;

public class GrandData {
    private ArrayList<Content> mContentList;

    public ArrayList<Content> getmContentList() {
        return mContentList;
    }

    public void setmContentList(ArrayList<Content> mContentList) {
        this.mContentList = mContentList;
    }

    public class Content {
        int id;
        String mTime;
        String mHeadURL;
        String mName;
        String mContent;
        String mImageURL;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public String getmHeadURL() {
            return mHeadURL;
        }

        public void setmHeadURL(String mHeadURL) {
            this.mHeadURL = mHeadURL;
        }

        public String getmName() {
            return mName;
        }

        public void setmName(String mName) {
            this.mName = mName;
        }

        public String getmImageURL() {
            return mImageURL;
        }

        public void setmImageURL(String mImageURL) {
            this.mImageURL = mImageURL;
        }

        public String getmContent() {
            return mContent;
        }

        public void setmContent(String mContent) {
            this.mContent = mContent;
        }

        public String getmTime() {
            return mTime;
        }

        public void setmTime(String mTime) {
            this.mTime = mTime;
        }
    }
}
