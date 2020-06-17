package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.widget.TextView;

import com.gerry.scaledelete.ScreenUtil;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.TabPagerEntity;
import com.kingyon.elevator.uis.fragments.user.MateriaListFragment;
import com.leo.afbaselibrary.uis.activities.BaseTabActivity;
import com.leo.afbaselibrary.uis.adapters.UnLazyAdapter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/11.
 * Email：lc824767150@163.com
 */

public class MyMateriaActivity extends BaseTabActivity<TabPagerEntity> {

    @BindView(R.id.pre_v_right)
    TextView preVRight;
    private boolean editMode;

    @Override
    protected String getTitleText() {
        return "我的素材";
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        preVRight.setText("编辑");
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_materia;
    }

    @Override
    public Fragment getContent(int pos) {
        return MateriaListFragment.newInstance(mItems.get(pos).getType());
    }

    @NonNull
    @Override
    protected PagerAdapter getPagerAdapter() {
        return new UnLazyAdapter<>(getSupportFragmentManager(), mItems, this);
    }

    @Override
    protected void getData() {
        mItems.add(new TabPagerEntity("图片", "图片", Constants.Materia_Type.IMAGE));
        mItems.add(new TabPagerEntity("视频", "视频", Constants.Materia_Type.VIDEO));
        mPager.setOffscreenPageLimit(Integer.MAX_VALUE);
        initPager();
    }

    @Override
    protected void initTabView() {
        mTabLayout.setTextSize(ScreenUtil.sp2px(16));
    }

    @OnClick(R.id.pre_v_right)
    public void onViewClicked() {
        editMode = !editMode;
        updateMode();
    }

    private void updateMode() {
        preVRight.setText(editMode ? "取消" : "编辑");
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            if (fragment instanceof MateriaListFragment) {
                ((MateriaListFragment) fragment).setEditMode(editMode);
            }
        }
    }


}
