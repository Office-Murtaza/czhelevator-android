package com.kingyon.elevator.entities;

import com.leo.afbaselibrary.nets.entities.PageListEntity;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class RecommendListInfo {
    private RecommendInfoEntity recommendInfo;
    private PageListEntity<UserEntity> userPage;

    public RecommendListInfo(RecommendInfoEntity recommendInfo, PageListEntity<UserEntity> userPage) {
        this.recommendInfo = recommendInfo;
        this.userPage = userPage;
    }

    public RecommendInfoEntity getRecommendInfo() {
        return recommendInfo;
    }

    public void setRecommendInfo(RecommendInfoEntity recommendInfo) {
        this.recommendInfo = recommendInfo;
    }

    public PageListEntity<UserEntity> getUserPage() {
        return userPage;
    }

    public void setUserPage(PageListEntity<UserEntity> userPage) {
        this.userPage = userPage;
    }
}
