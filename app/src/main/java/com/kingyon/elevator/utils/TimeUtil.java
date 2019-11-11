package com.kingyon.elevator.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
        SimpleDateFormat format = new SimpleDateFormat("yyyy年\nMM月dd日");
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
}
