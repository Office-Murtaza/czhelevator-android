package com.leo.afbaselibrary.uis.fragments;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.chanven.lib.cptr.recyclerview.RecyclerAdapterWithHF;
import com.leo.afbaselibrary.R;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.wrapper.EmptyWrapper;
import com.leo.afbaselibrary.widgets.WrapContentLinearLayoutManager;
import com.orhanobut.logger.Logger;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;


import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.adapters.AnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

/**
 * created by arvin on 16/11/21 20:54
 * email：1035407623@qq.com
 */
public abstract class BaseRefreshLoadingFragment<T> extends BaseFragment implements SwipeRefreshHelper.OnSwipeRefreshListener,
        OnLoadMoreListener, MultiItemTypeAdapter.OnItemClickListener<T>, MultiItemTypeAdapter.OnItemLongClickListener<T> {

    protected final int FIRST_PAGE = 1;
    protected SwipeRefreshHelper mSwipeRefreshHelper;
    protected SwipeRefreshLayout mLayoutRefresh;
    protected RecyclerView mRecyclerView;

    protected List<T> mItems = new ArrayList<>();
    protected MultiItemTypeAdapter<T> mInnerAdapter;
    protected EmptyWrapper mEmptyWrapper;
    protected RecyclerAdapterWithHF mAdapter;

    protected RecyclerView.LayoutManager mLayoutManager;
    protected int mCurrPage = FIRST_PAGE;

    @Override
    public void init(Bundle savedInstanceState) {
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
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
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
        return new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    protected RecyclerAdapterWithHF initAdapter() {
        mInnerAdapter = getAdapter();
        mInnerAdapter.setOnItemClickListener(this);
        mInnerAdapter.setOnItemLongClickListener(this);
        mEmptyWrapper = new EmptyWrapper<>(mInnerAdapter);
        mEmptyWrapper.setEmptyView(getEmptyViewId());
        mAdapter = new RecyclerAdapterWithHF(dealAnimationAdapter(mEmptyWrapper));
        return mAdapter;
    }

    protected RecyclerView.Adapter dealAnimationAdapter(RecyclerView.Adapter recyclerAdapterWithHF) {
        AnimationAdapter adapter = new ScaleInAnimationAdapter(recyclerAdapterWithHF);
        adapter.setFirstOnly(true);
        adapter.setDuration(500);
        adapter.setInterpolator(new OvershootInterpolator(.5f));
        return adapter;
    }

    protected int getEmptyViewId() {
        return R.layout.ui_layout_empty;
    }

    protected void setupRefreshAndLoadMore() {
        mSwipeRefreshHelper = new SwipeRefreshHelper(mLayoutRefresh);
        mSwipeRefreshHelper.setOnSwipeRefreshListener(this);
        mSwipeRefreshHelper.setOnLoadMoreListener(this);
        autoRefresh();
    }

    public void autoRefresh() {
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
        mSwipeRefreshHelper.setLoadMoreEnable(loadSuccess && mCurrPage < totalPage);
        if (mSwipeRefreshHelper.isLoadMoreEnable()) {
            mSwipeRefreshHelper.loadMoreComplete(true);
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, T item, int position) {
        Logger.d(position);
    }

    protected abstract MultiItemTypeAdapter<T> getAdapter();

    protected abstract void loadData(int page);

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T item, int position) {
        return false;
    }
}
