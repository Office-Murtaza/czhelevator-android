package com.leo.afbaselibrary.uis.activities;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.orhanobut.logger.Logger;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/8/21.
 * Email：lc824767150@163.com
 */

public abstract class BaseSmartRefreshingLoadingActivity<T> extends BaseSwipeBackActivity implements SwipeRefreshHelper.OnSwipeRefreshListener,
        OnLoadMoreListener, MultiItemTypeAdapter.OnItemClickListener<T> {

    protected final int FIRST_PAGE = 0;
    protected SwipeRefreshHelper mSwipeRefreshHelper;
    protected SwipeRefreshLayout mLayoutRefresh;
    protected RecyclerView mRecyclerView;

    protected ArrayList<T> mItems = new ArrayList<>();
    protected RecyclerView.Adapter mInnerAdapter;
    protected RecyclerAdapterWithHF mAdapter;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected int mCurrPage = FIRST_PAGE;

    @Override
    protected void initViews(Bundle savedInstanceState) {
        mLayoutRefresh = getView(R.id.pre_refresh);
        mRecyclerView = getView(R.id.pre_recycler_view);

        mLayoutRefresh.setColorSchemeResources(R.color.colorAccent);

        setupRecyclerView();

        setupRefreshAndLoadMore();
    }

    protected void setupRecyclerView() {
        mRecyclerView.setLayoutManager(initLayoutManager());
        mRecyclerView.setAdapter(initAdapter());
        if (isShowDivider()) setDivider();
    }

    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .colorResId(R.color.black_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
    }

    protected boolean isShowDivider() {
        return true;
    }

    protected RecyclerView.LayoutManager initLayoutManager() {
        mLayoutManager = getLayoutManager();
        return mLayoutManager;
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerAdapterWithHF initAdapter() {
        mInnerAdapter = getAdapter();
        ((MultiItemTypeAdapter) mInnerAdapter).setOnItemClickListener(this);
//        mEmptyWrapper = new EmptyWrapper<>(mInnerAdapter);
//        mEmptyWrapper.setEmptyView(getEmptyViewId());
        mAdapter = new RecyclerAdapterWithHF(mInnerAdapter);
        return mAdapter;
    }

    private void setupRefreshAndLoadMore() {
        mSwipeRefreshHelper = new SwipeRefreshHelper(mLayoutRefresh);
        mSwipeRefreshHelper.setOnSwipeRefreshListener(this);
        mSwipeRefreshHelper.setOnLoadMoreListener(this);

        if (isAutoRefresh()) {
            autoRefresh();
        }
    }

    protected void autoRefresh() {
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshHelper.autoRefresh();
            }
        }, 100);
    }

    @Override
    public void onfresh() {
        mCurrPage = FIRST_PAGE;
        loadData(mCurrPage);
    }

    @Override
    public void loadMore() {
        loadData(++mCurrPage);
    }

    protected void refreshComplete(boolean loadSuccess) {
        if (!loadSuccess && mCurrPage > FIRST_PAGE) {
            mCurrPage--;
        }
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshHelper.refreshComplete();
        mSwipeRefreshHelper.setLoadMoreEnable(loadSuccess && mItems.size() >= 15);
        if (mCurrPage > FIRST_PAGE) {
            mSwipeRefreshHelper.loadMoreComplete(true);
        }
    }

    protected void refreshComplete(boolean loadSuccess, int totalPage) {
        if (!loadSuccess && mCurrPage > FIRST_PAGE) {
            mCurrPage--;
        }
        mAdapter.notifyDataSetChanged();
        mSwipeRefreshHelper.refreshComplete();
        mSwipeRefreshHelper.setLoadMoreEnable(loadSuccess && mCurrPage < totalPage - 1);
        if (mCurrPage > FIRST_PAGE && mCurrPage < totalPage - 1) {
            mSwipeRefreshHelper.loadMoreComplete(true);
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, T item, int position) {
        Logger.d(position);
    }

    protected abstract MultiItemTypeAdapter<T> getAdapter();

    protected abstract void loadData(int page);

    public boolean isAutoRefresh() {
        return true;
    }

}