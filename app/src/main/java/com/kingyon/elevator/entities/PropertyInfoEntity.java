package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyInfoEntity implements Parcelable {

    /**


     "total": 82090.8,

     "notClearing": 6744.6,

     "yesterday": 3002.4,

     "thisMonth": 82090.8,

     "thisYear": 82090.8
     */

    private double total;
    private double notClearing;
    private double yesterday;
    private double thisMonth;
    private double thisYear;

    public double getAllIncome() {
        return total;
    }

    public void setAllIncome(double allIncome) {
        this.total = allIncome;
    }

    public double getUsefulIncome() {
        return notClearing;
    }

    public void setUsefulIncome(double usefulIncome) {
        this.notClearing = usefulIncome;
    }

    public double getTodayIncome() {
        return yesterday;
    }

    public void setTodayIncome(double todayIncome) {
        this.yesterday = todayIncome;
    }

    public double getMouthIncome() {
        return thisMonth;
    }

    public void setMouthIncome(double mouthIncome) {
        this.thisMonth = mouthIncome;
    }

    public double getYearIncome() {
        return thisYear;
    }

    public void setYearIncome(double yearIncome) {
        this.thisYear = yearIncome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.total);
        dest.writeDouble(this.notClearing);
        dest.writeDouble(this.yesterday);
        dest.writeDouble(this.thisMonth);
        dest.writeDouble(this.thisYear);
    }

    public PropertyInfoEntity() {
    }

    protected PropertyInfoEntity(Parcel in) {
        this.total = in.readDouble();
        this.notClearing = in.readDouble();
        this.yesterday = in.readDouble();
        this.thisMonth = in.readDouble();
        this.thisYear = in.readDouble();
    }

    public static final Parcelable.Creator<PropertyInfoEntity> CREATOR = new Parcelable.Creator<PropertyInfoEntity>() {
        @Override
        public PropertyInfoEntity createFromParcel(Parcel source) {
            return new PropertyInfoEntity(source);
        }

        @Override
        public PropertyInfoEntity[] newArray(int size) {
            return new PropertyInfoEntity[size];
        }
    };
}
