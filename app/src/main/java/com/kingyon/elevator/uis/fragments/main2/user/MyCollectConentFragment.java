package com.kingyon.elevator.uis.fragments.main2.user;

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
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.MycollectConentAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
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
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:内容收藏
 */
public class MyCollectConentFragment extends FoundFragemtUtils {

    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;

    MycollectConentAdapter attentionAdapter;
    List<QueryRecommendEntity> recommendEntityList = new ArrayList<>();
    private int page = 1;
    @Override
    protected void lazyLoad() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.autoRefresh(100);
        } else {
            recommendEntityList.clear();
            httpRecommend(page, "");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_my_collect_conent;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableAutoLoadMore(true);
    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtils.e("推荐");
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                recommendEntityList.clear();
                httpRecommend(1, "");
                smartRefreshLayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                recommendEntityList.clear();
                httpRecommend(page, "");
                smartRefreshLayout.finishLoadMore();
            }
        });
    }
    private void httpRecommend(int page, String title) {
        NetService.getInstance().setQueryRecommend(page, title)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<QueryRecommendEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        closeRefresh();
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        if (ex.getCode()==-102){
                            if (page>1) {
                                ToastUtils.showShort("已经没有更多了");
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }else {
                                rvComment.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        }else {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<QueryRecommendEntity> conentEntity) {
                        closeRefresh();
                        rvComment.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        dataAdd(conentEntity);
                    }
                });
    }
    private void dataAdd(ConentEntity<QueryRecommendEntity> conentEntity) {
        for (int i = 0;i<conentEntity.getContent().size();i++){
            QueryRecommendEntity queryRecommendEntity = new QueryRecommendEntity();
            queryRecommendEntity = conentEntity.getContent().get(i);
            recommendEntityList.add(queryRecommendEntity);
        }
        if (attentionAdapter == null || page == 1) {
            attentionAdapter = new MycollectConentAdapter((BaseActivity) getActivity());
            attentionAdapter.addData(recommendEntityList);
            rvComment.setAdapter(attentionAdapter);
            rvComment.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        } else {
            attentionAdapter.addData(recommendEntityList);
            attentionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_error:
                httpRecommend(1,"");
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }

    public void closeRefresh() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }
}
