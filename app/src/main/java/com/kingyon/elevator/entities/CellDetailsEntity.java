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

    /**
     *
     "objctId": 100042,
     "cellName": "梯联网观光小区",
     "address": "贵州省贵阳市观山湖区金岭社区服务中心都匀路金利大厦(百挑路)",
     "liftNum": 3,
     "unitNum": 1,
     "distance": 0,
     "longitude": 106.648306,
     "latitude": 26.616736,
     "enterTime": 1562256000000,
     "cellType": "OFFICE_BUILD",
     "humanTraffic": 6000,
     "businessAdPrice": 10,
     "diyAdPrice": 200,
     "informationAdPrice": 0,
     "cellLogo": "http:\/\/cdn.tlwgz.com\/\/276d5343-812f-4f92-924d-9e6d700bbff4.jpg",
     "isCollect": false,
     "cellBanner": [
     "http:\/\/cdn.tlwgz.com\/\/979aa122-60f7-401c-878e-0f6617a52624.jpg"
     ],
     "regionCode": 520115,
     "regionName": "贵州省贵阳市观山湖区",
     "businessIntro": null,
     "diyIntro": null,
     "informationIntro": null,
     "collect": false
     *
     * */

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

    @Override
    public String toString() {
        return "CellDetailsEntity{" +
                "businessIntro='" + businessIntro + '\'' +
                ", diyIntro='" + diyIntro + '\'' +
                ", informationIntro='" + informationIntro + '\'' +
                ", regionCode=" + regionCode +
                ", regionName='" + regionName + '\'' +
                ", objctId=" + objctId +
                ", cellName='" + cellName + '\'' +
                ", address='" + address + '\'' +
                ", liftNum=" + liftNum +
                ", unitNum=" + unitNum +
                ", distance=" + distance +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", enterTime=" + enterTime +
                ", cellType='" + cellType + '\'' +
                ", humanTraffic=" + humanTraffic +
                ", businessAdPrice=" + businessAdPrice +
                ", diyAdPrice=" + diyAdPrice +
                ", informationAdPrice=" + informationAdPrice +
                ", cellLogo='" + cellLogo + '\'' +
                ", isCollect=" + isCollect +
                ", cellBanner=" + cellBanner +
                '}';
    }

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
