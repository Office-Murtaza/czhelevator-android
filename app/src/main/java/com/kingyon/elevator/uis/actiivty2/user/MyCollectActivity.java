package com.kingyon.elevator.uis.actiivty2.user;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.uis.fragments.main2.user.MyCollectConentFragment;
import com.kingyon.elevator.uis.fragments.main2.user.MyCollectPointFragment;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MY_COLLECT;

/**
 * @Created By Admin  on 2020/6/5
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:我的收藏
 */
@Route(path = ACTIVITY_MY_COLLECT)
public class MyCollectActivity extends BaseActivity {
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

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_collecttwo;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new MyCollectConentFragment(), "内容");
        adapter.addFrag(new MyCollectPointFragment(), "点位");
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
