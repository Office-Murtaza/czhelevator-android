package com.leo.afbaselibrary.uis.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

import butterknife.BindView;

/**
 * Created by gongli on 2017/5/23 10:39
 * email: lc824767150@163.com
 */

public abstract class BaseStateLoadingFragment extends BaseFragment {
    protected StateLayout stateLayout;

    protected final static int STATE_CONTENT = 0;
    protected final static int STATE_EMPTY = 1;
    protected final static int STATE_PROGRESS = 2;
    protected final static int STATE_ERROR = 3;

    @Override
    public void init(Bundle savedInstanceState) {
        stateLayout = getView(R.id.stateLayout);
        initStateLayout();
    }

    protected void initStateLayout() {
        stateLayout.setViewSwitchAnimProvider(new FadeViewAnimProvider());
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoLoading();
            }
        });
        stateLayout.showProgressView("数据加载中");
        stateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 200);
    }

    protected void autoLoading() {
        stateLayout.showProgressView("数据加载中");
        loadData();
    }


    public abstract void loadData();

    protected void loadingComplete(int state) {
        switch (state) {
            case STATE_CONTENT:
                stateLayout.showContentView();
                break;
            case STATE_EMPTY:
                stateLayout.showEmptyView(getEmptyTip());
                break;
            case STATE_PROGRESS:
                stateLayout.showProgressView("数据加载中");
                break;
            case STATE_ERROR:
                stateLayout.showErrorView("加载失败");
                break;
        }
    }

    @NonNull
    protected String getEmptyTip() {
        return "暂无数据";
    }
}
