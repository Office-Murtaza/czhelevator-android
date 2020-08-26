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
    private Boolean expand;

    public boolean isExpand() {
        return expand;
    }

    public void setExpand(boolean expand) {
        this.expand = expand;
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.cacheCount);
        dest.writeInt(this.couponsCount);
        dest.writeLong(this.objctId);
        dest.writeString(this.coupontype);
        dest.writeFloat(this.couponCondition);
        dest.writeFloat(this.money);
        dest.writeFloat(this.discount);
        dest.writeString(this.adType);
        dest.writeLong(this.expiredDate);
        dest.writeString(this.status);
        dest.writeValue(this.expand);
    }

    protected CouponItemEntity(Parcel in) {
        this.cacheCount = in.readInt();
        this.couponsCount = in.readInt();
        this.objctId = in.readLong();
        this.coupontype = in.readString();
        this.couponCondition = in.readFloat();
        this.money = in.readFloat();
        this.discount = in.readFloat();
        this.adType = in.readString();
        this.expiredDate = in.readLong();
        this.status = in.readString();
        this.expand = (Boolean) in.readValue(Boolean.class.getClassLoader());
    }

    public static final Creator<CouponItemEntity> CREATOR = new Creator<CouponItemEntity>() {
        @Override
        public CouponItemEntity createFromParcel(Parcel source) {
            return new CouponItemEntity(source);
        }

        @Override
        public CouponItemEntity[] newArray(int size) {
            return new CouponItemEntity[size];
        }
    };
}
