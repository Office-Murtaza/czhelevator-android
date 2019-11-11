package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.text.TextUtils;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.ArrayList;

/**
 * Created by GongLi on 2019/1/3.
 * Email：lc824767150@163.com
 */

public class CouponsAdapter extends MultiItemTypeAdapter<Object> {
    public CouponsAdapter(Context context, ArrayList<Object> mItems) {
        super(context, mItems);
        addItemViewDelegate(1, new CouponItemDelegate());
        addItemViewDelegate(2, new CouponTypeDelegate());
    }

    private class CouponItemDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_order_coupons_item;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof CouponItemEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            CouponItemEntity item = (CouponItemEntity) o;

            holder.setSelected(R.id.ll_bg, item.isChoosed());

            boolean discount = TextUtils.equals(Constants.CouponType.DISCOUNT, item.getCoupontype());
            holder.setTextNotHide(R.id.tv_discounts_1, discount ? CommonUtil.getMayTwoFloat(item.getDiscount()) : "￥");
            holder.setTextNotHide(R.id.tv_discounts_2, discount ? "折" : CommonUtil.getMayTwoFloat(item.getMoney()));
            holder.setTextSize(R.id.tv_discounts_1, discount ? 32 : 14);
            holder.setTextSize(R.id.tv_discounts_2, discount ? 14 : 32);
            holder.setTextNotHide(R.id.tv_condition, String.format("满%s可用", CommonUtil.getMayTwoFloat(item.getCouponCondition())));
            holder.setTextNotHide(R.id.tv_name, discount ? "折扣券" : "代金券");

            String adType = item.getAdType() != null ? item.getAdType() : "";
            boolean business = adType.contains(Constants.PLAN_TYPE.BUSINESS);
            boolean diy = adType.contains(Constants.PLAN_TYPE.DIY);
            boolean info = adType.contains(Constants.PLAN_TYPE.INFORMATION);
            String result;
            if (business && diy && info) {
                result = "全部";
            } else if (business && diy && !info) {
                result = "商业广告,DIY";
            } else if (business && !diy && info) {
                result = "商业广告,便民服务";
            } else if (!business && diy && info) {
                result = "DIY,便民服务";
            } else if (business && !diy && !info) {
                result = "仅商业广告可用";
            } else if (!business && diy && !info) {
                result = "仅DIY可用";
            } else if (!business && !diy && info) {
                result = "仅便民服务可用";
            } else {
                result = "全部";
            }
            holder.setTextNotHide(R.id.tv_range, String.format("适用：%s", result));
            holder.setTextNotHide(R.id.tv_expier_time, String.format("过期时间：%s", TimeUtil.getYMdTime(item.getExpiredDate())));
        }
    }

    private class CouponTypeDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_order_coupons_type;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            String couponType = o.toString();
            boolean voucher = TextUtils.equals(Constants.CouponType.VOUCHER, couponType);
            holder.setTextNotHide(R.id.tv_name, voucher ? "代金券" : "折扣券");
            holder.setTextDrawableLeft(R.id.tv_name, voucher ? R.drawable.ic_coupon_voucher : R.drawable.ic_coupon_discount);
        }
    }
}
