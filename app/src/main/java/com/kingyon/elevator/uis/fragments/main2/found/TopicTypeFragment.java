package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adapter2.TopicAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.LazyloadFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions: 话题类别
 */
public class TopicTypeFragment extends LazyloadFragment {
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    Unbinder unbinder;
    TopicAdapter topicAdapter;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    public TopicTypeFragment setIndex(String s) {

        return null;
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_topic_type;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        init();
    }

    @Override
    protected void init() {
        rcView = rootView.findViewById(R.id.rc_view);
        smartRefreshLayout = rootView.findViewById(R.id.smart_refresh_layout);
        topicAdapter = new TopicAdapter(getActivity(), 20);
        rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcView.setAdapter(topicAdapter);
        LogUtils.e("栏目开始");
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                smartRefreshLayout.finishLoadMore();
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                topicAdapter = new TopicAdapter(getActivity(), 20);
                rcView.setLayoutManager(new LinearLayoutManager(getActivity()));
                rcView.setAdapter(topicAdapter);
                smartRefreshLayout.finishRefresh();
            }
        });
    }

    @Override
    protected void lazyLoad() {
        init();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
