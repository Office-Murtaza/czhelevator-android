package com.leo.afbaselibrary.uis.activities;

import android.os.Bundle;
import android.view.View;

import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

/**
 * Created by gongli on 2017/5/23 13:45
 * email: lc824767150@163.com
 */

public abstract class BaseStateLoadingActivity extends BaseSwipeBackActivity {
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
                autoLoad();
            }
        });
        stateLayout.showProgressView(getString(R.string.loading));
        stateLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        }, 200);
    }

    public void autoLoad() {
        stateLayout.showProgressView(getString(R.string.loading));
        loadData();
    }


    protected abstract void loadData();

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
    }

    protected String getEmptyTip() {
        return getString(R.string.empty);
    }
}
