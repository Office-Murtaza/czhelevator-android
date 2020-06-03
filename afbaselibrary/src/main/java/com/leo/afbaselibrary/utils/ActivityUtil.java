package com.leo.afbaselibrary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.text.TextUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * created by zhengliao on 16/11/24 15:30
 * email：46499102@qq.com
 */
public class ActivityUtil {
    /**
     * 获取当前显示的Activity
     *
     * @return 当前Activity
     */
    public static Activity getCurrentActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getCurActivity();
//        return null;
    }


    private static final List<Activity> activities = new CopyOnWriteArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            if (!activity.isFinishing())
                activity.finish();
        }
    }

    public static void finishAllNotThis(String name) {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            if (!activity.getClass().getSimpleName().contains("name") && !activity.isFinishing())
                activity.finish();
        }
    }

    public static void finishAllNotLogin() {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            if (!activity.getClass().getSimpleName().contains("LoginActivity") && !activity.isFinishing())
                activity.finish();
        }
    }

    public static void finishAllLogin() {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            if (activity.getClass().getSimpleName().contains("LoginActivity") && !activity.isFinishing())
                activity.finish();
        }
    }

    public static void finishAllMain() {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            if (activity.getClass().getSimpleName().contains("MainActivity") && !activity.isFinishing())
                activity.finish();
        }
    }

    public static void finishAllNotPassword() {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            if (!activity.getClass().getSimpleName().contains("LoginActivity") && !activity.getClass().getSimpleName().contains("RegisterActivity") && !activity.isFinishing())
                activity.finish();
        }
    }

    public static void finishAllNotMain() {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            if (!activity.getClass().getSimpleName().contains("MainActivity") && !activity.isFinishing())
                activity.finish();
        }
    }

    public static void finishTool() {
        if (activities.size() == 0)
            return;
        for (Activity activity : activities) {
            String packageName = activity.getClass().getName();
            if (!TextUtils.isEmpty(packageName) && packageName.contains("com.kingyon.capacap.uis.activities.tools") && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static void clearActivities() {
        if (activities != null)
            activities.clear();
    }

    public static Activity getCurActivity() {
        if (activities == null || activities.size() == 0)
//            return getCurrentActivity();
            return null;
        else
            return activities.get(activities.size() - 1);
    }

    public static boolean isRunningForeground(Context context) {
        boolean result = false;
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = activityManager.getRunningAppProcesses();// 枚举进程
            for (ActivityManager.RunningAppProcessInfo appProcessInfo : appProcessInfos) {
                if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (appProcessInfo.processName.equals(context.getApplicationInfo().processName)) {
                        result = true;
                        break;
                    }
                }
            }
        }
        return result;
    }

    public static List<Activity> getAuctionDetailsActivities() {
        List<Activity> results = new ArrayList<>();
        for (Activity activity : activities) {
            String packageName = activity.getClass().getSimpleName();
            if (!TextUtils.isEmpty(packageName) && packageName.contains("AuctionDetailsActivity") && !activity.isFinishing()) {
                results.add(activity);
            }
        }
        return results;
    }
}