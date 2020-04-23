package com.kingyon.elevator.uis.fragments.homepage;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.entities.NewsItemEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.NewsRecommendationPresenter;
import com.kingyon.elevator.uis.activities.NewsDetailsActivity;
import com.kingyon.elevator.uis.adapters.NewsRecommendAdapter;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.NewsRecommendationView;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更多新闻列表界面
 */
public class NewsRecommendationFragment extends MvpBaseFragment<NewsRecommendationPresenter> implements NewsRecommendationView {

    @BindView(R.id.news_recommend_list)
    ListView news_recommend_list;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.et_input_keyword)
    EditText et_input_keyword;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;


    NewsRecommendAdapter newsRecommendAdapter;
    private Integer currentType = null;
    private Boolean isReflashing = false;

    @Override
    public NewsRecommendationPresenter initPresenter() {
        return new NewsRecommendationPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        newsRecommendAdapter = new NewsRecommendAdapter(getActivity(), presenter.getNewsEntityList());
        news_recommend_list.setAdapter(newsRecommendAdapter);
        news_recommend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                        MyActivityUtils.goNewsDetailsActivity(getActivity(), presenter.getNewsEntityList().get(position).getId());

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        smart_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                smart_refresh_layout.setEnableLoadMore(true);
                isReflashing = true;
                presenter.loadNewsList(ReflashConstants.Refalshing, currentType, et_input_keyword.getText().toString().trim());
            }
        });
        smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                isReflashing = true;
                presenter.loadNewsList(ReflashConstants.LoadMore, currentType, et_input_keyword.getText().toString().trim());
            }
        });
        smart_refresh_layout.autoRefresh();
        et_input_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“搜索”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    smart_refresh_layout.autoRefresh();
                }
                return false;
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_news_recommendation;
    }


    @OnClick({R.id.tab_tuiguang, R.id.tab_hangye, R.id.tab_zhishi, R.id.tab_news,R.id.tab_all})
    public void OnClick(View view) {
        if (isReflashing) {
            return;
        }
        switch (view.getId()) {
            case R.id.tab_tuiguang:
                currentType = 1;
                smart_refresh_layout.autoRefresh();
                break;
            case R.id.tab_hangye:
                currentType = 2;
                smart_refresh_layout.autoRefresh();
                break;
            case R.id.tab_zhishi:
                currentType = 3;
                smart_refresh_layout.autoRefresh();
                break;
            case R.id.tab_news:
                currentType = 4;
                smart_refresh_layout.autoRefresh();
                break;
            case R.id.tab_all:
                currentType = null;
                smart_refresh_layout.autoRefresh();
                break;

        }
    }


    public static NewsRecommendationFragment newInstance() {
        Bundle args = new Bundle();
        NewsRecommendationFragment fragment = new NewsRecommendationFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void showListData(List<NewsEntity> incomeDetailsEntities) {
        if (newsRecommendAdapter != null) {
            newsRecommendAdapter.reflashData(incomeDetailsEntities);
        }
        if (incomeDetailsEntities.size()>0) {
            stateLayout.showContentView();
        }else {
            stateLayout.setEmptyViewTip("  ");
            stateLayout.showEmptyView("暂无数据");
        }
    }

    @Override
    public void loadMoreIsComplete() {
        smart_refresh_layout.setEnableLoadMore(false);
        isReflashing = false;
        showShortToast("数据已全部加载完毕");
    }

    @Override
    public void hideProgressDailog() {
        isReflashing = false;
        smart_refresh_layout.finishLoadMore();
        smart_refresh_layout.finishRefresh();
    }
}
