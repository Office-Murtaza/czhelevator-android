package com.kingyon.elevator.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.kingyon.elevator.entities.entities.OrderCommunityEntiy;

import java.util.List;

/**
 * Created by GongLi on 2019/1/4.
 * Email：lc824767150@163.com
 */

public class OrderDetailsEntity implements Parcelable {

    /**
     * objctId : 1
     * createTime : 1596566466565165
     * orderStatus : waitPay
     * creator : {"objctId":1,"nikeName":"TONY","phone":13888888888}
     * goodsPrice : 20000.12
     * couponPrice : 500.01
     * realPrice : 19500.01
     * reviewFaild : 内容违规
     * adStartTime : 1596566466565165
     * adEndTime : 1596566466568963
     * orderType : diyAd
     * contract : {"type":"company","companyName":"成都大众广告","industry":"美容","name":"张三","mobile":13888888888}
     * totalScreen : 2000
     * cancelReason : 用户取消
     * cancelTime : 1596566466565165
     * orderSn : 1236568646
     * payWay : alipay
     * undercastReason : 有点儿问题哦
     * informationAdContent : 便民信息内容
     * adId : 123
     * isInvoice : false
     * invoiceId : 15686463132132
     *
     *
     */

    private boolean auditLate;
    private String address;
    private float releaseingPercent;
    private String failedReason;
    private ADEntity advertising;
    private String name;
    private long payTime;
    private long remainTime;
    private String objctId;
    private long createTime;
    private String orderStatus;
    private UserEntity creator;
    private double goodsPrice;
    private double couponPrice;
    private double realPrice;
    private String reviewFaild;
    private long adStartTime;
    private long adEndTime;
    private String orderType;
    private OrderIdentityEntity contract;
    private int totalScreen;
    private String cancelReason;
    private long cancelTime;
    private String orderSn;
    private String payWay;
    private String undercastReason;
    private String informationAdContent;
    private long adId;
    private boolean isInvoice;
    private String invoiceId;
    private int numberScreen;
    private String auditState;
    private String auditContent;
    private String downSowingReason;
    private float throwProportion;
    private List<OrderCommunityEntiy> lstHousingBean;

    @Override
    public String toString() {
        return "OrderDetailsEntity{" +
                "auditLate=" + auditLate +
                ", address='" + address + '\'' +
                ", releaseingPercent=" + releaseingPercent +
                ", failedReason='" + failedReason + '\'' +
                ", advertising=" + advertising +
                ", name='" + name + '\'' +
                ", payTime=" + payTime +
                ", remainTime=" + remainTime +
                ", objctId=" + objctId +
                ", createTime=" + createTime +
                ", orderStatus='" + orderStatus + '\'' +
                ", creator=" + creator +
                ", goodsPrice=" + goodsPrice +
                ", couponPrice=" + couponPrice +
                ", realPrice=" + realPrice +
                ", reviewFaild='" + reviewFaild + '\'' +
                ", adStartTime=" + adStartTime +
                ", adEndTime=" + adEndTime +
                ", orderType='" + orderType + '\'' +
                ", contract=" + contract +
                ", totalScreen=" + totalScreen +
                ", cancelReason='" + cancelReason + '\'' +
                ", cancelTime=" + cancelTime +
                ", orderSn='" + orderSn + '\'' +
                ", payWay='" + payWay + '\'' +
                ", undercastReason='" + undercastReason + '\'' +
                ", informationAdContent='" + informationAdContent + '\'' +
                ", adId=" + adId +
                ", isInvoice=" + isInvoice +
                ", invoiceId='" + invoiceId + '\'' +
                '}';
    }

    public String getDownSowingReason() {
        return downSowingReason;
    }

    public void setDownSowingReason(String downSowingReason) {
        this.downSowingReason = downSowingReason;
    }

    public String getAuditContent() {
        return auditContent;
    }

    public void setAuditContent(String auditContent) {
        this.auditContent = auditContent;
    }

    public float getThrowProportion() {
        return throwProportion;
    }

    public void setThrowProportion(float throwProportion) {
        this.throwProportion = throwProportion;
    }

    public List<OrderCommunityEntiy> getLstHousingBean() {
        return lstHousingBean;
    }

    public void setLstHousingBean(List<OrderCommunityEntiy> lstHousingBean) {
        this.lstHousingBean = lstHousingBean;
    }

    public String getAuditState() {
        return auditState;
    }

    public void setAuditState(String auditState) {
        this.auditState = auditState;
    }

    public int getNumberScreen() {
        return numberScreen;
    }

    public void setNumberScreen(int numberScreen) {
        this.numberScreen = numberScreen;
    }

    public boolean isAuditLate() {
        return auditLate;
    }

