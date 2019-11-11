package com.kingyon.elevator.entities;

import com.kingyon.elevator.R;
import com.kingyon.library.social.ShareEntity;

import java.util.List;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class RecommendInfoEntity implements ShareEntity {

    /**
     * recommendPersons : 100
     * receiveCoupons : 50
     * coupons : [{"objctId":1,"coupontype":"voucher","couponCondition":1000,"money":"10","discount":8.8,"adType":"businessAd","expiredDate":16598868464,"status":"used"},{"objctId":1,"coupontype":"voucher","couponCondition":1000,"money":"10","discount":8.8,"adType":"businessAd","expiredDate":16598868464,"status":"used"}]
     */

    private String shareTitle;
    private String shareContent;
    private String shareLink;
    private int recommendPersons;
    private int receiveCoupons;
    private List<CouponItemEntity> coupons;

    public void setShareTitle(String shareTitle) {
        this.shareTitle = shareTitle;
    }

    public String getShareContent() {
        return shareContent;
    }

    public void setShareContent(String shareContent) {
        this.shareContent = shareContent;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public int getRecommendPersons() {
        return recommendPersons;
    }

    public void setRecommendPersons(int recommendPersons) {
        this.recommendPersons = recommendPersons;
    }

    public int getReceiveCoupons() {
        return receiveCoupons;
    }

    public void setReceiveCoupons(int receiveCoupons) {
        this.receiveCoupons = receiveCoupons;
    }

    public List<CouponItemEntity> getCoupons() {
        return coupons;
    }

    public void setCoupons(List<CouponItemEntity> coupons) {
        this.coupons = coupons;
    }


    @Override
    public String getShareTitle() {
        return shareTitle != null ? shareTitle : "";
    }

    @Override
    public String getShareText() {
        return shareContent != null ? shareContent : "";
    }

    @Override
    public String getShareTitleUrl() {
        return shareLink != null ? shareLink : "";
    }

    @Override
    public String getShareSiteUrl() {
        return shareLink != null ? shareLink : "";
    }

    @Override
    public Integer getShareImageData() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public String getShareImageUrl() {
        return null;
    }

    @Override
    public String[] getShareImageArray() {
        return null;
    }
}
