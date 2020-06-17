package com.kingyon.elevator.uis.fragments.homepage;

import android.os.Bundle;
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
import com.kingyon.elevator.others.AddCellToPlanPresenter;
import com.kingyon.elevator.uis.adapters.adaptertwo.RecommendHouseAdapter;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/5/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TextSearchTwoFragment extends BaseFragment {

    @BindView(R.id.rv_recommended)
    RecyclerView rvRecommended;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    private List<RecommendHouseEntiy> list = new ArrayList<>();
    RecommendHouseAdapter recommendHouseAdapter;
    private AddCellToPlanPresenter addCellToPlanPresenter;
    private  int page = 1;
    private String latitude,longitude;
    private  int cityId;
    private String provinceCode, cityCode,  countyCode;
    @Override
    public int getContentViewId() {
        return R.layout.fragment_text_two_search;
    }
    ConentEntity<RecommendHouseEntiy> entiyConentEntity;
    @Override
    public void init(Bundle savedInstanceState) {
        addCellToPlanPresenter = new AddCellToPlanPresenter((BaseActivity) getActivity());
        LocationEntity entity = AppContent.getInstance().getLocation();
        latitude = String.valueOf(entity.getLatitude());
        longitude = String.valueOf(entity.getLongitude());
        cityCode = String.valueOf(520100) ;
        cityId = 520100;
        initRefresh();

    }
//    public MapSearchFragment newInstance( ConentEntity<RecommendHouseEntiy> entiyConentEntity2) {
//        this.entiyConentEntity = entiyConentEntity2;
//        MapSearchFragment fragment = new MapSearchFragment();
//        return fragment;
//    }

    public TextSearchTwoFragment newInstance(ConentEntity<RecommendHouseEntiy> entiyConentEntity2) {
        this.entiyConentEntity = entiyConentEntity2;
        return (this);
    }
    private void initRefresh() {
//        httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
//        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
//            @Override
//            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
//                list.clear();
//                page=1;
//                httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
//            }
//        });
//        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                page++;
//                httpRecommendHouse(page, latitude, longitude, cityId, "", "", "", "");
//            }
//        });
        dataAdd(entiyConentEntity);
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    private void httpRecommendHouse(int page, String latitude, String longitude, int cityId,
                                    String areaId, String pointType, String keyWord, String distance) {
        LogUtils.e(page,latitude,longitude,cityId,areaId,pointType,keyWord,distance);
        NetService.getInstance().setRecommendHouse(page, latitude, longitude, cityId, areaId, pointType, keyWord, distance)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<RecommendHouseEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        if (ex.getCode()==-102){
                            if (page>1) {
                                ToastUtils.showShort("已经没有更多了");
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            }else {
                                rvRecommended.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        }else {
                            rvRecommended.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<RecommendHouseEntiy> conentEntity) {
                        hideProgress();
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        rvRecommended.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);

                    }
                });


    }

    private void dataAdd(ConentEntity<RecommendHouseEntiy> conentEntity) {
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            RecommendHouseEntiy queryRecommendEntity = new RecommendHouseEntiy();
            queryRecommendEntity = conentEntity.getContent().get(i);
            list.add(queryRecommendEntity);
        }
        if (rvRecommended == null || page == 1) {
            rvRecommended.setNestedScrollingEnabled(false);
            rvRecommended.setFocusable(false);
            recommendHouseAdapter = new RecommendHouseAdapter((BaseActivity) getActivity());
            recommendHouseAdapter.addData(list);
            rvRecommended.setAdapter(recommendHouseAdapter);
            rvRecommended.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
            recommendHouseAdapter.setOnItemClickListener(new RecommendHouseAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    if (addCellToPlanPresenter != null) {

                        addCellToPlanPresenter.showHomeOagePicker(1, null);
                    }
                }
            });

        } else {
            recommendHouseAdapter.addData(list);
            recommendHouseAdapter.notifyDataSetChanged();
        }
    }
}
