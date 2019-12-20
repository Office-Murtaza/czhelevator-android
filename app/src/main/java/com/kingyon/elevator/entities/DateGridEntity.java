package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/12/13
 * Email : 1531603384@qq.com
 */
public class DateGridEntity {

    private int timeDay;//今天的日期  多少号
    private String date;//格式化后的日期  yyyy-MM-dd
    private int year;
    private int month;
    private int type = 1;//默认是正常日期，0表示在一号前面填充空的数据


    public DateGridEntity(int year,int month,int timeDay,int type) {
        this.timeDay = timeDay;
        this.year = year;
        this.month = month;
        this.type = type;
        date=year + "-" + month + "-" + timeDay;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getTimeDay() {
        return timeDay;
    }

    public void setTimeDay(int timeDay) {
        this.timeDay = timeDay;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
}
