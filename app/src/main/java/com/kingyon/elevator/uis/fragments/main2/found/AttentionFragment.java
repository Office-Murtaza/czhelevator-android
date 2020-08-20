package com.kingyon.elevator.uis.fragments.main2.found;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.leo.afbaselibrary.widgets.WrapContentLinearLayoutManager;
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
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:关注
 */
public class AttentionFragment extends FoundFragemtUtils {
    @BindView(R.id.rv_attention_list)
    RecyclerView rvAttentionList;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rl_notlogin;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    private View view;
    // 标志位，标志已经初始化完成。
    public static boolean isRefresh = true;
    AttentionAdapter attentionAdapter;
    List<QueryRecommendEntity> recommendEntityList = new ArrayList<>();
    private int page = 1;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_attention;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }


    private void httpQueryAttention(int page, String title, String orderBy) {
//        showProgressDialog("请稍后...");

        NetService.getInstance().setQueryAttention(page, title, orderBy)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<QueryRecommendEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        hideProgress();
                        closeRefresh();
                        stateLayout.showContentView();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(getContext(), ex.getDisplayMessage(), 1000);
                            } else {
                                rvAttentionList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                                rl_notlogin.setVisibility(View.GONE);
                            }

                        } else if (ex.getCode() == 100200) {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.VISIBLE);

                        } else {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<QueryRecommendEntity> conentEntity) {
                        stateLayout.showContentView();
                        hideProgress();
                        closeRefresh();
                        rvAttentionList.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        rl_notlogin.setVisibility(View.GONE);
                        dataAdd(conentEntity);


                    }
                });


    }

    private void dataAdd(ConentEntity<QueryRecommendEntity> conentEntity) {
        try {
            for (int i = 0; i < conentEntity.getContent().size(); i++) {
                QueryRecommendEntity queryRecommendEntity = new QueryRecommendEntity();
                queryRecommendEntity = conentEntity.getContent().get(i);
                recommendEntityList.add(queryRecommendEntity);
            }
            if (attentionAdapter == null || page == 1) {
                attentionAdapter = new AttentionAdapter((BaseActivity) getActivity());
                attentionAdapter.addData(recommendEntityList);
                attentionAdapter.setHasStableIds(true);
                rvAttentionList.setAdapter(attentionAdapter);

                rvAttentionList.setLayoutManager(new WrapContentLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                attentionAdapter.notifyDataSetChanged();
            } else {
                attentionAdapter.addData(recommendEntityList);
                attentionAdapter.notifyDataSetChanged();
            }
        } catch (IndexOutOfBoundsException e) {
            LogUtils.e(e.toString());
        }
    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                recommendEntityList.clear();
                stateLayout.showProgressView(getString(R.string.wait));
                httpQueryAttention(1, "", "");
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                httpQueryAttention(page, "", "");
            }
        });
        stateLayout.setErrorAndEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stateLayout.showProgressView(getString(R.string.wait));
                httpQueryAttention(1, "", "");
            }
        });

    }

    public void closeRefresh() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
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
//        if (recommendEntityList.size() < 0) {
//        if (PublicFuncation.isIntervalTenMin()) {
            if (smartRefreshLayout != null) {
                smartRefreshLayout.autoRefresh(100);
            } else {
                stateLayout.showProgressView(getString(R.string.wait));
                httpQueryAttention(1, "", "");
            }

//        }
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (isRefresh) {
            smartRefreshLayout.autoRefresh(100);
        }

    }

    @OnClick({R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_error:
                if (stateLayout!=null){
                    stateLayout.showProgressView(getString(R.string.wait));
                }
                httpQueryAttention(1, "", "");
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }


}
