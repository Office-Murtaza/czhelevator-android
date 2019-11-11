package com.kingyon.elevator.utils;


import com.kingyon.elevator.application.App;

/**
 * Created by gongli on 2017/7/17 17:39
 * email: lc824767150@163.com
 */
public class LeakCanaryUtils {
    public static void watchLeakCanary(Object obj) {
        if (App.getRefWatcher() != null && obj != null) {
            App.getRefWatcher().watch(obj);
        }
    }
}