package com.leo.afbaselibrary.utils;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by arvin on 2016/2/2 16:40.
 * 时间转换相关方法
 */
@SuppressWarnings("ALL")
public class TimeUtil {
    public static final long second = 1000L;
    public static final long minute = 60 * second; //分钟
    public static final long hour = 60 * minute; //小时
    public static final long day = 24 * hour;    //天
    public static final long week = 7 * day;     //周
    public static final long month = 31 * day;   //月
    public static final long year = 12 * month;  //年

    public static long getCurrentTime() {
        return Calendar.getInstance().getTimeInMillis();
    }

    public static String getTime(long time, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        Date now = new Date(time);
        return simpleDateFormat.format(now);
    }

    /**
     * 将time转换为 1970-1-1 00:00:00 格式的时间
     */
    public static String getAllTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1970-1-1 00:00:00 格式的时间
     */
    public static String getAllTimeNoSecond(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1970-1-1 00:00:00 格式的时间
     */
    public static String getAllTimeDuration(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    public static long ymdToLong(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(time);
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 将time转换为 1970-1-1 格式的时间
     */
    public static String getYMTime(long time) {
        if (time == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1970-1-1 格式的时间
     */
    public static String getYMdTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1970-1-1 格式的时间
     */
    public static String getYMdTimeDot(long time) {
        if (time == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1970年1月1日 格式的时间
     */
    public static String getYMdTimeText(long time) {
        if (time == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1970年1月1日 格式的时间
     */
    public static String getRecenlyYmdhmTimeText(long time) {
        if (time == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getYmdhmChineseTimeText(long time) {
//        if (time == 0) {
//            return "";
//        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1-1 格式的时间
     */
    public static String getMDTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 1-1 格式的时间
     */
    public static String getMDHMTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 00:00 格式的时间
     */
    public static String getHmTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 00:00:00 格式的时间
     */
    public static String getHmsTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 将time转换为 00:00:00 格式的时间
     */
    public static String getMsTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("mm:ss");
        Date now = new Date(time);
        return format.format(now);
    }

    public static int getDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Date now = new Date(time);
        return Integer.parseInt(format.format(now));
    }

    /**
     * 获取最近时间根据差值确定时间显示的样式
     */
    public static String getRecentlyTime(long time) {
        if (time <= 0) {
            return null;
        }
        long nowtime = System.currentTimeMillis();
        long diff = nowtime - time;
        long r = 0;
        if (diff < minute) {
            return "刚刚";
        }
        if (diff < hour) {
            r = (diff / minute);
            return r + "分钟前";
        }
        if (diff < 4 * hour) {
            r = (diff / hour);
            return r + "小时前";
        }
        if (TextUtils.equals(getYMdTime(time), getYMdTime(nowtime))) {
            return getHmTime(time);
        } else if (TextUtils.equals(getYTime(time), getYTime(nowtime))) {
            return getMDHMTime(time);
        }
        return getAllTimeNoSecond(time);
    }

    public static String getYTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date now = new Date(time);
        return format.format(now);
    }

    /**
     * 获取最近时间根据差值确定时间显示的样式
     */
    public static String[] getAgoTime(long time) {
        String[] result = new String[]{"--", ""};
        long diff = System.currentTimeMillis() - time;
        if (time > 0 && diff > 0) {
            if (diff >= year) {
                result[0] = String.valueOf((diff / year));
                result[1] = "年前";
            } else if (diff >= month) {
                result[0] = String.valueOf((diff / month));
                result[1] = "月前";
            } else if (diff >= day) {
                result[0] = String.valueOf((diff / day));
                result[1] = "天前";
            } else if (diff > hour) {
                result[0] = String.valueOf((diff / hour));
                result[1] = "小时前";
            } else if (diff > minute) {
                result[0] = String.valueOf((diff / minute));
                result[1] = "分钟前";
            } else {
                result[0] = String.valueOf(diff / 1000);
                result[1] = "秒前";
            }
        }
        return result;
    }

    /**
     * 获取年纪
     */
    public static int getAge(long time) {
        if (time <= 0) {
            return 0;
        }
        Date birthday = new Date();
        birthday.setTime(time);
        Calendar cal = Calendar.getInstance();
        if (cal.before(birthday)) {
            throw new IllegalArgumentException(
                    "The birthDay is before Now.It's unbelievable!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }

    public static String getRemainingTimeMinute(long duration) {
        String result;
        if (duration <= 0) {
            result = "";
        } else {
            long dayNumber = duration / day;
            long hourNumber = duration % day / hour;
            long minuteNumber = duration % hour / minute;
            if (dayNumber > 0) {
                result = String.format("%s天%s时%s分", dayNumber, hourNumber, minuteNumber);
            } else if (hourNumber > 0) {
                result = String.format("%s时%s分", hourNumber, minuteNumber);
            } else {
                result = String.format("%s分", minuteNumber);
            }
        }
        return result;
    }

    public static String getRemainingTimeSecond(long duration) {
        String result;
        if (duration < 0) {
            result = "";
        } else {
            long dayNumber = duration / day;
            long hourNumber = duration % day / hour;
            long minuteNumber = duration % hour / minute;
            long secondNumber = duration % minute / second;
            if (dayNumber > 0) {
                result = String.format("%s天%s时%s分%s秒", dayNumber, getTwoDigits(hourNumber), getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            } else if (hourNumber > 0) {
                result = String.format("%s时%s分%s秒", getTwoDigits(hourNumber), getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            } else if (minuteNumber > 0) {
                result = String.format("%s分%s秒", getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            } else {
                result = String.format("%s秒", getTwoDigits(secondNumber));
            }
        }
        return result;
    }

    public static String getRemainingTimeFormat(long duration) {
        String result;
        if (duration < 0) {
            result = "";
        } else {
            long dayNumber = duration / day;
            long hourNumber = duration % day / hour;
            long minuteNumber = duration % hour / minute;
            long secondNumber = duration % minute / second;
            if (dayNumber > 0) {
                result = String.format("%s天%s小时%s分%s秒", dayNumber, getTwoDigits(hourNumber), getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            } else {
                result = String.format("%s:%s:%s", getTwoDigits(hourNumber), getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            }
        }
        return result;
    }

    public static String getTwoDigits(double num) {
        DecimalFormat df = new DecimalFormat("#00");
        return df.format(num);
    }

    public static long getDayStartTimeMilliseconds(long time) {
        return getTimeByMilliseconds(String.format("%s 00:00:00.000", TimeUtil.getYMdTime(time)));
    }

    public static long getDayEndTimeMilliseconds(long time) {
        return getTimeByMilliseconds(String.format("%s 23:59:59.999", TimeUtil.getYMdTime(time)));
    }

    public static Long[] getTodayDuration(long time) {
        Long[] results = new Long[2];
        results[0] = getTimeByMilliseconds(String.format("%s 00:00:00.000", TimeUtil.getYMdTime(time)));
        results[1] = getTimeByMilliseconds(String.format("%s 23:59:59.999", TimeUtil.getYMdTime(time)));
        return results;
    }

    public static long getTimeByMilliseconds(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        try {
            return format.parse(src).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getMillisecondsTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date(time);
        return format.format(now);
    }
}
