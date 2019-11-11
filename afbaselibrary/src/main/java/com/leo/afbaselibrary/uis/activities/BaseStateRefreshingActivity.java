package com.leo.afbaselibrary.uis.activities;

import android.os.Bundle;
import android.view.View;

import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

/**
 * Created by gongli on 2017/5/24 12:01
 * email: lc824767150@163.com
 */

public abstract class BaseStateRefreshingActivity extends BaseRefreshActivity {
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

    private void initStateLayout() {
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
                stateLayout.showEmptyView(getString(R.string.empty));
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
                stateLayout.showEmptyView(getString(R.string.empty));
                break;
            case STATE_PROGRESS:
                stateLayout.showProgressView(getString(R.string.loading));
                break;
            case STATE_ERROR:
                stateLayout.showErrorView(getString(R.string.error));
                break;
        }
    }
}
