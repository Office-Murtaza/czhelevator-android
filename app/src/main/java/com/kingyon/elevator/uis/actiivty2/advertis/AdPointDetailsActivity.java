package com.kingyon.elevator.uis.actiivty2.advertis;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.CellDetailsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentImageAdapter;
import com.kingyon.elevator.uis.dialogs.DialogUtils;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.uis.fragments.main2.advertis.AdPointDetailsFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.utils.TimeUtil;
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.kingyon.elevator.view.AnimManager;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.ADV_BUSINESS;
import static com.czh.myversiontwo.utils.CodeType.ADV_DAY;
import static com.czh.myversiontwo.utils.CodeType.ADV_INFORMATION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ADPOINT_DETAILS;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isCertification;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * @Created By Admin  on 2020/5/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:点位详情
 */
@Route(path = ACTIVITY_ADPOINT_DETAILS)
public class AdPointDetailsActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;

    @BindView(R.id.tv_community_name)
    TextView tvCommunityName;
    @BindView(R.id.tv_distance)
    TextView tvDistance;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.vp_poindetails)
    ViewPager vpPoindetails;
    @BindView(R.id.tv_building_number)
    TextView tvBuildingNumber;
    @BindView(R.id.tv_elevators_number)
    TextView tvElevatorsNumber;
    @BindView(R.id.tv_lowest_floor)
    TextView tvLowestFloor;
    @BindView(R.id.tv_cars_number)
    TextView tvCarsNumber;
    @BindView(R.id.tv_delivery_time)
    TextView tvDeliveryTime;
    @BindView(R.id.tv_units)
    TextView tvUnits;
    @BindView(R.id.tv_devices_number)
    TextView tvDevicesNumber;
    @BindView(R.id.tv_highest_floor)
    TextView tvHighestFloor;
    @BindView(R.id.tv_rent)
    TextView tvRent;
    @BindView(R.id.tv_property_costs)
    TextView tvPropertyCosts;
    @BindView(R.id.tv_conent)
    TextView tvConent;
    @BindView(R.id.rv_image)
    RecyclerView rvImage;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.tv_current_price)
    TextView tvCurrentPrice;
    @BindView(R.id.tv_original_price)
    TextView tvOriginalPrice;
    @BindView(R.id.tv_program)
    TextView tvProgram;
    CellDetailsEntity cellEntity;
    @Autowired
    String panID;
    @Autowired
    String distanceM;
    String adtype = ADV_BUSINESS;
    @BindView(R.id.tv_bumber)
    TextView tvBumber;
    @BindView(R.id.rl_plan)
    RelativeLayout rlPlan;
    String imageUrl;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (AdUtils.planNumber > 0) {
                tvBumber.setText(AdUtils.planNumber + "");
                tvBumber.setVisibility(View.VISIBLE);
            }
            handler.postDelayed(this, 500);
        }
    };
    @BindView(R.id.img_plan)
    ImageView imgPlan;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_adpoin_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        ARouter.getInstance().inject(this);
        httpDetails();
        AdUtils.httpPlannuber();
        handler.postDelayed(runnable, 500);
        tvDistance.setText(distanceM + "");
        LogUtils.e(distanceM);
    }

    private void httpDetails() {
        LogUtils.e(panID);
        stateLayout.showProgressView(getString(R.string.wait));
        NetService.getInstance().cellDetails(Long.parseLong(panID), DataSharedPreferences.getCreatateAccount())
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<CellDetailsEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        ToastUtils.showToast(AdPointDetailsActivity.this, ex.getDisplayMessage(), 1000);
                        hideProgress();
                        finish();
                        stateLayout.showErrorView();
                    }

                    @Override
                    public void onNext(CellDetailsEntity cellDetailsEntity) {
                        stateLayout.showContentView();
                        cellEntity = cellDetailsEntity;
                        tvAddress.setText(cellDetailsEntity.regionName);
                        tvCommunityName.setText(cellDetailsEntity.name);
                        tvConent.setText(cellDetailsEntity.communityIntroduction + "");
                        tvType.setText("商业广告");
                        adtype = ADV_BUSINESS;
                        tvCurrentPrice.setText(cellEntity.priceBusiness + "元/台/天");
                        tvOriginalPrice.setText(cellEntity.originalPriceBusiness + "元/台/天");
                        tvOriginalPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        tvBuildingNumber.setText("楼栋数:" + cellDetailsEntity.numberBuilding);
                        tvElevatorsNumber.setText("电梯数：" + cellDetailsEntity.elevatorsNumber);
                        tvLowestFloor.setText("建筑面积：" + cellDetailsEntity.numberArea);
                        tvCarsNumber.setText("车位数：" + cellDetailsEntity.siteNumber);
                        if (cellDetailsEntity.deliveryTime != null) {
                            tvDeliveryTime.setText("交房时间：" + TimeUtil.getYmdDliverCh(cellDetailsEntity.deliveryTime));
                        } else {
                            tvDeliveryTime.setText("交房时间：");
                        }
                        tvUnits.setText("单元数：" + cellDetailsEntity.numberUnit);
                        tvDevicesNumber.setText("设备数：" + cellDetailsEntity.numberFacility);
                        tvHighestFloor.setText("社区楼层楼层：" + cellDetailsEntity.maxNumberFloor + "-" + cellDetailsEntity.maxNumberFloor);
                        tvRent.setText("租金：" + cellDetailsEntity.rent + "元/平方/月");
                        tvPropertyCosts.setText("物业费：" + cellDetailsEntity.propertyFee + "元/平方/月");
                        imageUrl = cellDetailsEntity.urlCover;
                        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
                        adapter.addFrag(new AdPointDetailsFragment(cellEntity, "1", panID), "商业广告");
                        adapter.addFrag(new AdPointDetailsFragment(cellEntity, "2", panID), "DIY广告");
                        adapter.addFrag(new AdPointDetailsFragment(cellEntity, "3", panID), "便民广告");
                        vpPoindetails.setAdapter(adapter);
                        vpPoindetails.setOffscreenPageLimit(adapter.getCount());
                        viewpagertab.setViewPager(vpPoindetails);

                        if (cellEntity.cellBanner != null) {
                            LogUtils.e(cellDetailsEntity.cellBanner);
                            List<Object> list = new ArrayList<>();
                            for (int c = 0; c < cellDetailsEntity.cellBanner.size(); c++) {
                                list.add(cellEntity.cellBanner.get(c));
                            }
                            ContentImageAdapter contentImageAdapter = new ContentImageAdapter(AdPointDetailsActivity.this, list);
                            rvImage.setLayoutManager(new LinearLayoutManager(AdPointDetailsActivity.this));
                            rvImage.setAdapter(contentImageAdapter);
                            rvImage.setNestedScrollingEnabled(false);
                        }
                        hideProgress();
                    }
                });


        vpPoindetails.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        tvType.setText("商业广告");
                        adtype = ADV_BUSINESS;
                        tvCurrentPrice.setText(cellEntity.priceBusiness + "元/台/天");
                        tvOriginalPrice.setText(cellEntity.originalPriceBusiness + "元/台/天");
                        break;
                    case 1:
                        adtype = ADV_DAY;
                        tvType.setText("DIY广告");
                        tvCurrentPrice.setText(cellEntity.priceDiy + "元/台/天");
                        tvOriginalPrice.setText(cellEntity.originalPriceDiy + "元/台/天");
                        break;
                    case 2:
                        adtype = ADV_INFORMATION;
                        tvType.setText("便民广告");
                        tvCurrentPrice.setText(cellEntity.priceText + "元/台/天");
                        tvOriginalPrice.setText(cellEntity.originalPriceText + "元/台/天");
                        break;
                    default:
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_top_back, R.id.rl_plan, R.id.tv_conent, R.id.tv_program})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.rl_plan:
                /*进入计划*/
                if (isToken(this)) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "ad");
                    startActivity(PlanNewFragment.class, bundle);
                } else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.tv_conent:

                break;
            case R.id.tv_program:
                /*加入计划*/
                tvProgram.setClickable(false);
                if (isToken(this)) {
                    if (isCertification()) {
                        DialogUtils.shwoCertificationDialog(this);
                        tvProgram.setClickable(true);
                    } else {
                        addPlan(adtype, panID);
                    }
                } else {
                    tvProgram.setClickable(true);
                    ActivityUtils.setLoginActivity();
                }
                break;
        }
    }

    private void addPlan(String type, String panID) {
        showProgressDialog(getString(R.string.wait),false);
        NetService.getInstance().plansAddCells(type, panID)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        ToastUtils.showToast(AdPointDetailsActivity.this, ex.getDisplayMessage(), 1000);
                        tvProgram.setClickable(true);
                    }

                    @Override
                    public void onNext(String s) {
                        tvProgram.setClickable(true);
                        hideProgress();
                        ToastUtils.showToast(AdPointDetailsActivity.this, "添加成功", 1000);
                        AdUtils.httpPlannuber();
                        LogUtils.e(imageUrl);
                        AdUtils.type = type;
                        AnimManager animManager = new AnimManager.Builder()
                                .with(AdPointDetailsActivity.this)
                                .startView(tvProgram)
                                .endView(imgPlan)
                                .imageUrl(imageUrl)
                                .time(1000)
                                .animHeight(40)
                                .animWidth(40)
                                .build();
                        animManager.startAnim();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
