package com.kingyon.elevator.uis.fragments.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.customview.PlanSelectDateNewDialog;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.SelectDateEntity;
import com.kingyon.elevator.entities.TabPagerEntity;
import com.kingyon.elevator.interfaces.PlanSelectDateLinsener;
import com.kingyon.elevator.uis.widgets.RoundPagerSlidingTabStrip;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.SrcSuccess;
import com.kingyon.elevator.view.AlwaysMarqueeTextView;
import com.leo.afbaselibrary.uis.adapters.UnLazyAdapter;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshFragment;
import com.leo.afbaselibrary.uis.fragments.BaseRefreshLoadingFragment;
import com.leo.afbaselibrary.uis.fragments.BaseStateLoadingFragment;
import com.leo.afbaselibrary.uis.fragments.BaseTabFragment;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.widgets.PagerSlidingTabStrip;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kingyon.elevator.utils.utilstwo.HtmlUtil.delHTMLTag;

/**
 * Created by GongLi on 2019/2/19.
 * Email：lc824767150@163.com
 * 计划activity 更改时间2020-5-22日
 */

public class PlanNewFragment extends BaseTabFragment<TabPagerEntity> {

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
    @BindView(R.id.select_date_container)
    LinearLayout select_date_container;
    @BindView(R.id.tv_total_day)
    TextView tv_total_day;
    @BindView(R.id.tv_start_date)
    TextView tv_start_date;
    @BindView(R.id.tv_start_date_desc)
    TextView tv_start_date_desc;
    @BindView(R.id.tv_end_date)
    TextView tv_end_date;
    @BindView(R.id.tv_end_date_desc)
    TextView tv_end_date_desc;
    @BindView(R.id.img_top_back)
    ImageView img_top_back;
    SimpleDateFormat simpleDateFormat;
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.head_root)
    RelativeLayout headRoot;
    @BindView(R.id.tv_business)
    TextView tvBusiness;
    @BindView(R.id.tv_diy_ad)
    TextView tvDiyAd;
    @BindView(R.id.tv_bianmin)
    TextView tvBianmin;
    @BindView(R.id.rl_xz)
    RelativeLayout rlXz;
    @BindView(R.id.ll_kstime)
    LinearLayout llKstime;
    @BindView(R.id.pre_tab_layout)
    RoundPagerSlidingTabStrip preTabLayout;
    @BindView(R.id.pre_pager)
    ViewPager prePager;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.tv_notice)
    AlwaysMarqueeTextView tvNotice;
    private boolean editMode;
    PlanSelectDateNewDialog planSelectDateNewDialog;
    String type;

