package com.mialab.healthbutler.app;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity收集器
 *
 * @author Wesly
 */
public class ActivityCollector {
    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static int getSize(){
        return activities.size();
    }
}