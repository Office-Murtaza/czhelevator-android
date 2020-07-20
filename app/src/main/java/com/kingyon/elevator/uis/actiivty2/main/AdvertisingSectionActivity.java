package com.kingyon.elevator.uis.actiivty2.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.AdvertisingSectionPresenter;
import com.kingyon.elevator.uis.adapters.adaptertwo.AdvertisingSectionAdapter;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ADVERTISING;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:广告专区
 */
@Route(path = ACTIVITY_MAIN2_ADVERTISING)
public class AdvertisingSectionActivity extends BaseActivity {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    @BindView(R.id.rv_list)
    RecyclerView rvList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_advertising_section;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        initData();
    }

    private void initData() {
        GridLayoutManager layoutManage = new GridLayoutManager(this, 6);
        rvList.setLayoutManager(layoutManage);
        layoutManage.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int i) {
                return setSpanSize(i);
            }
        });
        AdvertisingSectionAdapter advertisingSectionAdapter = new AdvertisingSectionAdapter(this);
        rvList.setAdapter(advertisingSectionAdapter);

    }
    private int setSpanSize(int position) {
        int count;
        if (position%3==0) {
            count = 6;
        } else {
            count = 3;
        }

        return count;
    }
    @OnClick(R.id.tv_bake)
    public void onViewClicked() {
        finish();
    }
}
