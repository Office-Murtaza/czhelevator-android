package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyInfoEntity implements Parcelable {

    /**
     * allIncome : 1236.03
     * usefulIncome : 1000.23
     * todayIncome : 236.02
     * mouthIncome : 236.32
     * yearIncome : 1236
     */

    private double allIncome;
    private double usefulIncome;
    private double todayIncome;
    private double mouthIncome;
    private double yearIncome;

    public double getAllIncome() {
        return allIncome;
    }

    public void setAllIncome(double allIncome) {
        this.allIncome = allIncome;
    }

    public double getUsefulIncome() {
        return usefulIncome;
    }

    public void setUsefulIncome(double usefulIncome) {
        this.usefulIncome = usefulIncome;
    }

    public double getTodayIncome() {
        return todayIncome;
    }

    public void setTodayIncome(double todayIncome) {
        this.todayIncome = todayIncome;
    }

    public double getMouthIncome() {
        return mouthIncome;
    }

    public void setMouthIncome(double mouthIncome) {
        this.mouthIncome = mouthIncome;
    }

    public double getYearIncome() {
        return yearIncome;
    }

    public void setYearIncome(double yearIncome) {
        this.yearIncome = yearIncome;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.allIncome);
        dest.writeDouble(this.usefulIncome);
        dest.writeDouble(this.todayIncome);
        dest.writeDouble(this.mouthIncome);
        dest.writeDouble(this.yearIncome);
    }

    public PropertyInfoEntity() {
    }

    protected PropertyInfoEntity(Parcel in) {
        this.allIncome = in.readDouble();
        this.usefulIncome = in.readDouble();
        this.todayIncome = in.readDouble();
        this.mouthIncome = in.readDouble();
        this.yearIncome = in.readDouble();
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
