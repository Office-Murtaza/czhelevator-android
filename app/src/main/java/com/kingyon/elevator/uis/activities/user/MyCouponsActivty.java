package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.gerry.scaledelete.ScreenUtil;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CouponItemEntity;
import com.kingyon.elevator.entities.TabPagerEntity;
import com.kingyon.elevator.uis.fragments.user.MyCouponsFragment;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.RoleUtils;
import com.leo.afbaselibrary.uis.activities.BaseTabActivity;
import com.leo.afbaselibrary.uis.adapters.UnLazyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MyCouponsActivty extends BaseTabActivity<TabPagerEntity> {
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    @Override
    protected String getTitleText() {
        return "优惠券";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_coupons;
    }

    @Override
    protected void getData() {
        mItems.add(new TabPagerEntity("未使用", "未使用", Constants.CouponStatus.NORMAL));
        mItems.add(new TabPagerEntity("已使用", "已使用", Constants.CouponStatus.USED));
        mItems.add(new TabPagerEntity("已过期", "已过期", Constants.CouponStatus.EXPIRED));
        mPager.setOffscreenPageLimit(3);
        initPager();
    }

    @Override
    public Fragment getContent(int pos) {
        return MyCouponsFragment.newInstance(mItems.get(pos).getType());
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("赠送优惠券");
    }

    @NonNull
    @Override
    protected PagerAdapter getPagerAdapter() {
        return new UnLazyAdapter<>(getSupportFragmentManager(), mItems, this);
    }

    @Override
    protected void initTabView() {
        mTabLayout.setTextSize(ScreenUtil.sp2px(16));

    }

    public void resultNumber(String type, long number) {
        for (TabPagerEntity entity : mItems) {
            if (TextUtils.equals(type, entity.getType())) {
                entity.setTitle(String.format("%s(%s)", entity.getRealTitle(), number));
                break;
            }
        }
        mTabLayout.notifyDataSetChanged();
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
//        ArrayList<CouponItemEntity> coupons = getChoosedCoupons();
//        if (coupons == null || coupons.size() < 1) {
//            showToast("至少需要选择一张未使用的优惠券");
//            return;
//        }
        startActivityForResult(DonateChooseActivity.class, CommonUtil.REQ_CODE_1);
    }

    @Override
    protected void onResume() {
        preVRight.setVisibility(RoleUtils.getInstance().roleBeTarget(Constants.RoleType.PARTNER, AppContent.getInstance().getMyUserRole()) ? View.VISIBLE : View.GONE);
        super.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            freshFragment();
        }
    }

    private void freshFragment() {
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment instanceof MyCouponsFragment) {
                    ((MyCouponsFragment) fragment).autoRefresh();
                }
            }
        }
    }


//
//    private ArrayList<CouponItemEntity> getChoosedCoupons() {
//        ArrayList<CouponItemEntity> coupons = null;
//        List<Fragment> fragments = getSupportFragmentManager().getFragments();
//        if (fragments != null) {
//            for (Fragment fragment : fragments) {
//                if (fragment instanceof MyCouponsFragment) {
//                    ArrayList<CouponItemEntity> choosedCoupons = ((MyCouponsFragment) fragment).getChoosedCoupons();
//                    if (choosedCoupons.size() > 0) {
//                        coupons = choosedCoupons;
//                        break;
//                    }
//                }
//            }
//        }
//        return coupons;
//    }
}
