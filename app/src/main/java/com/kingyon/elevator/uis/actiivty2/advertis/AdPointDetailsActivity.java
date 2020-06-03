package com.kingyon.elevator.uis.actiivty2.advertis;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentImageAdapter;
import com.kingyon.elevator.uis.fragments.main2.advertis.AdPointDetailsFragment;
import com.kingyon.elevator.uis.fragments.main2.advertis.CommercialFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    @BindView(R.id.img_plan)
    ImageView imgPlan;
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
    private String imga = "http://cdn.tlwgz.com/FgQ9lLaSS2ref866t6oZj7NINupl&_&http://cdn.tlwgz.com/Fmc2t4ho9jCERdgBgpq1JO8m6_sg&_&http://cdn.tlwgz.com/FpUnIRvbGBrPjBdQtpzaWYRX9ZDh&_&http://cdn.tlwgz.com/FsRd5vFYNQKcp2AobSaAMFPa5Brl&_&http://cdn.tlwgz.com/FsxyZ9aFsGOXpfCq4Vs_fjCE8z9O&_&http://cdn.tlwgz.com/Fvo6ThllZD3Hn6Mi3qhv66MDAmya";

    @Override
    public int getContentViewId() {
        return R.layout.activity_adpoin_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AdPointDetailsFragment().setIndex("1"), "商业广告");
        adapter.addFrag(new AdPointDetailsFragment().setIndex("2"), "DAY广告");
        adapter.addFrag(new AdPointDetailsFragment().setIndex("3"), "便民广告");
        vpPoindetails.setAdapter(adapter);
        vpPoindetails.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vpPoindetails);
        List<Object> list = StringUtils.StringToList(imga);
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
                switch (position){
                    case 0:
                       tvType.setText("商业广告");
                        break;
                    case 1:
                        tvType.setText("DAY广告");
                        break;
                    case 2:
                        tvType.setText("便民广告");
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

    @OnClick({R.id.img_top_back, R.id.img_plan, R.id.tv_conent,R.id.tv_program})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.img_plan:
                /*进入计划*/

                break;
            case R.id.tv_conent:

                break;
            case R.id.tv_program:
                /*加入计划*/

                break;
        }
    }

}
