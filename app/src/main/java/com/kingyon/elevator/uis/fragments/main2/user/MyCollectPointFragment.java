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
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.RecommendHouseEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.MycollectPointAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
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
 * @Instructions:点位收藏
 */
public class MyCollectPointFragment extends FoundFragemtUtils {

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
    private String latitude,longitude;
    private  int cityId;
    private int page = 1;
    private String provinceCode, cityCode,  countyCode;
    MycollectPointAdapter mycollectPointAdapter;
    private List<RecommendHouseEntiy> list = new ArrayList<>();
    @Override
    protected void lazyLoad() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.autoRefresh(100);
        } else {
            list.clear();
            showProgressDialog(getString(R.string.wait));
            httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
        }

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_my_collect_point;
    }


    @Override
    public void init(Bundle savedInstanceState) {
        LocationEntity entity = AppContent.getInstance().getLocation();
        if (entity!=null) {
            latitude = String.valueOf(entity.getLatitude());
            longitude = String.valueOf(entity.getLongitude());
        }

    }

    private void httpRecommendHouse(int page, String latitude, String longitude, int cityId,
                                    String areaId, String pointType, String keyWord, String distance) {
        LogUtils.e(page, latitude, longitude, cityId, areaId, pointType, keyWord, distance);
        NetService.getInstance().setmyCollects1(page, latitude, longitude)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<RecommendHouseEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        hideProgress();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showShort("已经没有更多了");
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                rvComment.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<RecommendHouseEntiy> conentEntity) {
                        LogUtils.e(conentEntity.getContent().toString());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        dataAdd(conentEntity);
                        if (conentEntity.getContent().size()>0||page>1) {
                            rvComment.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                        }else {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                        }
                        hideProgress();
                    }
                });


    }

    private void dataAdd(ConentEntity<RecommendHouseEntiy> conentEntity) {
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            RecommendHouseEntiy queryRecommendEntity = new RecommendHouseEntiy();
            queryRecommendEntity = conentEntity.getContent().get(i);
            list.add(queryRecommendEntity);
        }
        if (rvComment == null || page == 1) {
            rvComment.setNestedScrollingEnabled(false);
            rvComment.setFocusable(false);
            mycollectPointAdapter = new MycollectPointAdapter((BaseActivity) getActivity());
            mycollectPointAdapter.addData(list);
            rvComment.setAdapter(mycollectPointAdapter);
            rvComment.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

        } else {
            mycollectPointAdapter.addData(list);
            mycollectPointAdapter.notifyDataSetChanged();
        }
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
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                page=1;
                httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
            }
        });

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
                httpRecommendHouse(1, latitude, longitude, cityId, "", "", "", "");
                break;
            case R.id.rl_notlogin:
                break;
        }
    }
}
