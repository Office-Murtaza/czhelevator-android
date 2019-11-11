package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2017/10/27.
 * Email：lc824767150@163.com
 */

public class InputEntity implements Parcelable {
    private int maxLength;
    private int inputType;
    private String title;
    private String text;
    private String hint;
    private int showLines;

    public InputEntity(int maxLength, int inputType, String title, String text, String hint, int showLines) {
        this.maxLength = maxLength;
        this.inputType = inputType;
        this.title = title;
        this.text = text;
        this.hint = hint;
        this.showLines = showLines;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public int getShowLines() {
        return showLines;
    }

    public void setShowLines(int showLines) {
        this.showLines = showLines;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.maxLength);
        dest.writeInt(this.inputType);
        dest.writeString(this.title);
        dest.writeString(this.text);
        dest.writeString(this.hint);
        dest.writeInt(this.showLines);
    }

    protected InputEntity(Parcel in) {
        this.maxLength = in.readInt();
        this.inputType = in.readInt();
        this.title = in.readString();
        this.text = in.readString();
        this.hint = in.readString();
        this.showLines = in.readInt();
    }

    public static final Creator<InputEntity> CREATOR = new Creator<InputEntity>() {
        @Override
        public InputEntity createFromParcel(Parcel source) {
            return new InputEntity(source);
        }

        @Override
        public InputEntity[] newArray(int size) {
            return new InputEntity[size];
        }
    };
}
