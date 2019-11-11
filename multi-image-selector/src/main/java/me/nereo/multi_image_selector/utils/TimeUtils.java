package me.nereo.multi_image_selector.utils;

import android.media.ExifInterface;

import com.leo.afbaselibrary.utils.TimeUtil;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 时间处理工具
 * Created by Nereo on 2015/4/8.
 */
public class TimeUtils {

    public static String timeFormat(long timeMillis, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.CHINA);
        return format.format(new Date(timeMillis));
    }

    public static String formatPhotoDate(long time) {
        return timeFormat(time, "yyyy-MM-dd");
    }

    public static String formatPhotoDate(String path) {
        File file = new File(path);
        if (file.exists()) {
            long time = file.lastModified();
            return formatPhotoDate(time);
        }
        return "1970-01-01";
    }

    public static String getRemainingTimeFormat(long duration) {
        String result;
        if (duration < 0) {
            result = "";
        } else {
            long dayNumber = duration / TimeUtil.day;
            long hourNumber = duration % TimeUtil.day / TimeUtil.hour;
            long minuteNumber = duration % TimeUtil.hour / TimeUtil.minute;
            long secondNumber = duration % TimeUtil.minute / TimeUtil.second;
            if (dayNumber > 0) {
                result = String.format("%s天%s小时%s分%s秒", dayNumber, getTwoDigits(hourNumber), getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            } else if (hourNumber > 0) {
                result = String.format("%s:%s:%s", getTwoDigits(hourNumber), getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            } else {
                result = String.format("%s:%s", getTwoDigits(minuteNumber), getTwoDigits(secondNumber));
            }
        }
        return result;
    }

    public static String getTwoDigits(double num) {
        DecimalFormat df = new DecimalFormat("#00");
        return df.format(num);
    }
}
