package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingyon.elevator.others.OnChoosedInterface;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class CouponItemEntity extends OnChoosedInterface implements Parcelable {

    /**
     * objctId : 1
     * coupontype : voucher
     * couponCondition : 1000
     * money : 10
     * discount : 8.8
     * adType : businessAd
     * expiredDate : 16598868464
     * status : used
     */

    private int cacheCount;
    private int couponsCount;
    private long objctId;
    private String coupontype;
    private float couponCondition;
    private float money;
    private float discount;
    private String adType;
    private long expiredDate;
    private String status;

    protected CouponItemEntity(Parcel in) {
        cacheCount = in.readInt();
        couponsCount = in.readInt();
        objctId = in.readLong();
        coupontype = in.readString();
        couponCondition = in.readFloat();
        money = in.readFloat();
        discount = in.readFloat();
        adType = in.readString();
        expiredDate = in.readLong();
        status = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(cacheCount);
        dest.writeInt(couponsCount);
        dest.writeLong(objctId);
        dest.writeString(coupontype);
        dest.writeFloat(couponCondition);
        dest.writeFloat(money);
        dest.writeFloat(discount);
        dest.writeString(adType);
        dest.writeLong(expiredDate);
        dest.writeString(status);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CouponItemEntity> CREATOR = new Creator<CouponItemEntity>() {
        @Override
        public CouponItemEntity createFromParcel(Parcel in) {
            return new CouponItemEntity(in);
        }

        @Override
        public CouponItemEntity[] newArray(int size) {
            return new CouponItemEntity[size];
        }
    };

    public int getCacheCount() {
        return cacheCount;
    }

    public void setCacheCount(int cacheCount) {
        this.cacheCount = cacheCount;
    }

    public int getCouponsCount() {
        return couponsCount;
    }

    public void setCouponsCount(int couponsCount) {
        this.couponsCount = couponsCount;
    }

    public long getObjctId() {
        return objctId;
    }

    public void setObjctId(long objctId) {
        this.objctId = objctId;
    }

    public String getCoupontype() {
        return coupontype;
    }

    public void setCoupontype(String coupontype) {
        this.coupontype = coupontype;
    }

    public float getCouponCondition() {
        return couponCondition;
    }

    public void setCouponCondition(float couponCondition) {
        this.couponCondition = couponCondition;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getAdType() {
        return adType;
    }

    public void setAdType(String adType) {
        this.adType = adType;
    }

    public long getExpiredDate() {
        return expiredDate;
    }

    public void setExpiredDate(long expiredDate) {
        this.expiredDate = expiredDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String getStringName() {
        return adType;
    }

    public CouponItemEntity() {
    }

}
