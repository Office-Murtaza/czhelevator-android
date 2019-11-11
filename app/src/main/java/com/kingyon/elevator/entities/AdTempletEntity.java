package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by GongLi on 2019/1/21.
 * Email：lc824767150@163.com
 */

public class AdTempletEntity implements Parcelable {

    /**
     * objectId : 1
     * name : 广告模板名称
     * cover : 封面图
     * element : [{"objectId":1,"type":"","leftTop":0.05,"rightBottom":0.05}]
     */

    private long objectId;
    private String name;
    private String cover;
    private long time;
    private List<ElementBean> element;

    public long getObjectId() {
        return objectId;
    }

    public void setObjectId(long objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public List<ElementBean> getElement() {
        return element;
    }

    public void setElement(List<ElementBean> element) {
        this.element = element;
    }

    public static class ElementBean implements Parcelable {
        /**
         * objectId : 1
         * type :
         * leftTop : 0.05
         * rightBottom : 0.05
         */

        private long objectId;
        private String type;
        private float left;
        private float top;
        private float right;
        private float bottom;
        private String resource;
        private String textColor;
        private String hintColor;
        private int textSize;

        public long getObjectId() {
            return objectId;
        }

        public void setObjectId(long objectId) {
            this.objectId = objectId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public float getLeft() {
            return left;
        }

        public void setLeft(float left) {
            this.left = left;
        }

        public float getTop() {
            return top;
        }

        public void setTop(float top) {
            this.top = top;
        }

        public float getRight() {
            return right;
        }

        public void setRight(float right) {
            this.right = right;
        }

        public float getBottom() {
            return bottom;
        }

        public void setBottom(float bottom) {
            this.bottom = bottom;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public String getTextColor() {
            return textColor;
        }

        public void setTextColor(String textColor) {
            this.textColor = textColor;
        }

        public String getHintColor() {
            return hintColor;
        }

        public void setHintColor(String hintColor) {
            this.hintColor = hintColor;
        }

        public int getTextSize() {
            return textSize;
        }

        public void setTextSize(int textSize) {
            this.textSize = textSize;
        }

        public ElementBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.objectId);
            dest.writeString(this.type);
            dest.writeFloat(this.left);
            dest.writeFloat(this.top);
            dest.writeFloat(this.right);
            dest.writeFloat(this.bottom);
            dest.writeString(this.resource);
            dest.writeString(this.textColor);
            dest.writeString(this.hintColor);
            dest.writeInt(this.textSize);
        }

        protected ElementBean(Parcel in) {
            this.objectId = in.readLong();
            this.type = in.readString();
            this.left = in.readFloat();
            this.top = in.readFloat();
            this.right = in.readFloat();
            this.bottom = in.readFloat();
            this.resource = in.readString();
            this.textColor = in.readString();
            this.hintColor = in.readString();
            this.textSize = in.readInt();
        }

        public static final Creator<ElementBean> CREATOR = new Creator<ElementBean>() {
            @Override
            public ElementBean createFromParcel(Parcel source) {
                return new ElementBean(source);
            }

            @Override
            public ElementBean[] newArray(int size) {
                return new ElementBean[size];
            }
        };
    }

    public AdTempletEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.cover);
        dest.writeLong(this.time);
        dest.writeTypedList(this.element);
    }

    protected AdTempletEntity(Parcel in) {
        this.objectId = in.readLong();
        this.name = in.readString();
        this.cover = in.readString();
        this.time = in.readLong();
        this.element = in.createTypedArrayList(ElementBean.CREATOR);
    }

    public static final Creator<AdTempletEntity> CREATOR = new Creator<AdTempletEntity>() {
        @Override
        public AdTempletEntity createFromParcel(Parcel source) {
            return new AdTempletEntity(source);
        }

        @Override
        public AdTempletEntity[] newArray(int size) {
            return new AdTempletEntity[size];
        }
    };
}
