package com.mialab.healthbutler.domain;

import java.util.ArrayList;

/**
 * 新闻数据javabean
 *
 * @author Wesly
 */
public class NewsData {

    public ArrayList<TopNewsData> topNews;
    public ArrayList<SimpleNewsData> simpleNews;

    public class TopNewsData {
        public int id;
        public String title;
        public String pubTime;
        public String BodyURL;
        public String imageURL;
    }

    public class SimpleNewsData {
        public int id;
        public String title;
        public String pubTime;
        public String BodyURL;
        public String imageURL;
    }
}
