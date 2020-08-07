package com.kingyon.elevator.uis.actiivty2.user.order;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.order.OrderFragmentt;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_ORDER;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
@Route(path = ACTIVITY_ORDER)
public class OrderActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.vp)
    ViewPager vp;
    TextView tab_text;
    TextView tv_tab_number;
    RelativeLayout rl_num;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        tvTopTitle.setText("我的订单");
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OrderFragmentt().setIndex("",""), "全部");
        adapter.addFrag(new OrderFragmentt().setIndex("WAITRELEASE",""),"待发布");
        adapter.addFrag(new OrderFragmentt().setIndex("","REVIEWFAILD"),"审核未过");
        adapter.addFrag(new OrderFragmentt().setIndex("RELEASEING",""),"发布中");
        adapter.addFrag(new OrderFragmentt().setIndex("COMPLETE",""),"已完成");

        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vp);

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
