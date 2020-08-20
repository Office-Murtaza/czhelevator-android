package com.kingyon.elevator.uis.fragments.main2.found.topic;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
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
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TopicDetailsFragment extends FoundFragemtUtils {
    AttentionAdapter attentionAdapter;
    List<QueryRecommendEntity> recommendEntityList = new ArrayList<>();
    @BindView(R.id.rv_attention_list)
    RecyclerView rvAttentionList;
    Unbinder unbinder;
    String type;
    int topicId;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    int page = 1;
    StateLayout stateLayout;
    ImageView img_release;
    @Override
    public int getContentViewId() {
        return R.layout.fragment_topic_deatils;
    }

    public TopicDetailsFragment setIndex(String type, int topicId, StateLayout stateLayout, ImageView img_release) {
        this.type = type;
        this.topicId = topicId;
        this.stateLayout = stateLayout;
        this.img_release = img_release;
        return (this);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onRefresh");
                page = 1;
                recommendEntityList.clear();
                httpQueryAttention(1, topicId, type);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                httpQueryAttention(page, topicId, type);
            }
        });

    }

    private void httpQueryAttention(int page, int  topicId, String type) {
        LogUtils.e(page, topicId, type);
        NetService.getInstance().steTopicAttention(page, topicId, type)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<QueryRecommendEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        hideProgress();
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(getContext(), ex.getDisplayMessage(), 1000);
                            } else {
                                rvAttentionList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }

                        } else if (ex.getCode() == 100200) {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        } else {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            stateLayout.showErrorView();
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<QueryRecommendEntity> conentEntity) {
                        hideProgress();
                        stateLayout.showContentView();
                        img_release.setVisibility(View.VISIBLE);
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        rvAttentionList.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        dataAdd(conentEntity);

                    }
                });


    }

    private void dataAdd(ConentEntity<QueryRecommendEntity> conentEntity) {
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            QueryRecommendEntity queryRecommendEntity = new QueryRecommendEntity();
            queryRecommendEntity = conentEntity.getContent().get(i);
            recommendEntityList.add(queryRecommendEntity);
        }
        if (attentionAdapter == null || page == 1) {
            attentionAdapter = new AttentionAdapter((BaseActivity) getActivity());
            attentionAdapter.addData(recommendEntityList);
            rvAttentionList.setAdapter(attentionAdapter);
            rvAttentionList.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        } else {
            attentionAdapter.addData(recommendEntityList);
            attentionAdapter.notifyDataSetChanged();
        }
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

    @Override
    protected void lazyLoad() {
        if (smartRefreshLayout!=null) {
            smartRefreshLayout.autoRefresh(100);
        }else {
            httpQueryAttention(1, topicId, type);
        }
    }


    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected void dealLeackCanary() {

    }

    @OnClick(R.id.rl_error)
    public void onViewClicked() {
        httpQueryAttention(1, topicId, type);
    }
}
