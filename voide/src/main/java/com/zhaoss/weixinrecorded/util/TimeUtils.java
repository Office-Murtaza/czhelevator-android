package com.zhaoss.weixinrecorded.util;

import android.util.Log;

import java.util.Formatter;

/**
 * Created By Admin  on 2020/3/25
 * Email : 1531603384@qq.com
 */

public class TimeUtils {

    public static String secondToTime(long second) {
        Log.e("TAG",second+"==");
        long hours = second / 3600;//转换小时数
        second = second % 3600;//剩余秒数
        long minutes = second / 60;//转换分钟
        second = second % 60;//剩余秒数
        if (hours > 0) {
            return unitFormat(hours) + ":" + unitFormat(minutes) + ":" + unitFormat(second);
        } else {
            return unitFormat(minutes) + ":" + unitFormat(second);
        }
    }

    private static String unitFormat(long i) {
        String retStr;
        if (i >= 0 && i < 10)
            retStr = "0" + i;
        else
            retStr = "" + i;
        return retStr;
    }


    public static String stringForTime(long timeMs){
        long totalSeconds = timeMs/1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds/60)%60;
        long hours = totalSeconds/3600;

        return new Formatter().format("%02d:%02d:%02d",hours,minutes,seconds).toString();
    }

}


