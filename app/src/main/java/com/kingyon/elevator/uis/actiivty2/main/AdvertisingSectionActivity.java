package com.kingyon.elevator.uis.actiivty2.main;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.AdvertisingSectionPresenter;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AdvertisingSectionActivity extends MvpBaseActivity<AdvertisingSectionPresenter> {
    @Override
    public AdvertisingSectionPresenter initPresenter() {
        return new AdvertisingSectionPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertising_section);
        initData();
    }

    private void initData() {


    }
}
