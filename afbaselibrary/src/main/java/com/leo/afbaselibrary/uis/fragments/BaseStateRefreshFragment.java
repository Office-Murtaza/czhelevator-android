package com.leo.afbaselibrary.uis.fragments;

import android.os.Bundle;
import android.view.View;

import com.leo.afbaselibrary.widgets.StateLayout;
import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

/**
 * Created by GongLi on 2017/11/1.
 * Email：lc824767150@163.com
 */

public abstract class BaseStateRefreshFragment extends BaseRefreshFragment {
    protected StateLayout stateLayout;

    protected final static int STATE_CONTENT = 0;
    protected final static int STATE_EMPTY = 1;
    protected final static int STATE_PROGRESS = 2;
    protected final static int STATE_ERROR = 3;

    @Override
    public void init(Bundle savedInstanceState) {
        super.init(savedInstanceState);
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
        stateLayout.showProgressView(getString(R.string.loading));
        stateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                onRefresh();
            }
        }, 200);
    }

    protected void autoLoading() {
        stateLayout.showProgressView(getString(R.string.loading));
        onRefresh();
    }

    protected void loadingComplete(int state) {
        switch (state) {
            case STATE_CONTENT:
                stateLayout.showContentView();
                break;
            case STATE_EMPTY:
                stateLayout.showEmptyView(getEmptyTip());
                break;
            case STATE_PROGRESS:
                stateLayout.showProgressView(getString(R.string.loading));
                break;
            case STATE_ERROR:
                stateLayout.showErrorView(getString(R.string.error));
                break;
        }
        refreshComplete();
    }

    protected void showState(int state) {
        switch (state) {
            case STATE_CONTENT:
                stateLayout.showContentView();
                break;
            case STATE_EMPTY:
                stateLayout.showEmptyView(getEmptyTip());
                break;
            case STATE_PROGRESS:
                stateLayout.showProgressView(getString(R.string.loading));
                break;
            case STATE_ERROR:
                stateLayout.showErrorView(getString(R.string.error));
                break;
        }
    }

    protected String getEmptyTip() {
        return getString(R.string.empty);
    }
}
