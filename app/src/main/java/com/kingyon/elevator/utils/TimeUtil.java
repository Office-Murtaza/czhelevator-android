package com.kingyon.elevator.utils;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Joe on 2016/12/21.
 */

public class TimeUtil {
    public static String getAllTimeItalic(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getYmdTimeItalic(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getAllTimeNoSecondWithDot(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getAllTimeWithDot(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getAllTimeNoSecond(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getAllTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getYmdWithDot(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getYmdCh(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getYmdDliverCh(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getYmCh(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getYCh(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年");
        Date now = new Date(time);
        return format.format(now);
    }

    public static Date getCalenderDate(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date now = null;
        try {
            now = format.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (now == null) {
                Date date = new Date();
                date.setTime(System.currentTimeMillis());
                return date;
            } else {
                return now;
            }
        }
    }

    public static int getYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date now = new Date(time);
        return Integer.parseInt(format.format(now));
    }

    public static int getMonthDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");
        Date now = new Date(time);
        return Integer.parseInt(format.format(now));
    }

    public static int getMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date now = new Date(time);
        return Integer.parseInt(format.format(now));
    }

    public static String getYear(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
            date = format1.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (date != null) {
                return format.format(date);
            } else {
                return "";
            }
        }
    }

    public static String getYear(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        return format.format(date);
    }

    public static String getMonth(String src) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date date = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
            date = format1.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (date != null) {
                return format.format(date);
            } else {
                return "";
            }
        }
    }

    public static String getTimeNoline(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月");
        Date date = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
            date = format1.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (date != null) {
                return format.format(date);
            } else {
                return "";
            }
        }
    }


    public static String getMonth(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        return format.format(date);
    }

    public static String getDay(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        return format.format(date);
    }

    public static String getContrastContent(int year, int month) {
        String str = String.valueOf(month);
        if (month < 10) {
            str = "0" + str;
        }
        return year + "-" + str;
    }

    public static String getLastPhase(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String result = null;
        try {
            Date date = format.parse(src);
            int month = getMonth(date.getTime());
            int year = getYear(date.getTime());
            if (month - 1 > 0) {
                result = getContrastContent(year, month - 1);
            } else {
                result = getContrastContent(year - 1, 12);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("getLastPhase", "日期数据解析出错，请检查格式");
        } finally {
            return result;
        }
    }

    public static String getSamePhase(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        String result = null;
        try {
            Date date = format.parse(src);
            int month = getMonth(date.getTime());
            int year = getYear(date.getTime());
            result = getContrastContent(year - 1, month);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.i("getLastPhase", "日期数据解析出错，请检查格式");
        } finally {
            return result;
        }
    }

    public static String getFormatDateByChat(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM月");
        Date date = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM");
            date = format1.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (date != null) {
                return format.format(date);
            } else {
                return "";
            }
        }
    }

    public static String getFormatDateYM(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            date = format1.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (date != null) {
                return format.format(date);
            } else {
                return "";
            }
        }
    }

    public static String getFormatDateYMD(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年\nMM月dd日");
        Date date = null;
        try {
            SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
            date = format1.parse(src);
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (date != null) {
                return format.format(date);
            } else {
                return "";
            }
        }
    }

    public static String getTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date now = new Date(time);
        return format.format(now);
    }

    public static String getTimeNoHour(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date(time);
        return format.format(now);
    }

    public static long getLongTime(String src) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(src).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getDurationTime(int duration) {
        duration = duration / 1000;
        int ss = duration % 60;
        int mm = duration / 60 % 60;
        int hh = duration / 60 / 60 % 60;
        return (hh == 0 ? "" : String.valueOf(hh) + ":") + CommonUtil.getTwoDigits(mm) + ":" + CommonUtil.getTwoDigits(ss);
    }


    /**
     * 日期转换成秒数
     * */
    public static long getSecondsFromDate(String expireDate){
        if(expireDate==null||expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try{
            date=sdf.parse(expireDate);
            return (long)(date.getTime()/1000);
        }
        catch(ParseException e)
        {
            e.printStackTrace();
            return 0L;
        }
    }

    /**
     * 输入时间戳变星期,例如（1402733340）输出（"周六"）
     */
    public static String getWeek(long timeStamp) {
        int mydate = 0;
        String week = null;
        Calendar cd = Calendar.getInstance();
        cd.setTime(new Date(timeStamp * 1000L));
        mydate = cd.get(Calendar.DAY_OF_WEEK);
        // 获取指定日期转换成星期几
        if (mydate == 1) {
            week = "（周日）";
        } else if (mydate == 2) {
            week = "（周一）";
        } else if (mydate == 3) {
            week = "（周二）";
        } else if (mydate == 4) {
            week = "（周三）";
        } else if (mydate == 5) {
            week = "（周四）";
        } else if (mydate == 6) {
            week = "（周五）";
        } else if (mydate == 7) {
            week = "（周六）";
        }
        return week;
    }

    public static String times(long timeStamp) {
        SimpleDateFormat sdr = new SimpleDateFormat("MM月dd日");
        return sdr.format(new Date(timeStamp)).replaceAll("#",
                getWeek(timeStamp));


    }

    public static String getDayNumber(long starTime,long endTime) {
        String daystr = "";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date1 = formatter.format(starTime);
        String date2 = formatter.format(endTime);
        // 获取服务器返回的时间戳 转换成"yyyy-MM-dd HH:mm:ss"
        // 计算的时间差
        LogUtils.e(date1,date2);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(date1);
            Date d2 = df.parse(date2);
            // 这样得到的差值是微秒级别
            long diff = d1.getTime() - d2.getTime();
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24))
                    / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours
                    * (1000 * 60 * 60))
                    / (1000 * 60);
//            Log.i("viewDataFill", "会员剩余: " + " + hours + "小时" + minutes + "分");
            LogUtils.e(days,hours,minutes);
            daystr = String.valueOf((days+1));
        } catch (Exception e) {
        }
        return "共" + daystr + "天";
    }
}
