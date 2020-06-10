package com.kingyon.elevator.entities;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.date.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created By SongPeng  on 2019/12/16
 * Email : 1531603384@qq.com
 */
public class HorizontalSelectDateEntity {

    private String currentYearAndMonth;
    private int dayCount;//这个月有多少天
    private int year;
    private int month;
    private int startPosition;//一号是星期几的位置

    @Override
    public String toString() {
        return "HorizontalSelectDateEntity{" +
                "currentYearAndMonth='" + currentYearAndMonth + '\'' +
                ", dayCount=" + dayCount +
                ", year=" + year +
                ", month=" + month +
                ", startPosition=" + startPosition +
                '}';
    }

    public HorizontalSelectDateEntity(int year, int month, Date date) {
        this.year = year;
        this.month = month;
        dayCount = DateUtils.getDaysOfMonth(date);
        currentYearAndMonth = year + "年" + month + "月";
        startPosition = DateUtils.getWeekOfDatePosition(date);
        LogUtils.d("当前是：" + year + "-" + month + "总共有：" + dayCount + "天");
    }


    public String getCurrentYearAndMonth() {
        return currentYearAndMonth;
    }

    public void setCurrentYearAndMonth(String currentYearAndMonth) {
        this.currentYearAndMonth = currentYearAndMonth;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }
}
