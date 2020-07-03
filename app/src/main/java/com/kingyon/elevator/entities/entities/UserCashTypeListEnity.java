package com.kingyon.elevator.entities.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @Created By Admin  on 2020/6/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class UserCashTypeListEnity implements Parcelable {
    /*
    *   "id": 4,
      "cashType": 2,
      "cashAccount": "123456",
      "cashName": "项瑞请",
      "openingBank": ""
      *
      *
    * */
    public int id;
    public int cashType;
    public String cashAccount;
    public String cashName;
    public String openingBank;

    public UserCashTypeListEnity(Parcel in) {
        id = in.readInt();
        cashType = in.readInt();
        cashAccount = in.readString();
        cashName = in.readString();
        openingBank = in.readString();
    }

    public static final Creator<UserCashTypeListEnity> CREATOR = new Creator<UserCashTypeListEnity>() {
        @Override
        public UserCashTypeListEnity createFromParcel(Parcel in) {
            return new UserCashTypeListEnity(in);
        }

        @Override
        public UserCashTypeListEnity[] newArray(int size) {
            return new UserCashTypeListEnity[size];
        }
    };

    @Override
    public String toString() {
        return "UserCashTypeListEnity{" +
                "id=" + id +
                ", cashType=" + cashType +
                ", cashAccount='" + cashAccount + '\'' +
                ", cashName='" + cashName + '\'' +
                ", openingBank='" + openingBank + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(cashType);
        dest.writeString(cashAccount);
        dest.writeString(cashName);
        dest.writeString(openingBank);
    }
}
