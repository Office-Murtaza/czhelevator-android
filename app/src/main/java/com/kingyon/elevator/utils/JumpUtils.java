package com.kingyon.elevator.utils;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.ADEntity;
import com.kingyon.elevator.entities.BannerEntity;
import com.kingyon.elevator.entities.NormalMessageEntity;
import com.kingyon.elevator.entities.NormalOptionEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.uis.activities.advertising.AdPreviewActivity;
import com.kingyon.elevator.uis.activities.HtmlActivity;
import com.kingyon.elevator.uis.activities.MainActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationActivity;
import com.kingyon.elevator.uis.activities.homepage.CellDetailsActivity;
import com.kingyon.elevator.uis.activities.homepage.RecommendListActivity;
import com.kingyon.elevator.uis.activities.homepage.WikiDetailsActivity;
import com.kingyon.elevator.uis.activities.homepage.WikiListActivity;
import com.kingyon.elevator.uis.activities.order.OrderDetailsActivity;
import com.kingyon.elevator.uis.activities.property.PropertyActivity;
import com.kingyon.elevator.uis.activities.user.FeedBackDetailsActivity;
import com.kingyon.elevator.uis.activities.user.IdentityCompanyActivity;
import com.kingyon.elevator.uis.activities.user.IdentityPersonActivity;
import com.kingyon.elevator.uis.activities.user.IdentitySuccessActivity;
import com.kingyon.elevator.uis.activities.user.InviteActivity;
import com.kingyon.elevator.uis.activities.user.InviteListActivity;
import com.kingyon.elevator.uis.activities.user.MessageDetailsActivity;
import com.kingyon.elevator.uis.activities.user.MyAdActivity;
import com.kingyon.elevator.uis.activities.user.MyCouponsActivty;
import com.kingyon.elevator.uis.activities.user.MyInvoiceActivity;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.AFUtil;
import com.leo.afbaselibrary.utils.ActivityUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by GongLi on 2018/9/13.
 * Email：lc824767150@163.com
 */

public class JumpUtils {
    private static JumpUtils jumpUtils;

    public static JumpUtils getInstance() {
        if (jumpUtils == null) {
            jumpUtils = new JumpUtils();
        }
        return jumpUtils;
    }

    public void jumpToRoleMain(BaseActivity baseActivity, UserEntity userEntity) {
        if (userEntity == null) {
            return;
        }
        jumpToRoleMain(baseActivity, userEntity.getRole());
    }

    public void jumpToRoleMain(BaseActivity baseActivity, String role) {
//        if (TextUtils.isEmpty(role)) {
//            baseActivity.startActivity(MainActivity.class);
//        }
        baseActivity.startActivity(MainActivity.class);
    }

    public void onBannerClick(BaseActivity activity, BannerEntity item) {
        if (item == null || TextUtils.isEmpty(item.getType())) {
            return;
        }
        switch (item.getType()) {
            case Constants.BANNER_TYPE.LINK:
                if (item.getLink() != null && item.getLink().startsWith("http://jiali.gzzhkjw.com")) {
                    boolean success = AFUtil.openHtmlBySystem(activity, item.getLink());
                    if (!success) {
                        HtmlActivity.start(activity, "", item.getLink());
                    }
                } else {
                    HtmlActivity.start(activity, "", item.getLink());
                }
                break;
            case Constants.BANNER_TYPE.IMAGE:
                break;
            case Constants.BANNER_TYPE.EXAMPLE:
                ADEntity example = item.getExample();
                if (example == null) {
                    activity.startActivity(RecommendListActivity.class);
                } else {
                    jumpToAdPreview(activity, example);
                }
                break;
            case Constants.BANNER_TYPE.BAIKE:
                NormalOptionEntity baike = item.getBaike();
                if (baike == null) {
                    activity.startActivity(WikiListActivity.class);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putLong(CommonUtil.KEY_VALUE_1, baike.getObjectId());
                    bundle.putString(CommonUtil.KEY_VALUE_2, baike.getTitle());
                    activity.startActivity(WikiDetailsActivity.class, bundle);
                }
                break;
            case Constants.BANNER_TYPE.INVITE:
                activity.startActivity(InviteActivity.class);
                break;
            case Constants.BANNER_TYPE.CELL:
                Bundle bundle = new Bundle();
                bundle.putLong(CommonUtil.KEY_VALUE_1, item.getJumpId());
                activity.startActivity(CellDetailsActivity.class, bundle);
                break;
        }
    }

