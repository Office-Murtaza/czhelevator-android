package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created By SongPeng  on 2019/11/29
 * Email : 1531603384@qq.com
 */
public class CooperationInfoNewEntity implements Parcelable {

    /**
     * totalIncome : 19878.06
     * realizableIncome : 17555.92
     * fulfilledIncome : 1000
     * yesterdayIncome : 426.93
     */
    private float taxation;
    private double totalIncome;
    private double realizableIncome;
    private double fulfilledIncome;
    private double yesterdayIncome;
    private boolean disable;
    private boolean isSetPayPassword;
    private boolean isCashing;

    public double getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(double totalIncome) {
        this.totalIncome = totalIncome;
    }

    public double getRealizableIncome() {
        return realizableIncome;
    }

    public void setRealizableIncome(double realizableIncome) {
        this.realizableIncome = realizableIncome;
    }

    public double getFulfilledIncome() {
        return fulfilledIncome;
    }

    public void setFulfilledIncome(double fulfilledIncome) {
        this.fulfilledIncome = fulfilledIncome;
    }

    public double getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(double yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public float getTaxation() {
        return taxation;
    }

    public void setTaxation(float taxation) {
        this.taxation = taxation;
    }

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public boolean isSetPayPassword() {
        return isSetPayPassword;
    }

    public void setSetPayPassword(boolean setPayPassword) {
        isSetPayPassword = setPayPassword;
    }

    public boolean isCashing() {
        return isCashing;
    }

    public void setCashing(boolean cashing) {
        isCashing = cashing;
    }

    public CooperationInfoNewEntity(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(this.taxation);
        dest.writeDouble(this.totalIncome);
        dest.writeDouble(this.realizableIncome);
        dest.writeDouble(this.fulfilledIncome);
        dest.writeDouble(this.yesterdayIncome);
        dest.writeByte((byte)(this.disable ?1:0));
        dest.writeByte((byte)(this.isSetPayPassword ?1:0));
        dest.writeByte((byte)(this.isCashing ?1:0));
    }

    protected CooperationInfoNewEntity(Parcel in) {
        this.taxation = in.readFloat();
        this.totalIncome = in.readDouble();
        this.realizableIncome = in.readDouble();
        this.fulfilledIncome = in.readDouble();
        this.yesterdayIncome = in.readDouble();
        this.disable =in.readByte()!=0;
        this.isSetPayPassword =in.readByte()!=0;
        this.isCashing =in.readByte()!=0;
    }

    public static final Creator<CooperationInfoNewEntity> CREATOR = new Creator<CooperationInfoNewEntity>() {
        @Override
        public CooperationInfoNewEntity createFromParcel(Parcel source) {
            return new CooperationInfoNewEntity(source);
        }

        @Override
        public CooperationInfoNewEntity[] newArray(int size) {
            return new CooperationInfoNewEntity[size];
        }
    };
}
