package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.TimeUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 */

public class CouponDetailsActivity extends BaseSwipeBackActivity {
    @BindView(R.id.tv_discounts_1)
    TextView tvDiscounts1;
    @BindView(R.id.tv_discounts_2)
    TextView tvDiscounts2;
    @BindView(R.id.tv_condition)
    TextView tvCondition;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_range)
    TextView tvRange;
    @BindView(R.id.tv_expier_time)
    TextView tvExpierTime;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.ll_bg)
    LinearLayout llBg;
    @BindView(R.id.tv_name_details)
    TextView tvNameDetails;
    @BindView(R.id.tv_discoun_details)
    TextView tvDiscounDetails;
    @BindView(R.id.tv_number_details)
    TextView tvNumberDetails;
    @BindView(R.id.ll_number_details)
    LinearLayout llNumberDetails;
    @BindView(R.id.tv_condition_details)
    TextView tvConditionDetails;
    @BindView(R.id.tv_range_details)
    TextView tvRangeDetails;
    @BindView(R.id.tv_expier_details)
    TextView tvExpierDetails;
    private CouponItemEntity coupon;

    @Override
    protected String getTitleText() {
        coupon = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        return "优惠券详情";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_coupon_details;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        boolean discount = TextUtils.equals(Constants.CouponType.DISCOUNT, coupon.getCoupontype());
        tvDiscounts1.setText(discount ? CommonUtil.getMayTwoFloat(coupon.getDiscount()) : "￥");
        tvDiscounts2.setText(discount ? "折" : CommonUtil.getMayTwoFloat(coupon.getMoney()));
        tvDiscounDetails.setText(String.format("%s%s", tvDiscounts1.getText().toString(), tvDiscounts2.getText().toString()));

        tvCondition.setText(String.format("满%s可用", CommonUtil.getMayTwoFloat(coupon.getCouponCondition())));
        tvConditionDetails.setText(tvCondition.getText().toString());

        tvName.setText(discount ? "折扣券" : "代金券");
        tvNameDetails.setText(tvName.getText().toString());

        tvNumber.setText(String.format("×%s", coupon.getCouponsCount()));
        tvNumberDetails.setText(String.format("%s张", coupon.getCouponsCount()));

        String adType = coupon.getAdType() != null ? coupon.getAdType() : "";
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
        tvRange.setText(String.format("适用：%s", result));
        tvRangeDetails.setText(result);
        tvExpierTime.setText(String.format("过期时间：%s", TimeUtil.getYMdTime(coupon.getExpiredDate())));
        tvExpierDetails.setText(TimeUtil.getYMdTime(coupon.getExpiredDate()));

        int visiableValue = RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PARTNER, AppContent.getInstance().getMyUserRole()) ? View.VISIBLE : View.GONE;
        llNumberDetails.setVisibility(visiableValue);
        tvNumber.setVisibility(visiableValue);
    }

    @OnClick(R.id.tv_to_use)
    public void onViewClicked() {
        ActivityUtil.finishAllNotMain();
        EventBus.getDefault().post(new TabEntity(1));
    }
}
