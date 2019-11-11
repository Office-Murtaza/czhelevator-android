package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2019/1/9.
 * Email：lc824767150@163.com
 */

public class NormalMessageEntity implements Parcelable {

    /**
     * objectId : 1
     * title : 你的省份已经头痛审核
     * content : 你的省份已经头痛审核
     * type : auth
     * extraId : 1236
     */

    private long objectId;
    private String title;
    private String content;
    private String type;
    private long extraId;
    private long time;
    private boolean unRead;
    private String image;
    private String link;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

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

    public long getExtraId() {
        return extraId;
    }

    public void setExtraId(long extraId) {
        this.extraId = extraId;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isUnRead() {
        return unRead;
    }

    public void setUnRead(boolean unRead) {
        this.unRead = unRead;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.objectId);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.type);
        dest.writeLong(this.extraId);
        dest.writeLong(this.time);
        dest.writeByte(this.unRead ? (byte) 1 : (byte) 0);
        dest.writeString(this.image);
        dest.writeString(this.link);
    }

    public NormalMessageEntity() {
    }

    protected NormalMessageEntity(Parcel in) {
        this.objectId = in.readLong();
        this.title = in.readString();
        this.content = in.readString();
        this.type = in.readString();
        this.extraId = in.readLong();
        this.time = in.readLong();
        this.unRead = in.readByte() != 0;
        this.image = in.readString();
        this.link = in.readString();
    }

    public static final Creator<NormalMessageEntity> CREATOR = new Creator<NormalMessageEntity>() {
        @Override
        public NormalMessageEntity createFromParcel(Parcel source) {
            return new NormalMessageEntity(source);
        }

        @Override
        public NormalMessageEntity[] newArray(int size) {
            return new NormalMessageEntity[size];
        }
    };
}
