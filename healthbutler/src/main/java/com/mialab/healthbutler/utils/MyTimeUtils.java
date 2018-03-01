package com.mialab.healthbutler.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Wesly186 on 2016/3/13.
 */
public class MyTimeUtils {
    public static String time2Now(String publishDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String nowDate = dateFormat.format(new Date());

        //当前时间
        int nowYear = Integer.parseInt(nowDate.substring(0, 4));
        int nowMonth = Integer.parseInt(nowDate.substring(5, 7));
        int nowDay = Integer.parseInt(nowDate.substring(8, 10));
        int nowHour = Integer.parseInt(nowDate.substring(11, 13));
        int nowMinute = Integer.parseInt(nowDate.substring(14, 16));

        //publish时间
        int publishYear = Integer.parseInt(publishDate.substring(0, 4));
        int publishMonth = Integer.parseInt(publishDate.substring(5, 7));
        int publishDay = Integer.parseInt(publishDate.substring(8, 10));
        int publishHour = Integer.parseInt(publishDate.substring(11, 13));
        int publishMinute = Integer.parseInt(publishDate.substring(14, 16));


        if (nowYear - publishYear > 0) {//一年前
            return publishYear + "-" + publishMonth + "-" + publishDay;
        } else {
            if (nowMonth - publishMonth > 0) {//一个月前
                return publishMonth + "-" + publishDay;
            } else {
                if (nowDay - publishDay > 1) {//2天前
                    return publishMonth + "-" + publishDay;
                } else {
                    if (nowDay - publishDay > 0) {//一天前
                        return "昨天" + publishHour + ":" + publishMinute;
                    } else {
                        if (nowHour - publishHour > 0) {//一小时前
                            return nowHour - publishHour + "小时前";
                        } else {
                            if (nowMinute - publishMinute > 4) {//5分钟前
                                return nowMinute - publishMinute + "分钟前";
                            } else {
                                return "刚刚";
                            }
                        }
                    }
                }
            }
        }
    }
}
