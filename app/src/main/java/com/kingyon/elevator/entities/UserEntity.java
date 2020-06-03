package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2018/12/24.
 * Email：lc824767150@163.com
 */

public class UserEntity implements Parcelable {

    /**
     * objectId : 1
     * account
     * avatar : http://www.ghost64.com/qqtupian/zixunImg/local/2017/12/28/15144398193808.jpg
     * nikeName : TONY
     * phone : 13888888888
     * role : normal
     * isAuthentication : false
     * isBindPhone : false
     */

    private Long recommendTime;
    private long objectId;
    private String account;
    private String avatar;
    private String nikeName;
    private String phone;
    private String role;
    private String authStatus;
    private String fullName;
    private boolean isBindPhone;

    public String getFullname() {
        return fullName;
    }

    public void setFullname(String fullName) {
        this.fullName = fullName;
    }
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public Long getRecommendTime() {
        return recommendTime;
    }

    public void setRecommendTime(Long recommendTime) {
        this.recommendTime = recommendTime;
    }

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public boolean isBindPhone() {
        return isBindPhone;
    }

    public void setBindPhone(boolean bindPhone) {
        isBindPhone = bindPhone;
    }

    public UserEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.objectId);
        dest.writeString(this.avatar);
        dest.writeString(this.nikeName);
        dest.writeString(this.phone);
        dest.writeString(this.role);
        dest.writeString(this.authStatus);
        dest.writeByte(this.isBindPhone ? (byte) 1 : (byte) 0);
    }

    protected UserEntity(Parcel in) {
        this.objectId = in.readLong();
        this.avatar = in.readString();
        this.nikeName = in.readString();
        this.phone = in.readString();
        this.role = in.readString();
        this.authStatus = in.readString();
        this.isBindPhone = in.readByte() != 0;
    }

    public static final Creator<UserEntity> CREATOR = new Creator<UserEntity>() {
        @Override
        public UserEntity createFromParcel(Parcel source) {
            return new UserEntity(source);
        }

        @Override
        public UserEntity[] newArray(int size) {
            return new UserEntity[size];
        }
    };
}
