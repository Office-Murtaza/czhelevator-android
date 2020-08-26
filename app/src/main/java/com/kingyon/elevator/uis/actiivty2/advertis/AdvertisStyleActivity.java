package com.kingyon.elevator.uis.actiivty2.advertis;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.advertis.CommercialFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ADVERTIS_STYLE;

/**
 * @Created By Admin  on 2020/5/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:广告样式
 */
@Route(path = ACTIVITY_ADVERTIS_STYLE)
public class AdvertisStyleActivity extends BaseActivity {

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.vp_style)
    ViewPager vpStyle;

    @Override
    public int getContentViewId() {
        return R.layout.activity_advertis_style;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new CommercialFragment().setIndex("create_time desc","1"), "商业广告");
        adapter.addFrag(new CommercialFragment().setIndex("likes desc","2"), "DIY广告");
        adapter.addFrag(new CommercialFragment().setIndex("likes desc","3"), "便民广告");
        vpStyle.setAdapter(adapter);
        vpStyle.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vpStyle);
        tvTopTitle.setText("查看样式");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}
