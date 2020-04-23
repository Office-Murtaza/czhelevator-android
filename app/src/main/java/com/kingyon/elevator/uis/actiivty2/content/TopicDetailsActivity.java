package com.kingyon.elevator.uis.actiivty2.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.TopicDetailsPresenter;
import com.kingyon.elevator.uis.fragments.main2.found.topic.TopicDetailsFragment;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.CustomFragmentPagerAdapter;
import com.kingyon.elevator.view.ModifyTabLayout;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_TOPIC_DETAILS;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 话题详情
 */
@Route(path = ACTIVITY_MAIN2_TOPIC_DETAILS)
public class TopicDetailsActivity extends MvpBaseActivity<TopicDetailsPresenter> {
    @BindView(R.id.img_topimg)
    ImageView imgTopimg;
    @BindView(R.id.tv_topic_title)
    TextView tvTopicTitle;
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.img_jb)
    ImageView imgJb;
    @BindView(R.id.tv_topic_contetn)
    TextView tvTopicContetn;
    @BindView(R.id.modiftTabLayout)
    ModifyTabLayout modiftTabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rl_bg)
    RelativeLayout rlBg;

    @Override
    public TopicDetailsPresenter initPresenter() {
        return new TopicDetailsPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_details);
//        StatusBarUtil.setTransparent(this);
//        StatusBarUtil.setHeadViewPadding(this, rlBg);
        ButterKnife.bind(this);
        initData();

    }

    private void initData() {
        CustomFragmentPagerAdapter adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new TopicDetailsFragment(), "最新");
        adapter.addFrag(new TopicDetailsFragment(), "最热");
        vp.setAdapter(adapter);

        vp.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vp);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishRefresh();
            }
        });


    }

    @OnClick({R.id.img_bake, R.id.img_jb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_jb:
                break;
        }
    }

    public int dp2px(float dpValue) {
        float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

}
