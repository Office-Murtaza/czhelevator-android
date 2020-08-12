package com.kingyon.elevator.entities.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @Created By Admin  on 2020/8/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderComeEntiy implements Parcelable {

        public int housingInfoId;
        public String housingName;
        public int occupationNum;
        public List<Integer> facilityIds;

    @Override
    public String toString() {
        return "OrderComeEntiy{" +
                "housingInfoId=" + housingInfoId +
                ", housingName='" + housingName + '\'' +
                ", occupationNum=" + occupationNum +
                ", facilityIds=" + facilityIds +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.housingInfoId);
        dest.writeString(this.housingName);
        dest.writeInt(this.occupationNum);
        dest.writeList(this.facilityIds);
    }

    public OrderComeEntiy() {
    }

    protected OrderComeEntiy(Parcel in) {
        this.housingInfoId = in.readInt();
        this.housingName = in.readString();
        this.occupationNum = in.readInt();
        this.facilityIds = new ArrayList<Integer>();
        in.readList(this.facilityIds, Integer.class.getClassLoader());
    }

    public static final Parcelable.Creator<OrderComeEntiy> CREATOR = new Parcelable.Creator<OrderComeEntiy>() {
        @Override
        public OrderComeEntiy createFromParcel(Parcel source) {
            return new OrderComeEntiy(source);
        }

        @Override
        public OrderComeEntiy[] newArray(int size) {
            return new OrderComeEntiy[size];
        }
    };
}
