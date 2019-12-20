package com.kingyon.elevator.uis.fragments.main;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.TabEntity;
import com.kingyon.elevator.entities.TabPagerEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.PlanNewFragment2Presenter;
import com.kingyon.elevator.uis.activities.PhotoPickerActivity;
import com.kingyon.elevator.uis.fragments.PhotoPickerFragment;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshFragment;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshLoadingFragment;
import com.leo.afbaselibrary.uis.fragments.BaseStateLoadingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 计划单新界面
 */
public class PlanNewFragment2 extends MvpBaseFragment<PlanNewFragment2Presenter> {
    @BindView(R.id.tv_mode)
    TextView tvMode;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.tab_business)
    TextView tab_business;
    @BindView(R.id.tab_diy_ad)
    TextView tab_diy_ad;
    @BindView(R.id.tab_bianmin)
    TextView tab_bianmin;
    @BindView(R.id.pre_pager)
    ViewPager mPager;
    private List<Fragment> fragmentList;
    protected List<TabPagerEntity> mItems = new ArrayList<>();
    private boolean editMode;

    public static PlanNewFragment2 newInstance() {

        Bundle args = new Bundle();

        PlanNewFragment2 fragment = new PlanNewFragment2();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public PlanNewFragment2Presenter initPresenter() {
        return new PlanNewFragment2Presenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        mItems.add(new TabPagerEntity("商业", "商业", Constants.PLAN_TYPE.BUSINESS));
        mItems.add(new TabPagerEntity("DIY", "DIY", Constants.PLAN_TYPE.DIY));
        mItems.add(new TabPagerEntity("便民信息", "便民信息", Constants.PLAN_TYPE.INFORMATION));
        initFragment();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_plan_new_fragment2;
    }

    @OnClick({R.id.tab_business, R.id.tab_diy_ad, R.id.tab_bianmin, R.id.tv_mode})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tab_business:
                tab_business.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
                tab_diy_ad.setBackground(null);
                tab_bianmin.setBackground(null);
                mPager.setCurrentItem(0, false);
                break;
            case R.id.tab_diy_ad:
                tab_diy_ad.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
                tab_business.setBackground(null);
                tab_bianmin.setBackground(null);
                mPager.setCurrentItem(1, false);
                break;
            case R.id.tab_bianmin:
                tab_bianmin.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
                tab_diy_ad.setBackground(null);
                tab_business.setBackground(null);
                mPager.setCurrentItem(2, false);
                break;
            case R.id.tv_mode:
                editMode = !editMode;
                updateMode();
                break;
        }
    }

    private void initFragment() {
        fragmentList.add(PlanListFragment.newInstance(mItems.get(0).getType()));
        fragmentList.add(PlanListFragment.newInstance(mItems.get(1).getType()));
        fragmentList.add(PlanListFragment.newInstance(mItems.get(2).getType()));
        mPager.setAdapter(new MyFragmentPagerAdapter(getChildFragmentManager(), fragmentList));
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectedDataView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    private void updateMode() {
        tvMode.setText(editMode ? "取消" : "编辑");
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof PlanListFragment) {
                ((PlanListFragment) fragment).setEditMode(editMode);
            }
        }
    }

    public void onTypeModify(String planType) {
        if (TextUtils.isEmpty(planType)) {
            return;
        }
        int targetItem = getTypeItem(planType);
        if (mPager.getCurrentItem() != targetItem) {
            mPager.setCurrentItem(targetItem, false);
            setSelectedDataView(targetItem);
        }
        updateFragmentContent();
    }

    private void setSelectedDataView(int position) {
        if (position == 0) {
            tab_business.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
            tab_diy_ad.setBackground(null);
            tab_bianmin.setBackground(null);
        } else if (position == 1) {
            tab_diy_ad.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
            tab_business.setBackground(null);
            tab_bianmin.setBackground(null);
        } else if (position == 2) {
            tab_bianmin.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
            tab_diy_ad.setBackground(null);
            tab_business.setBackground(null);
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getChildFragmentManager().getFragments()) {
            if (fragment instanceof PlanListFragment) {
                ((PlanListFragment) fragment).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mfragmentList;

        public MyFragmentPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.mfragmentList = fragmentList;
        }

        //获取集合中的某个项
        @Override
        public Fragment getItem(int position) {
            return mfragmentList.get(position);
        }

        //返回绘制项的数目
        @Override
        public int getCount() {
            return mfragmentList.size();
        }
    }
}
