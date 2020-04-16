package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by GongLi on 2018/12/26.
 * Email：lc824767150@163.com
 */

public class ADEntity implements Parcelable {

    /**
     * objctId : 1
     * adType : fullVideo
     * adStatus : waitReview
     * videoUrl : 视频地址
     * imageUrl : 图片地址
     * bgMusic : 背景音乐
     * faildReason : 失败原因
     * title :
     * adIndustry :
     * adIndustryId : 1
     */

    private boolean onlyInfo;
    private long objctId;
    private String planType;
    private String screenType;
    private String adStatus;
    private String videoUrl;
    private String imageUrl;
    private String bgMusic;
    private String faildReason;
    private String title;
    private String adIndustry;
    private long adIndustryId;

    @Override
    public String toString() {
        return "ADEntity{" +
                "onlyInfo=" + onlyInfo +
                ", objctId=" + objctId +
                ", planType='" + planType + '\'' +
                ", screenType='" + screenType + '\'' +
                ", adStatus='" + adStatus + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", bgMusic='" + bgMusic + '\'' +
                ", faildReason='" + faildReason + '\'' +
                ", title='" + title + '\'' +
                ", adIndustry='" + adIndustry + '\'' +
                ", adIndustryId=" + adIndustryId +
                '}';
    }

    public boolean isOnlyInfo() {
        return onlyInfo;
    }

    public void setOnlyInfo(boolean onlyInfo) {
        this.onlyInfo = onlyInfo;
    }

    public long getObjctId() {
        return objctId;
    }

    public void setObjctId(long objctId) {
        this.objctId = objctId;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String getScreenType() {
        return screenType;
    }

    public void setScreenType(String screenType) {
        this.screenType = screenType;
    }

    public String getAdStatus() {
        return adStatus;
    }

    public void setAdStatus(String adStatus) {
        this.adStatus = adStatus;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBgMusic() {
        return bgMusic;
    }

    public void setBgMusic(String bgMusic) {
        this.bgMusic = bgMusic;
    }

    public String getFaildReason() {
        return faildReason;
    }

    public void setFaildReason(String faildReason) {
        this.faildReason = faildReason;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAdIndustry() {
        return adIndustry;
    }

    public void setAdIndustry(String adIndustry) {
        this.adIndustry = adIndustry;
    }

    public long getAdIndustryId() {
        return adIndustryId;
    }

    public void setAdIndustryId(long adIndustryId) {
        this.adIndustryId = adIndustryId;
    }

    public ADEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.onlyInfo ? (byte) 1 : (byte) 0);
        dest.writeLong(this.objctId);
        dest.writeString(this.planType);
        dest.writeString(this.screenType);
        dest.writeString(this.adStatus);
        dest.writeString(this.videoUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.bgMusic);
        dest.writeString(this.faildReason);
        dest.writeString(this.title);
        dest.writeString(this.adIndustry);
        dest.writeLong(this.adIndustryId);
    }

    protected ADEntity(Parcel in) {
        this.onlyInfo = in.readByte() != 0;
        this.objctId = in.readLong();
        this.planType = in.readString();
        this.screenType = in.readString();
        this.adStatus = in.readString();
        this.videoUrl = in.readString();
        this.imageUrl = in.readString();
        this.bgMusic = in.readString();
        this.faildReason = in.readString();
        this.title = in.readString();
        this.adIndustry = in.readString();
        this.adIndustryId = in.readLong();
    }

    public static final Creator<ADEntity> CREATOR = new Creator<ADEntity>() {
        @Override
        public ADEntity createFromParcel(Parcel source) {
            return new ADEntity(source);
        }

        @Override
        public ADEntity[] newArray(int size) {
            return new ADEntity[size];
        }
    };
}
