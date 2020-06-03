package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingyon.elevator.others.OnChoosedInterface;

import java.util.ArrayList;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class CellItemEntity extends OnChoosedInterface implements Parcelable {

    /**
     * objctId : 1
     * cellName : 每年广场
     * address : 天府五接93号
     * liftNum : 7
     * unitNum : 6
     * distance : 100
     * longitude : 104.07
     * latitude : 30.67
     * enterTime : 15230698659696
     * cellType : office_build
     * humanTraffic : 336
     * businessAdPrice : 333.06
     * diyAdPrice : 662
     * informationAdPrice : 112
     * cellLogo : http://gc.zbj.com/upimg/6/2015/1025/20151025204911_14101.jpg
     */



    private String regionName;
    private int totalScreenNum;
    private int choosedScreenNum;
    private boolean canEdit;
    private int planPosition;
    private long collectTime;
    private ArrayList<PointItemEntity> points;
    private ArrayList<PointItemEntity> allPoints;
    private int targetScreenNum;
    private boolean deleteCache;
    private String planTypeCache;
    private long objctId;
    private String cellName;
    private String address;
    private int liftNum;
    private int unitNum;
    private float distance;
    private double longitude;
    private double latitude;
    private long enterTime;
    private String cellType;
    private int humanTraffic;
    private float businessAdPrice;
    private float diyAdPrice;
    private float informationAdPrice;
    private String cellLogo;
    private double originalBusinessAdPrice;
    private double originalDiyAdPrice;
    private double originalInformationAdPrice;


    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getTotalScreenNum() {
        return totalScreenNum;
    }

    public void setTotalScreenNum(int totalScreenNum) {
        this.totalScreenNum = totalScreenNum;
    }

    public int getChoosedScreenNum() {
        return choosedScreenNum;
    }

    public void setChoosedScreenNum(int choosedScreenNum) {
        this.choosedScreenNum = choosedScreenNum;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }

    public int getPlanPosition() {
        return planPosition;
    }

    public void setPlanPosition(int planPosition) {
        this.planPosition = planPosition;
    }

    public long getCollectTime() {
        return collectTime;
    }

    public void setCollectTime(long collectTime) {
        this.collectTime = collectTime;
    }

    public ArrayList<PointItemEntity> getPoints() {
        return points;
    }

    public void setPoints(ArrayList<PointItemEntity> points) {
        this.points = points;
    }

    public ArrayList<PointItemEntity> getAllPoints() {
        return allPoints;
    }

    public void setAllPoints(ArrayList<PointItemEntity> allPoints) {
        this.allPoints = allPoints;
    }

    public int getTargetScreenNum() {
        return targetScreenNum;
    }

    public void setTargetScreenNum(int targetScreenNum) {
        this.targetScreenNum = targetScreenNum;
    }

    public boolean isDeleteCache() {
        return deleteCache;
    }

    public void setDeleteCache(boolean deleteCache) {
        this.deleteCache = deleteCache;
    }

    public String getPlanTypeCache() {
        return planTypeCache;
    }

    public void setPlanTypeCache(String planTypeCache) {
        this.planTypeCache = planTypeCache;
    }

    public long getObjctId() {
        return objctId;
    }

    public void setObjctId(long objctId) {
        this.objctId = objctId;
    }

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getLiftNum() {
        return liftNum;
    }

    public void setLiftNum(int liftNum) {
        this.liftNum = liftNum;
    }

    public int getUnitNum() {
        return unitNum;
    }

    public void setUnitNum(int unitNum) {
        this.unitNum = unitNum;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public long getEnterTime() {
        return enterTime;
    }

    public void setEnterTime(long enterTime) {
        this.enterTime = enterTime;
    }

    public String getCellType() {
        return cellType;
    }

    public void setCellType(String cellType) {
        this.cellType = cellType;
    }

    public int getHumanTraffic() {
        return humanTraffic;
    }

    public void setHumanTraffic(int humanTraffic) {
        this.humanTraffic = humanTraffic;
    }

    public float getBusinessAdPrice() {
        return businessAdPrice;
    }

    public void setBusinessAdPrice(float businessAdPrice) {
        this.businessAdPrice = businessAdPrice;
    }

    public float getDiyAdPrice() {
        return diyAdPrice;
    }

    public void setDiyAdPrice(float diyAdPrice) {
        this.diyAdPrice = diyAdPrice;
    }

    public float getInformationAdPrice() {
        return informationAdPrice;
    }

    public void setInformationAdPrice(float informationAdPrice) {
        this.informationAdPrice = informationAdPrice;
    }

    public String getCellLogo() {
        return cellLogo;
    }

    public void setCellLogo(String cellLogo) {
        this.cellLogo = cellLogo;
    }

    public double getOriginalBusinessAdPrice() {
        return originalBusinessAdPrice;
    }

    public void setOriginalBusinessAdPrice(double originalBusinessAdPrice) {
        this.originalBusinessAdPrice = originalBusinessAdPrice;
    }

    public double getOriginalDiyAdPrice() {
        return originalDiyAdPrice;
    }

    public void setOriginalDiyAdPrice(double originalDiyAdPrice) {
        this.originalDiyAdPrice = originalDiyAdPrice;
    }

    public double getOriginalInformationAdPrice() {
        return originalInformationAdPrice;
    }

    public void setOriginalInformationAdPrice(double originalInformationAdPrice) {
        this.originalInformationAdPrice = originalInformationAdPrice;
    }

    @Override
    public String getStringName() {
        return cellName;
    }

    public CellItemEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.totalScreenNum);
        dest.writeInt(this.choosedScreenNum);
        dest.writeByte(this.canEdit ? (byte) 1 : (byte) 0);
        dest.writeInt(this.planPosition);
        dest.writeLong(this.collectTime);
        dest.writeTypedList(this.points);
        dest.writeTypedList(this.allPoints);
        dest.writeInt(this.targetScreenNum);
        dest.writeByte(this.deleteCache ? (byte) 1 : (byte) 0);
        dest.writeString(this.planTypeCache);
        dest.writeLong(this.objctId);
        dest.writeString(this.cellName);
        dest.writeString(this.address);
        dest.writeInt(this.liftNum);
        dest.writeInt(this.unitNum);
        dest.writeFloat(this.distance);
        dest.writeDouble(this.longitude);
        dest.writeDouble(this.latitude);
        dest.writeLong(this.enterTime);
        dest.writeString(this.cellType);
        dest.writeInt(this.humanTraffic);
        dest.writeFloat(this.businessAdPrice);
        dest.writeFloat(this.diyAdPrice);
        dest.writeFloat(this.informationAdPrice);
        dest.writeString(this.cellLogo);
        dest.writeDouble(this.originalBusinessAdPrice);
        dest.writeDouble(this.originalDiyAdPrice);
        dest.writeDouble(this.originalInformationAdPrice);
    }

    protected CellItemEntity(Parcel in) {
        this.totalScreenNum = in.readInt();
        this.choosedScreenNum = in.readInt();
        this.canEdit = in.readByte() != 0;
        this.planPosition = in.readInt();
        this.collectTime = in.readLong();
        this.points = in.createTypedArrayList(PointItemEntity.CREATOR);
        this.allPoints = in.createTypedArrayList(PointItemEntity.CREATOR);
        this.targetScreenNum = in.readInt();
        this.deleteCache = in.readByte() != 0;
        this.planTypeCache = in.readString();
        this.objctId = in.readLong();
        this.cellName = in.readString();
        this.address = in.readString();
        this.liftNum = in.readInt();
        this.unitNum = in.readInt();
        this.distance = in.readFloat();
        this.longitude = in.readDouble();
        this.latitude = in.readDouble();
        this.enterTime = in.readLong();
        this.cellType = in.readString();
        this.humanTraffic = in.readInt();
        this.businessAdPrice = in.readFloat();
        this.diyAdPrice = in.readFloat();
        this.informationAdPrice = in.readFloat();
        this.cellLogo = in.readString();
        this.originalBusinessAdPrice = in.readDouble();
        this.originalDiyAdPrice = in.readDouble();
        this.originalInformationAdPrice = in.readDouble();
    }

    public static final Creator<CellItemEntity> CREATOR = new Creator<CellItemEntity>() {
        @Override
        public CellItemEntity createFromParcel(Parcel source) {
            return new CellItemEntity(source);
        }

        @Override
        public CellItemEntity[] newArray(int size) {
            return new CellItemEntity[size];
        }
    };
}
