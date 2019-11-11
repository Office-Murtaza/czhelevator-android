package com.kingyon.elevator.entities;

import java.util.List;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class CellDetailsEntity {

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
     * cellBanner : [{"objectId":1,"url":"http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg"},{"objectId":1,"url":"http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg"},{"objectId":1,"url":"http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg"},{"objectId":1,"url":"http://img.zcool.cn/community/0139735a90f9c0a8012192310823b3.jpg@1280w_1l_2o_100sh.jpg"}]
     * isCollect : 1
     */

    private String businessIntro;
    private String diyIntro;
    private String informationIntro;
    private long regionCode;
    private String regionName;
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
    private long humanTraffic;
    private float businessAdPrice;
    private float diyAdPrice;
    private float informationAdPrice;
    private String cellLogo;
    private boolean isCollect;
    private List<String> cellBanner;

    public String getBusinessIntro() {
        return businessIntro;
    }

    public void setBusinessIntro(String businessIntro) {
        this.businessIntro = businessIntro;
    }

    public String getDiyIntro() {
        return diyIntro;
    }

    public void setDiyIntro(String diyIntro) {
        this.diyIntro = diyIntro;
    }

    public String getInformationIntro() {
        return informationIntro;
    }

    public void setInformationIntro(String informationIntro) {
        this.informationIntro = informationIntro;
    }

    public long getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(long regionCode) {
        this.regionCode = regionCode;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
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

    public long getHumanTraffic() {
        return humanTraffic;
    }

    public void setHumanTraffic(long humanTraffic) {
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

    public boolean isCollect() {
        return isCollect;
    }

    public void setCollect(boolean collect) {
        isCollect = collect;
    }

    public List<String> getCellBanner() {
        return cellBanner;
    }

    public void setCellBanner(List<String> cellBanner) {
        this.cellBanner = cellBanner;
    }
}
