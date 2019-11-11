package com.kingyon.library.social;

import android.content.Context;
import android.graphics.BitmapFactory;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by GongLi on 2017/11/17.
 * Email：lc824767150@163.com
 */

public class BaseSharePramsProvider<T extends ShareEntity> implements ShareDialog.ShareParamsProvider {
    protected Context context;
    protected T shareEntity;

    public BaseSharePramsProvider(Context context, T shareEntity) {
        this.context = context;
        this.shareEntity = shareEntity;
    }

    public BaseSharePramsProvider(Context context) {
        this.context = context;
    }

    public T getShareEntity() {
        return shareEntity;
    }

    public void setShareEntity(T shareEntity) {
        this.shareEntity = shareEntity;
    }

    @Override
    public Platform.ShareParams getParamsQQ() {
        QQ.ShareParams shareParams = new QQ.ShareParams();
        if (shareEntity != null) {
            shareParams.setTitle(shareEntity.getShareTitle());
            shareParams.setTitleUrl(shareEntity.getShareTitleUrl());
            shareParams.setText(shareEntity.getShareText());
            if (shareEntity.getShareImageData() != null) {
                shareParams.setImageData(BitmapFactory.decodeResource(context.getResources(), shareEntity.getShareImageData()));
            }
            shareParams.setImageUrl(shareEntity.getShareImageUrl());
            shareParams.setSiteUrl(shareEntity.getShareSiteUrl());
        }
        return shareParams;
    }

//    @Override
//    public Platform.ShareParams getParamsQQZone() {
//        ShareParams shareParams = new ShareParams();
//        if (shareEntity != null) {
//            shareParams.setTitle(shareEntity.getShareTitle());
//            shareParams.setTitleUrl(shareEntity.getShareTitleUrl());
//            shareParams.setText(shareEntity.getShareTitle());
//            if (shareEntity.getShareImageData() != null) {
//                shareParams.setImageData(BitmapFactory.decodeResource(context.getResources(), shareEntity.getShareImageData()));
//            }
//            shareParams.setImageUrl(shareEntity.getShareImageUrl());
//            shareParams.setSiteUrl(shareEntity.getShareSiteUrl());
//        }
//        return shareParams;
//    }

    @Override
    public Platform.ShareParams getParamsWeChat() {
        Wechat.ShareParams shareParams = new Wechat.ShareParams();
        if (shareEntity != null) {
            shareParams.setShareType(Platform.SHARE_WEBPAGE);
            shareParams.setTitle(shareEntity.getShareTitle());
            shareParams.setText(shareEntity.getShareText());
            if (shareEntity.getShareImageData() != null) {
                shareParams.setImageData(BitmapFactory.decodeResource(context.getResources(), shareEntity.getShareImageData()));
            }
            shareParams.setImageUrl(shareEntity.getShareImageUrl());
            shareParams.setUrl(shareEntity.getShareSiteUrl());
        }
        return shareParams;
    }

    @Override
    public Platform.ShareParams getParamsWeChatMoments() {
        WechatMoments.ShareParams shareParams = new WechatMoments.ShareParams();
        if (shareEntity != null) {
            shareParams.setShareType(Platform.SHARE_WEBPAGE);
            shareParams.setTitle(shareEntity.getShareTitle());
            shareParams.setText(shareEntity.getShareText());
            if (shareEntity.getShareImageData() != null) {
                shareParams.setImageData(BitmapFactory.decodeResource(context.getResources(), shareEntity.getShareImageData()));
            }
            shareParams.setImageUrl(shareEntity.getShareImageUrl());
            shareParams.setUrl(shareEntity.getShareSiteUrl());
        }
        return shareParams;
    }

    @Override
    public Platform.ShareParams getParamsWeChatImages() {
        WechatMoments.ShareParams shareParams = new WechatMoments.ShareParams();
        if (shareEntity != null) {
            shareParams.setShareType(Platform.SHARE_IMAGE);
            shareParams.setText(shareEntity.getShareText());
            if (shareEntity.getShareImageArray() != null) {
                shareParams.setImageArray(shareEntity.getShareImageArray());
            }
        }
        return shareParams;
    }

    @Override
    public Platform.ShareParams getParamsSina() {
        SinaWeibo.ShareParams shareParams = new SinaWeibo.ShareParams();
        if (shareEntity != null) {
            shareParams.setText(shareEntity.getShareTitle() + "\n" + shareEntity.getShareSiteUrl());
        }
        return shareParams;
    }

    @Override
    public String getParamsMore() {
        return shareEntity != null ? shareEntity.getShareSiteUrl() : "";
    }
}
