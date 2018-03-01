package com.mialab.healthbutler.domain;

import java.util.ArrayList;

/**
 * MUSIC的javaBean
 *
 * @author Wesly
 */
public class MusicData {

    private ArrayList<Music> mMusicList;

    public ArrayList<Music> getmMusicList() {
        return mMusicList;
    }

    public void setmMusicList(ArrayList<Music> mMusicList) {
        this.mMusicList = mMusicList;
    }

    public class Music {
        int id;
        String mName;
        String mImageURL;
        String mMusicURL;
        boolean mLike;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public boolean ismLike() {
            return mLike;
        }

        public void setmLike(boolean mLike) {
            this.mLike = mLike;
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

        public String getmMusicURL() {
            return mMusicURL;
        }

        public void setmMusicURL(String mMusicURL) {
            this.mMusicURL = mMusicURL;
        }
    }

}
