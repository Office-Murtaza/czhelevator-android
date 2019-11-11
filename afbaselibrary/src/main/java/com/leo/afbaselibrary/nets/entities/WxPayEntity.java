package com.leo.afbaselibrary.nets.entities;

/**
 * Created by GongLi on 2017/10/30.
 * Email：lc824767150@163.com
 */

public class WxPayEntity {

    /**
     * appid :
     * noncestr :
     * packages :
     * partnerid :
     * prepayid :
     * sign :
     * timestamp :
     */

    private String payType;
    private String appid;
    private String noncestr;
    private String packages;
    private String partnerid;
    private String prepayid;
    private String sign;
    private String timestamp;

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
