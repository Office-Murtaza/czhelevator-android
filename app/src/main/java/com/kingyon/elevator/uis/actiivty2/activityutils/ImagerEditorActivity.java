package com.kingyon.elevator.uis.actiivty2.activityutils;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.CustomFragmentPagerAdapter;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.IMAGER_EDITOR_ACTIVITY;

/**
 * @Created By Admin  on 2020/4/30
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:图片编辑
 */
@Route(path = IMAGER_EDITOR_ACTIVITY)
public class ImagerEditorActivity extends BaseActivity {

    @Autowired
    ArrayList<String> listPath;
    @Autowired
    int position;
    @BindView(R.id.viewpagertab)
    SmartTabLayout viewpagertab;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_ll)
    TextView tvLl;
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_top_root)
    RelativeLayout rlTopRoot;
    CustomFragmentPagerAdapter adapter;

    private int position1 = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_image_editor;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        StatusBarUtil.setHeadViewPadding(this, tvLl);
        StatusBarUtil.setTransparent(this, false);
        position1 = position;
        tvTopTitle.setText((position + 1) + "/" + listPath.size());
        initAdapter();
    }


    private void initAdapter() {
        adapter = new CustomFragmentPagerAdapter(getSupportFragmentManager());
        LogUtils.e(listPath.toString());
        for (int i = 0; i < listPath.size(); i++) {
            adapter.addFrag(new ImagerEditorFragment().setIndex(listPath.get(i)), "全部");
        }
        adapter.notifyDataSetChanged();
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(adapter.getCount());
        viewpagertab.setViewPager(vp);
        vp.setCurrentItem(position1);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvTopTitle.setText((position + 1) + "/" + listPath.size());
                position1 = position;
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

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}