    public void setAuditLate(boolean auditLate) {
        this.auditLate = auditLate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public float getReleaseingPercent() {
        return releaseingPercent;
    }

    public void setReleaseingPercent(float releaseingPercent) {
        this.releaseingPercent = releaseingPercent;
    }

    public String getFailedReason() {
        return failedReason;
    }

    public void setFailedReason(String failedReason) {
        this.failedReason = failedReason;
    }

    public ADEntity getAdvertising() {
        return advertising;
    }

    public void setAdvertising(ADEntity advertising) {
        this.advertising = advertising;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPayTime() {
        return payTime;
    }

    public void setPayTime(long payTime) {
        this.payTime = payTime;
    }

    public long getRemainTime() {
        return remainTime;
    }

    public void setRemainTime(long remainTime) {
        this.remainTime = remainTime;
    }

    public String getObjctId() {
        return objctId;
    }

    public void setObjctId(String objctId) {
        this.objctId = objctId;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public UserEntity getCreator() {
        return creator;
    }

    public void setCreator(UserEntity creator) {
        this.creator = creator;
    }

    public double getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(double goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public double getCouponPrice() {
        return couponPrice;
    }

    public void setCouponPrice(double couponPrice) {
        this.couponPrice = couponPrice;
    }

    public double getRealPrice() {
        return realPrice;
    }

    public void setRealPrice(double realPrice) {
        this.realPrice = realPrice;
    }

    public String getReviewFaild() {
        return reviewFaild;
    }

    public void setReviewFaild(String reviewFaild) {
        this.reviewFaild = reviewFaild;
    }

    public long getAdStartTime() {
        return adStartTime;
    }

    public void setAdStartTime(long adStartTime) {
        this.adStartTime = adStartTime;
    }

    public long getAdEndTime() {
        return adEndTime;
    }

    public void setAdEndTime(long adEndTime) {
        this.adEndTime = adEndTime;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public OrderIdentityEntity getContract() {
        return contract;
    }

    public void setContract(OrderIdentityEntity contract) {
        this.contract = contract;
    }

    public int getTotalScreen() {
        return totalScreen;
    }

    public void setTotalScreen(int totalScreen) {
        this.totalScreen = totalScreen;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public long getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(long cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getOrderSn() {
        return orderSn;
    }

    public void setOrderSn(String orderSn) {
        this.orderSn = orderSn;
    }

    public String getPayWay() {
        return payWay;
    }

    public void setPayWay(String payWay) {
        this.payWay = payWay;
    }

    public String getUndercastReason() {
        return undercastReason;
    }

    public void setUndercastReason(String undercastReason) {
        this.undercastReason = undercastReason;
    }

    public String getInformationAdContent() {
        return informationAdContent;
    }

    public void setInformationAdContent(String informationAdContent) {
        this.informationAdContent = informationAdContent;
    }

    public long getAdId() {
        return adId;
    }

    public void setAdId(long adId) {
        this.adId = adId;
    }

    public boolean isInvoice() {
        return isInvoice;
    }

    public void setInvoice(boolean invoice) {
        isInvoice = invoice;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public OrderDetailsEntity() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.address);
        dest.writeFloat(this.releaseingPercent);
        dest.writeString(this.failedReason);
        dest.writeParcelable(this.advertising, flags);
        dest.writeString(this.name);
        dest.writeLong(this.payTime);
        dest.writeLong(this.remainTime);
        dest.writeString(this.objctId);
        dest.writeLong(this.createTime);
        dest.writeString(this.orderStatus);
        dest.writeParcelable(this.creator, flags);
        dest.writeDouble(this.goodsPrice);
        dest.writeDouble(this.couponPrice);
        dest.writeDouble(this.realPrice);
        dest.writeString(this.reviewFaild);
        dest.writeLong(this.adStartTime);
        dest.writeLong(this.adEndTime);
        dest.writeString(this.orderType);
        dest.writeParcelable(this.contract, flags);
        dest.writeInt(this.totalScreen);
        dest.writeString(this.cancelReason);
        dest.writeLong(this.cancelTime);
        dest.writeString(this.orderSn);
        dest.writeString(this.payWay);
        dest.writeString(this.undercastReason);
        dest.writeString(this.informationAdContent);
        dest.writeLong(this.adId);
        dest.writeByte(this.isInvoice ? (byte) 1 : (byte) 0);
        dest.writeString(this.invoiceId);
    }

    protected OrderDetailsEntity(Parcel in) {
        this.address = in.readString();
        this.releaseingPercent = in.readFloat();
        this.failedReason = in.readString();
        this.advertising = in.readParcelable(ADEntity.class.getClassLoader());
        this.name = in.readString();
        this.payTime = in.readLong();
        this.remainTime = in.readLong();
        this.objctId = in.readString();
        this.createTime = in.readLong();
        this.orderStatus = in.readString();
        this.creator = in.readParcelable(UserEntity.class.getClassLoader());
        this.goodsPrice = in.readDouble();
        this.couponPrice = in.readDouble();
        this.realPrice = in.readDouble();
        this.reviewFaild = in.readString();
        this.adStartTime = in.readLong();
        this.adEndTime = in.readLong();
        this.orderType = in.readString();
        this.contract = in.readParcelable(OrderIdentityEntity.class.getClassLoader());
        this.totalScreen = in.readInt();
        this.cancelReason = in.readString();
        this.cancelTime = in.readLong();
        this.orderSn = in.readString();
        this.payWay = in.readString();
        this.undercastReason = in.readString();
        this.informationAdContent = in.readString();
        this.adId = in.readLong();
        this.isInvoice = in.readByte() != 0;
        this.invoiceId = in.readString();
    }

    public static final Creator<OrderDetailsEntity> CREATOR = new Creator<OrderDetailsEntity>() {
        @Override
        public OrderDetailsEntity createFromParcel(Parcel source) {
            return new OrderDetailsEntity(source);
        }

        @Override
        public OrderDetailsEntity[] newArray(int size) {
            return new OrderDetailsEntity[size];
        }
    };
}
