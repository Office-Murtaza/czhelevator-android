package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by lc on 2017/10/29.
 */

public class PushMessageEntity implements Parcelable {
    private String title;
    private String content;
    private String type;
    private NormalMessageEntity message;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public NormalMessageEntity getMessage() {
        return message;
    }

    public void setMessage(NormalMessageEntity message) {
        this.message = message;
    }

    public PushMessageEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.type);
        dest.writeParcelable(this.message, flags);
    }

    protected PushMessageEntity(Parcel in) {
        this.title = in.readString();
        this.content = in.readString();
        this.type = in.readString();
        this.message = in.readParcelable(NormalMessageEntity.class.getClassLoader());
    }

    public static final Creator<PushMessageEntity> CREATOR = new Creator<PushMessageEntity>() {
        @Override
        public PushMessageEntity createFromParcel(Parcel source) {
            return new PushMessageEntity(source);
        }

        @Override
        public PushMessageEntity[] newArray(int size) {
            return new PushMessageEntity[size];
        }
    };
}
