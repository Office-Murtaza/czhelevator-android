package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.HomeTopicConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.TopicAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
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
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 *
 * @Author:Mrczh Instructions: 话题类别
 */
public class TopicTypeFragment extends FoundFragemtUtils {
    TopicAdapter topicAdapter;
    @BindView(R.id.rc_view_topic)
    RecyclerView rcViewTopic;
    @BindView(R.id.smart_refresh_layout_topic)
    SmartRefreshLayout smartRefreshLayoutTopic;
    List<HomeTopicConentEntity> list = new ArrayList<>();
    int label = 0;
    int page = 1;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    View rootView;
    StateLayout stateLayout;


    public TopicTypeFragment setIndex(int label, StateLayout stateLayout) {
        this.label = label;
        this.stateLayout = stateLayout;
        return (this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @Override
    public int getContentViewId() {
        return R.layout.fragment_topic_type;
    }

    @Override
    public void init(Bundle savedInstanceState) {
    }

    @Override
    protected void lazyLoad() {
       if (smartRefreshLayoutTopic!=null) {
           smartRefreshLayoutTopic.autoRefresh(100);
       }else {
           list.clear();
           httpTopicType(page, String.valueOf(label),"",0);
       }
    }



    public void closeRefresh() {
        smartRefreshLayoutTopic.finishRefresh();
        smartRefreshLayoutTopic.finishLoadMore();
    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        smartRefreshLayoutTopic.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                closeRefresh();
                page++;
                httpTopicType(page, String.valueOf(label), "",0);
            }
        });
        smartRefreshLayoutTopic.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                closeRefresh();
                list.clear();
                page=1;
                httpTopicType(page, String.valueOf(label), "",0);
            }
        });
    }

    private void httpTopicType(int page, String label, String title,int id) {
        LogUtils.e(label,page,title);
        NetService.getInstance().setQueryTopicConetn(page, label, title,id)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<HomeTopicConentEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        stateLayout.showContentView();
                        hideProgress();
                        if (ex.getCode()==-102){
                            if (page>1) {
                                ToastUtils.showShort("已经没有更多了");
                                smartRefreshLayoutTopic.finishLoadMoreWithNoMoreData();
                            }else {
                                rcViewTopic.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        }else {
                            rcViewTopic.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onNext(ConentEntity<HomeTopicConentEntity> conentEntity) {
                        hideProgress();
                        stateLayout.showContentView();
                        if (conentEntity.getContent().size()==0&&page==1){
                            rcViewTopic.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                        }else {
                            dataAdd(conentEntity);
                            rcViewTopic.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        }
                        LogUtils.e(conentEntity.getContent().toString());

                    }
                });

    }

    private void dataAdd(ConentEntity<HomeTopicConentEntity> conentEntity) {
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            HomeTopicConentEntity queryRecommendEntity = new HomeTopicConentEntity();
            queryRecommendEntity = conentEntity.getContent().get(i);
            list.add(queryRecommendEntity);
        }
        if (topicAdapter == null || page == 1) {
            topicAdapter = new TopicAdapter(getActivity());
            topicAdapter.addData(list);
            rcViewTopic.setAdapter(topicAdapter);
            rcViewTopic.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        } else {
            topicAdapter.addData(list);
            topicAdapter.notifyDataSetChanged();
        }

    }


    @OnClick(R.id.rl_error)
    public void onViewClicked() {
        if (smartRefreshLayoutTopic!=null) {
            smartRefreshLayoutTopic.autoRefresh(100);
        }else {
            list.clear();
            httpTopicType(page, String.valueOf(label),"",0);
        }
    }
}
