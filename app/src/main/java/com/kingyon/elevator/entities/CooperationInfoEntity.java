package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class CooperationInfoEntity implements Parcelable {

    /**
     * allIncome : 1236.03
     * usefulIncome : 1000.23
     * todayIncome : 236.02
     * mouthIncome : 236.32
     * yearIncome : 1236
     * propertyPay : 4569
     * networkPay : 562
     */

    private float taxation;
    private double allIncome;
    private double usefulIncome;
    private double todayIncome;
    private double mouthIncome;
    private double yearIncome;
    private double propertyPay;
    private double networkPay;

    public float getTaxation() {
        return taxation;
    }

    public void setTaxation(float taxation) {
        this.taxation = taxation;
    }

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

    public double getPropertyPay() {
        return propertyPay;
    }

    public void setPropertyPay(double propertyPay) {
        this.propertyPay = propertyPay;
    }

    public double getNetworkPay() {
        return networkPay;
    }

    public void setNetworkPay(double networkPay) {
        this.networkPay = networkPay;
    }

    public CooperationInfoEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.taxation);
        dest.writeDouble(this.allIncome);
        dest.writeDouble(this.usefulIncome);
        dest.writeDouble(this.todayIncome);
        dest.writeDouble(this.mouthIncome);
        dest.writeDouble(this.yearIncome);
        dest.writeDouble(this.propertyPay);
        dest.writeDouble(this.networkPay);
    }

    protected CooperationInfoEntity(Parcel in) {
        this.taxation = in.readFloat();
        this.allIncome = in.readDouble();
        this.usefulIncome = in.readDouble();
        this.todayIncome = in.readDouble();
        this.mouthIncome = in.readDouble();
        this.yearIncome = in.readDouble();
        this.propertyPay = in.readDouble();
        this.networkPay = in.readDouble();
    }

    public static final Creator<CooperationInfoEntity> CREATOR = new Creator<CooperationInfoEntity>() {
        @Override
        public CooperationInfoEntity createFromParcel(Parcel source) {
            return new CooperationInfoEntity(source);
        }

        @Override
        public CooperationInfoEntity[] newArray(int size) {
            return new CooperationInfoEntity[size];
        }
    };
}
