package com.kingyon.elevator.uis.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.TabPagerEntity;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.adapters.UnLazyAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshFragment;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshLoadingFragment;
import com.leo.afbaselibrary.uis.fragments.BaseStateLoadingFragment;
import com.leo.afbaselibrary.uis.fragments.BaseTabFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.widgets.PagerSlidingTabStrip;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 */

public class PlanNewFragment extends BaseTabFragment<TabPagerEntity> {

    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;

    private boolean editMode;

    public static PlanNewFragment newInstance() {
        Bundle args = new Bundle();
        PlanNewFragment fragment = new PlanNewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_plan_new;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        updateMode();
    }

    @Override
    public Fragment getContent(int pos) {
        return PlanListFragment.newInstance(mItems.get(pos).getType());
    }

    @Override
    protected void getData() {
        mItems.add(new TabPagerEntity("商业", "商业", Constants.PLAN_TYPE.BUSINESS));
        mItems.add(new TabPagerEntity("DIY", "DIY", Constants.PLAN_TYPE.DIY));
        mItems.add(new TabPagerEntity("便民信息", "便民信息", Constants.PLAN_TYPE.INFORMATION));
        mPager.setOffscreenPageLimit(Integer.MAX_VALUE);
        initPager();
    }

    @Override
    protected PagerSlidingTabStrip.TabAddWay getItemAddWay() {
        return PagerSlidingTabStrip.TabAddWay.ITEM_MATCH;
    }

    @Override
    protected void initTabView() {
        mTabLayout.setTextSize(ScreenUtil.sp2px(18));
    }

    @NonNull
    @Override
    protected PagerAdapter getPagerAdapter() {
        return new UnLazyAdapter<>(getChildFragmentManager(), mItems, this);
    }

    public void onTypeModify(String planType) {
        if (TextUtils.isEmpty(planType)) {
            return;
        }
        int targetItem = getTypeItem(planType);
        if (mPager.getCurrentItem() != targetItem) {
            mPager.setCurrentItem(targetItem);
        }
        updateFragmentContent();
    }

    private int getTypeItem(String planType) {
        int result = -1;
        if (mItems != null) {
            for (int i = 0; i < mItems.size(); i++) {
                TabPagerEntity tabPagerEntity = mItems.get(i);
                if (TextUtils.equals(planType, tabPagerEntity.getType())) {
                    result = i;
                    break;
                }
            }
        }
        return result;
    }

    private void updateFragmentContent() {
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof BaseRefreshLoadingFragment) {
                ((BaseRefreshLoadingFragment) fragment).autoRefresh();
            } else if (fragment instanceof BaseRefreshFragment) {
                ((BaseRefreshFragment) fragment).autoRefresh();
            } else if (fragment instanceof BaseStateLoadingFragment) {
                ((BaseStateLoadingFragment) fragment).loadData();
            }
        }
    }

    //    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser) {
//            updateFragmentContent();
//        }
//    }

    @OnClick(R.id.tv_mode)
    public void onViewClicked() {
        editMode = !editMode;
        updateMode();
    }

    private void updateMode() {
        tvMode.setText(editMode ? "取消" : "编辑");
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof PlanListFragment) {
                ((PlanListFragment) fragment).setEditMode(editMode);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof PlanListFragment) {
                ((PlanListFragment) fragment).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }
}
