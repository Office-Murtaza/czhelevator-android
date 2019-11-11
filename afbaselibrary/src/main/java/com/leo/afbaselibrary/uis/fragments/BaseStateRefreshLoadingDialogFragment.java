package com.leo.afbaselibrary.uis.fragments;

import android.os.Bundle;
import android.view.View;

import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.leo.afbaselibrary.widgets.emptyprovider.FadeViewAnimProvider;

/**
 * Created by GongLi on 2018/7/23.
 * Email：lc824767150@163.com
 */

public abstract class BaseStateRefreshLoadingDialogFragment<T> extends BaseRefreshLoadingDialogFragment<T> {
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
    }

    public void autoLoading() {
        stateLayout.showProgressView(getString(R.string.loading));
        onfresh();
    }

    @Override
    protected void refreshComplete(boolean loadSuccess) {
        initState(loadSuccess);
        super.refreshComplete(loadSuccess);
    }

    protected void loadingComplete(boolean loadSuccess, int totalPage) {
        initState(loadSuccess);
        super.refreshComplete(loadSuccess, totalPage);
    }

    protected void initState(boolean loadSuccess) {
        if (loadSuccess) {
            if (mCurrPage == FIRST_PAGE && mItems.size() == 0) {
                stateLayout.showEmptyView(getEmptyTip());
            } else {
                stateLayout.showContentView();
            }
        } else {
            if (mCurrPage == FIRST_PAGE) {
                stateLayout.showErrorView(getString(R.string.error));
            }
        }
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
