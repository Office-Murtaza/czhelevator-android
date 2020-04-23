package com.kingyon.elevator.uis.fragments.main2.found;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.andview.refreshview.utils.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.TopicAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions: 话题类别
 */
public class TopicTypeFragment extends BaseFragment {
    @BindView(R.id.rc_view)
    RecyclerView rcView;
    Unbinder unbinder;
    TopicAdapter topicAdapter;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder1;

    private void initUi() {
        rcView = getContentView().findViewById(R.id.rc_view);
        smartRefreshLayout = getContentView().findViewById(R.id.smart_refresh_layout);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder1.unbind();
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_topic_type;
    }

    @Override
    protected void lazyLoad() {
        initUi();
    }

}