//    public static PlanNewFragment newInstance() {
//        Bundle args = new Bundle();
//        PlanNewFragment fragment = new PlanNewFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_plan_new;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
        StatusBarUtil.setHeadViewPadding(this, flTitle);
        StatusBarUtil.setTransparent(this, false);
        type = getIntent().getStringExtra("type");
        selectedIndex = 0;
        updateMode();
        initDateView();
        ConentUtils.httpData(Constants.AgreementType.PLAN_PROMPT.getValue(), new SrcSuccess() {
            @Override
            public void srcSuccess(String data) {
                tvNotice.setText(delHTMLTag(data) + "");
            }
        });
    }

    private void initDateView() {
        tv_start_date.setText(String.format("%d月%d日", DateUtils.getLastSelectDateDay().getMonth(), DateUtils.getLastSelectDateDay().getDay()));
        tv_end_date.setText(String.format("%d月%d日", DateUtils.getLastSelectDateDay().getMonth(), DateUtils.getLastSelectDateDay().getDay()));
        tv_total_day.setText(String.format("共%d天", getDiffDay(DateUtils.getLastSelectDateDay().getDate() + " 00:00:00.000",
                DateUtils.getLastSelectDateDay().getDate() + " 23:59:59.999")));
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof PlanListFragment) {
                ((PlanListFragment) fragment).updateTime(DateUtils.getLastSelectDateDay().getDate() + " 00:00:00.000",
                        DateUtils.getLastSelectDateDay().getDate() + " 23:59:59.999");
            }
        }
    }

    @Override
    public Fragment getContent(int pos) {
        return PlanListFragment.newInstance(mItems.get(pos).getType(),type);
    }

    @Override
    protected void getData() {
        mItems.add(new TabPagerEntity("商业", "商业", Constants.PLAN_TYPE.BUSINESS));
        mItems.add(new TabPagerEntity("DIY", "DIY", Constants.PLAN_TYPE.DIY));
        mItems.add(new TabPagerEntity("便民信息", "便民信息", Constants.PLAN_TYPE.INFORMATION));
        mPager.setOffscreenPageLimit(Integer.MAX_VALUE);
        initPager();
        selectedIndex = 0;
        mPager.setCurrentItem(AdUtils.pager(AdUtils.type), false);
    }


    @OnClick({R.id.tab_business, R.id.tab_diy_ad, R.id.tab_bianmin, R.id.tv_mode, R.id.select_date_container, R.id.img_top_back})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tab_business:
                tab_business.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
                tvBusiness.setVisibility(View.VISIBLE);
                tvBianmin.setVisibility(View.GONE);
                tvDiyAd.setVisibility(View.GONE);
                tab_diy_ad.setBackground(null);
                tab_bianmin.setBackground(null);
                tab_business.setTextColor(Color.parseColor("#000000"));
                tab_diy_ad.setTextColor(Color.parseColor("#666666"));
                tab_bianmin.setTextColor(Color.parseColor("#666666"));
                mPager.setCurrentItem(0, false);
                break;
            case R.id.tab_diy_ad:
                tab_diy_ad.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
                tvBusiness.setVisibility(View.GONE);
                tvBianmin.setVisibility(View.GONE);
                tvDiyAd.setVisibility(View.VISIBLE);
                tab_business.setBackground(null);
                tab_bianmin.setBackground(null);
                tab_diy_ad.setTextColor(Color.parseColor("#000000"));
                tab_business.setTextColor(Color.parseColor("#666666"));
                tab_bianmin.setTextColor(Color.parseColor("#666666"));
                mPager.setCurrentItem(1, false);
                break;
            case R.id.tab_bianmin:
                tab_bianmin.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
                tvBusiness.setVisibility(View.GONE);
                tvBianmin.setVisibility(View.VISIBLE);
                tvDiyAd.setVisibility(View.GONE);
                tab_diy_ad.setBackground(null);
                tab_business.setBackground(null);
                tab_bianmin.setTextColor(Color.parseColor("#000000"));
                tab_business.setTextColor(Color.parseColor("#666666"));
                tab_diy_ad.setTextColor(Color.parseColor("#666666"));
                mPager.setCurrentItem(2, false);
                break;
            case R.id.tv_mode:
                editMode = !editMode;
                updateMode();
                break;
            case R.id.select_date_container:
                if (QuickClickUtils.isFastClick()) {
                    showDateDialog();
                }

                break;
            case R.id.img_top_back:
                finish();
                break;
        }
    }

    private void showDateDialog() {
        DialogUtils.getInstance().showPlanSelectDateDialog(this, new PlanSelectDateLinsener() {
            @Override
            public void confirmSelectDate(SelectDateEntity startTime, SelectDateEntity endTime) {
                if (startTime != null && endTime != null) {
                    tv_start_date.setText(String.format("%d月%d日", startTime.getMonth(), startTime.getDay()));
                    tv_end_date.setText(String.format("%d月%d日", endTime.getMonth(), endTime.getDay()));
                    tv_total_day.setText(String.format("共%d天", getDiffDay(startTime.getDate(), endTime.getDate())));
                    for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                        if (fragment instanceof PlanListFragment) {
                            ((PlanListFragment) fragment).updateTime(startTime.getDate(), endTime.getDate());
                        }
                    }
                }
            }

            @Override
            public void dialogShowSuccess() {
                hideProgress();
            }
        });
    }

    private int getDiffDay(String startTime, String endTime) {
        try {
            simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date start = simpleDateFormat.parse(startTime);
            Date end = simpleDateFormat.parse(endTime);
            tv_start_date_desc.setText("(" + DateUtils.getWeekOfDate(start) + ")");
            tv_end_date_desc.setText("(" + DateUtils.getWeekOfDate(end) + ")");
            return DateUtils.getDatePoorDay(end, start);
        } catch (ParseException e) {
            return 0;
        }
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
        return new UnLazyAdapter<>(getSupportFragmentManager(), mItems, this);
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


    @Override
    public void onPageSelected(int position) {
        super.onPageSelected(position);
        setSelectedDataView(position);
    }

    private void setSelectedDataView(int position) {
        if (position == 0) {
            tab_business.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
            tvBusiness.setVisibility(View.VISIBLE);
            tvBianmin.setVisibility(View.GONE);
            tvDiyAd.setVisibility(View.GONE);
            tab_diy_ad.setBackground(null);
            tab_bianmin.setBackground(null);
            tab_business.setTextColor(Color.parseColor("#000000"));
            tab_diy_ad.setTextColor(Color.parseColor("#666666"));
            tab_bianmin.setTextColor(Color.parseColor("#666666"));
        } else if (position == 1) {
            tab_diy_ad.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
            tvBusiness.setVisibility(View.GONE);
            tvBianmin.setVisibility(View.GONE);
            tvDiyAd.setVisibility(View.VISIBLE);
            tab_business.setBackground(null);
            tab_bianmin.setBackground(null);
            tab_diy_ad.setTextColor(Color.parseColor("#000000"));
            tab_business.setTextColor(Color.parseColor("#666666"));
            tab_bianmin.setTextColor(Color.parseColor("#666666"));
        } else if (position == 2) {
            tab_bianmin.setBackgroundResource(R.drawable.shape_plan_tab_title_selected_bg);
            tvBusiness.setVisibility(View.GONE);
            tvBianmin.setVisibility(View.VISIBLE);
            tvDiyAd.setVisibility(View.GONE);
            tab_diy_ad.setBackground(null);
            tab_business.setBackground(null);
            tab_bianmin.setTextColor(Color.parseColor("#000000"));
            tab_business.setTextColor(Color.parseColor("#666666"));
            tab_diy_ad.setTextColor(Color.parseColor("#666666"));
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
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
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


    private void updateMode() {
        tvMode.setText(editMode ? "取消" : "编辑");
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof PlanListFragment) {
                ((PlanListFragment) fragment).setEditMode(editMode);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof PlanListFragment) {
                ((PlanListFragment) fragment).onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogUtils.getInstance().hidePlanSelectDateDialog();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