    public void jumpToAdPreview(BaseActivity baseActivity, ADEntity entity) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        baseActivity.startActivity(AdPreviewActivity.class, bundle);
    }

    public void jumpToMessagePage(BaseActivity baseActivity, NormalMessageEntity entity) {
        if (entity == null) {
            return;
        }
        String messageType = entity.getType() != null ? entity.getType() : "";
        Bundle bundle = new Bundle();
        switch (messageType) {
            case "ORDER_LIST"://订单
                EventBus.getDefault().post(new TabEntity(2, new NormalParamEntity("", "全部订单")));
                ActivityUtil.finishAllNotMain();
                break;
            case "ORDER"://订单
                bundle.putLong(CommonUtil.KEY_VALUE_1, entity.getExtraId());
                baseActivity.startActivity(OrderDetailsActivity.class, bundle);
                break;
            case "ACQUIRE_COUPONS"://获得优惠券
                baseActivity.startActivity(MyCouponsActivty.class);
                break;
            case "AD_SUCCED"://广告审核成功
                baseActivity.startActivity(MyAdActivity.class);
                break;
            case "AD_FAILED"://广告审核失败
                baseActivity.startActivity(MyAdActivity.class);
                break;
            case "PROMOTE_SUCCED"://推广已完成
                baseActivity.startActivity(InviteActivity.class);
                break;
            case "PROMOTE_AWARD"://推广获得奖励
                baseActivity.startActivity(InviteListActivity.class);
                break;
            case "FEEDBACK"://反馈收到平台回复
                bundle.putLong(CommonUtil.KEY_VALUE_1, entity.getExtraId());
                baseActivity.startActivity(FeedBackDetailsActivity.class, bundle);
                break;
            case "PERSON_SUCCED"://个人认证成功
                baseActivity.startActivity(IdentitySuccessActivity.class);
                break;
            case "PERSON_FAILED"://个人认证失败
                baseActivity.startActivity(IdentityPersonActivity.class);
                break;
            case "COMPANY_SUCCED"://企业认证成功
                baseActivity.startActivity(IdentitySuccessActivity.class);
                break;
            case "COMPANY_FAILED"://企业认证失败
                baseActivity.startActivity(IdentityCompanyActivity.class);
                break;
            case "PARTNER_SUCCED"://合伙人认证成功
                baseActivity.startActivity(CooperationActivity.class);
                break;
            case "PARTNER_FAILED"://合伙人认证失败
                baseActivity.startActivity(CooperationActivity.class);
                break;
            case "PROPERTY_SUCCED"://物业认证成功
                baseActivity.startActivity(PropertyActivity.class);
                break;
            case "PROPERTY_FAILED"://物业认证失败
                baseActivity.startActivity(PropertyActivity.class);
                break;
            case "INVOICE"://发票
                baseActivity.startActivity(MyInvoiceActivity.class);
                break;
            default:
                bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
                baseActivity.startActivity(MessageDetailsActivity.class, bundle);
                break;
        }
        //        PROMOTE_SUCCED 推广成功
//        PROMOTE_AWARD 推广成功
//        ACQUIRE_COUPONS 收到优惠券
//        PERSON_SUCCED 个人认证成功
//        PERSON_FAILED 个人认证失败
//        COMPANY_SUCCED 企业认证成功
//        COMPANY_FAILED 企业认证失败
//        AD_SUCCED 广告认证成功
//        AD_FAILED 广告认证失败
//        PARTNER_SUCCED 合伙人认证成功
//        PARTNER_FAILED 合伙人认证失败
//        PROPERTY_SUCCED 物业认证成功
//        PROPERTY_FAILED 物业认证失败
//        ORDER 订单
//        FEEDBACK 意见反馈
    }
}
