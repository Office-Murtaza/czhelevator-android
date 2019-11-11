package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2019/1/24.
 * Email：lc824767150@163.com
 */

public class OrderIdentityEntity implements Parcelable {

    /**
     * state :
     * name :
     * companyName  :
     * type  :
     * industry :
     * mobile  :
     * typeCertification :
     */

    private String state;
    private String name;
    private String companyName;
    private String type;
    private String industry;
    private String mobile;
    private String typeCertification;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTypeCertification() {
        return typeCertification;
    }

    public void setTypeCertification(String typeCertification) {
        this.typeCertification = typeCertification;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.state);
        dest.writeString(this.name);
        dest.writeString(this.companyName);
        dest.writeString(this.type);
        dest.writeString(this.industry);
        dest.writeString(this.mobile);
        dest.writeString(this.typeCertification);
    }

    public OrderIdentityEntity() {
    }

    protected OrderIdentityEntity(Parcel in) {
        this.state = in.readString();
        this.name = in.readString();
        this.companyName = in.readString();
        this.type = in.readString();
        this.industry = in.readString();
        this.mobile = in.readString();
        this.typeCertification = in.readString();
    }

    public static final Parcelable.Creator<OrderIdentityEntity> CREATOR = new Parcelable.Creator<OrderIdentityEntity>() {
        @Override
        public OrderIdentityEntity createFromParcel(Parcel source) {
            return new OrderIdentityEntity(source);
        }

        @Override
        public OrderIdentityEntity[] newArray(int size) {
            return new OrderIdentityEntity[size];
        }
    };
}
