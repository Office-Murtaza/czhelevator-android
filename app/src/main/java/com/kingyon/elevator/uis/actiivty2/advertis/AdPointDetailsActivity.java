package com.kingyon.elevator.uis.actiivty2.advertis;

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
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellDetailsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentImageAdapter;
import com.kingyon.elevator.uis.fragments.main.PlanNewFragment;
import com.kingyon.elevator.uis.fragments.main2.advertis.AdPointDetailsFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
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
    String adtype = ADV_BUSINESS;
    @BindView(R.id.tv_bumber)
    TextView tvBumber;
    @BindView(R.id.rl_plan)
    RelativeLayout rlPlan;

    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            if (AdUtils.planNumber > 0) {
                tvBumber.setText(AdUtils.planNumber + "");
                tvBumber.setVisibility(View.VISIBLE);
            }
            handler.postDelayed(this, 500);
        }
    };

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
    }

    private void httpDetails() {
        showProgressDialog("加载中。。。");
        LogUtils.e(panID);
        NetService.getInstance().cellDetails(Long.parseLong(panID))
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<CellDetailsEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(CellDetailsEntity cellDetailsEntity) {
                        LogUtils.e(cellDetailsEntity.toString());
                        cellEntity = cellDetailsEntity;
                        hideProgress();
                        tvAddress.setText(cellDetailsEntity.getAddress());
                        tvCommunityName.setText(cellDetailsEntity.getCellName());
                        tvDistance.setText(cellDetailsEntity.getDistance() + "");
                    }
                });

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AdPointDetailsFragment().setIndex(cellEntity, "1"), "商业广告");
        adapter.addFrag(new AdPointDetailsFragment().setIndex(cellEntity, "2"), "DAY广告");
        adapter.addFrag(new AdPointDetailsFragment().setIndex(cellEntity, "3"), "便民广告");
        vpPoindetails.setAdapter(adapter);
        vpPoindetails.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vpPoindetails);
        List<Object> list = new ArrayList<>();
//        list.add(cellEntity.getCellBanner());
        ContentImageAdapter contentImageAdapter = new ContentImageAdapter(this, list);
        rvImage.setLayoutManager(new LinearLayoutManager(this));
        rvImage.setAdapter(contentImageAdapter);
        rvImage.setNestedScrollingEnabled(false);
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
                        tvCurrentPrice.setText(cellEntity.getBusinessAdPrice() + "元/台/天");
                        tvOriginalPrice.setText(cellEntity.getBusinessAdPrice() + "元/台/天");
                        break;
                    case 1:
                        adtype = ADV_DAY;
                        tvType.setText("DAY广告");
                        tvCurrentPrice.setText(cellEntity.getDiyAdPrice() + "元/台/天");
                        tvOriginalPrice.setText(cellEntity.getDiyAdPrice() + "元/台/天");
                        break;
                    case 2:
                        adtype = ADV_INFORMATION;
                        tvType.setText("便民广告");
                        tvCurrentPrice.setText(cellEntity.getInformationAdPrice() + "元/台/天");
                        tvOriginalPrice.setText(cellEntity.getInformationAdPrice() + "元/台/天");
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
                startActivity(PlanNewFragment.class);
                break;
            case R.id.tv_conent:

                break;
            case R.id.tv_program:
                /*加入计划*/
                addPlan(adtype, panID);
                break;
        }
    }

    private void addPlan(String type, String panID) {
        NetService.getInstance().plansAddCells(type, panID)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(AdPointDetailsActivity.this, ex.getDisplayMessage(), 1000);
                    }

                    @Override
                    public void onNext(String s) {
                        ToastUtils.showToast(AdPointDetailsActivity.this, "添加成功", 1000);
                        AdUtils.httpPlannuber();
                    }
                });
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
    }
}
