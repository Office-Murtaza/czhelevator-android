package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

public class CooperationIdentityEntity implements Parcelable {

    /**
     * personName : name
     * phone : 511321785555
     * faildReason :
     * status : auth
     */

    private String personName;
    private String phone;
    private String faildReason;
    private String status;
    private AMapCityEntity city;

    @Override
    public String toString() {
        return "CooperationIdentityEntity{" +
                "personName='" + personName + '\'' +
                ", phone='" + phone + '\'' +
                ", faildReason='" + faildReason + '\'' +
                ", status='" + status + '\'' +
                ", city=" + city +
                '}';
    }

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

    public AMapCityEntity getCity() {
        return city;
    }

    public void setCity(AMapCityEntity city) {
        this.city = city;
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
        dest.writeParcelable(this.city, flags);
    }

    public CooperationIdentityEntity() {
    }

    protected CooperationIdentityEntity(Parcel in) {
        this.personName = in.readString();
        this.phone = in.readString();
        this.faildReason = in.readString();
        this.status = in.readString();
        this.city = in.readParcelable(AMapCityEntity.class.getClassLoader());
    }

    public static final Parcelable.Creator<CooperationIdentityEntity> CREATOR = new Parcelable.Creator<CooperationIdentityEntity>() {
        @Override
        public CooperationIdentityEntity createFromParcel(Parcel source) {
            return new CooperationIdentityEntity(source);
        }

        @Override
        public CooperationIdentityEntity[] newArray(int size) {
            return new CooperationIdentityEntity[size];
        }
    };
}
