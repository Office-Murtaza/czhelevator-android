package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyIdentityEntity implements Parcelable {


    /**
     * personName : 联系人A
     * phone : 028123456
     * faildReason : 信息不真实，弄虚作假。     信息不真实，弄虚作假。
     * status : AUTHING
     * companyName : 金翼致远
     * certificate :
     */

    private String personName;
    private String phone;
    private String faildReason;
    private String status;
    private String companyName;
    private String certificate;

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFaildReason() {
        return faildReason;
    }

    public void setFaildReason(String faildReason) {
        this.faildReason = faildReason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCertificate() {
        return certificate;
    }

    public void setCertificate(String certificate) {
        this.certificate = certificate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.personName);
        dest.writeString(this.phone);
        dest.writeString(this.faildReason);
        dest.writeString(this.status);
        dest.writeString(this.companyName);
        dest.writeString(this.certificate);
    }

    public PropertyIdentityEntity() {
    }

    protected PropertyIdentityEntity(Parcel in) {
        this.personName = in.readString();
        this.phone = in.readString();
        this.faildReason = in.readString();
        this.status = in.readString();
        this.companyName = in.readString();
        this.certificate = in.readString();
    }

    public static final Creator<PropertyIdentityEntity> CREATOR = new Creator<PropertyIdentityEntity>() {
        @Override
        public PropertyIdentityEntity createFromParcel(Parcel source) {
            return new PropertyIdentityEntity(source);
        }

        @Override
        public PropertyIdentityEntity[] newArray(int size) {
            return new PropertyIdentityEntity[size];
        }
    };
}
