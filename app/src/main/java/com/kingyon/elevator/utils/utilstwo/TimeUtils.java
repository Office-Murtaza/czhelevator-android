package com.kingyon.elevator.utils.utilstwo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Created By Admin  on 2020/5/9
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TimeUtils {


    /**
     * 根据毫秒时间戳来格式化字符串
     * 今天显示几时几分、昨天显示昨天、前天显示前天.
     * 早于前天的显示具体年-月-日，如2017-06-12；
     * @param timeStamp 毫秒值
     * @return 几时几分 昨天 前天 或者 yyyy-MM-dd 类型字符串
     */
    public static String format(long timeStamp){
        String strTime="";
        long curTimeMillis = System.currentTimeMillis();
        Date curDate = new Date(curTimeMillis);
        int todayHoursSeconds = curDate.getHours() * 60 * 60;
        int todayMinutesSeconds = curDate.getMinutes() * 60;
        int todaySeconds = curDate.getSeconds();
        int todayMillis = (todayHoursSeconds + todayMinutesSeconds + todaySeconds) * 1000;
        long todayStartMillis = curTimeMillis - todayMillis;
        if(timeStamp >= todayStartMillis) {
            Date date=new Date(timeStamp);
            strTime=date.getHours()+":"+date.getMinutes();//显示几时几分
            return strTime;
        }
        int oneDayMillis=24*60*60*1000;
        long yesterdayStartMillis=todayStartMillis-oneDayMillis;
        if (timeStamp>=yesterdayStartMillis){
            return "昨天";
        }
        long yesterdayBeforeStartMillis=yesterdayStartMillis-oneDayMillis;
        if (timeStamp>=yesterdayBeforeStartMillis){
            return "前天";
        }
        strTime = getDateToString(timeStamp, "yyyy-MM-dd ");
        return strTime;
    }


    /**
     * 时间戳转换成字符窜
     * @param milSecond
     * @param pattern
     * @return
     */
    public static String getDateToString(long milSecond, String pattern) {
        Date date = new Date(milSecond);
        SimpleDateFormat format = new SimpleDateFormat(pattern);
        return format.format(date);
    }


    /**
     * 获取系统时间戳
     * @return
     */
    public static long getCurTimeLong(){
        long time=System.currentTimeMillis();
        return time;
    }

    public static String secondToTime(long second) {
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

}
