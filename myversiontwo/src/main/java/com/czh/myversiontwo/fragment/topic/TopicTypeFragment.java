package com.czh.myversiontwo.fragment.topic;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.R;
import com.czh.myversiontwo.uiutils.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions: 话题类别
 */
public class TopicTypeFragment extends BaseFragment {
    RecyclerView rcView;
    SmartRefreshLayout smartRefreshLayout;
//    TopicAdapter topicAdapter;
    @Override
    protected int setContentView() {
        return R.layout.fragment_topic_type;
    }

    @Override
    protected void lazyLoad() {
        initView();
    }

    private void initView() {
        rcView = getContentView().findViewById(R.id.rc_view);
        smartRefreshLayout = getContentView().findViewById(R.id.smart_refresh_layout);

//        topicAdapter = new TopicAdapter(getActivity(), 20);
//        rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rcView.setAdapter(topicAdapter);
//        LogUtils.e("栏目开始");
//        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                smartRefreshLayout.finishLoadMore();
//            }
//        });
//        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                topicAdapter = new TopicAdapter(getActivity(), 20);
//                rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                rcView.setAdapter(topicAdapter);
//                smartRefreshLayout.finishRefresh();
//            }
//        });
    }
}
