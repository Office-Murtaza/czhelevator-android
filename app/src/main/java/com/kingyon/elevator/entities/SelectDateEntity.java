package com.kingyon.elevator.entities;

/**
 * Created By SongPeng  on 2019/12/16
 * Email : 1531603384@qq.com
 */
public class SelectDateEntity {

    private int year;
    private int month;
    private int day;
    private String date;

    public SelectDateEntity(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        if (month<10) {
            if (day<10) {
                date = year + "-0" + month + "-0" + day;
            }else {
                date = year + "-0" + month + "-" + day;
            }
        }else {
            if (day<10) {
                date = year + "-" + month + "-0" + day;
            }else {
                date = year + "-" + month + "-" + day;
            }
        }
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

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }
}
