package com.leo.afbaselibrary.uis.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.leo.afbaselibrary.R;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public abstract class BaseRefreshDialogFragment extends BaseDialogFragment implements SwipeRefreshLayout.OnRefreshListener {

    protected SwipeRefreshLayout mLayoutRefresh;

    @Override
    public void init(Bundle savedInstanceState) {
        mLayoutRefresh = getView(R.id.pre_refresh);
        mLayoutRefresh.setColorSchemeResources(R.color.colorAccent);
        mLayoutRefresh.setOnRefreshListener(this);
    }

    public void autoRefresh() {
        mLayoutRefresh.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLayoutRefresh.setRefreshing(true);
                onRefresh();
            }
        }, 100);
    }

    protected void refreshComplete() {
        mLayoutRefresh.setRefreshing(false);
    }
}
