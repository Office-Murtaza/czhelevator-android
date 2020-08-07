package com.kingyon.elevator.uis.fragments.homepage;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.kingyon.elevator.utils.utilstwo.AdUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

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
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                AdUtils.isSX = 2;
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            Thread.sleep(600);
                            smartRefreshLayout.finishRefresh();
                            AdUtils.isSX = 3;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });
    }


    public TextSearchTwoFragment newInstance(ConentEntity<RecommendHouseEntiy> entiyConentEntity2) {
        this.entiyConentEntity = entiyConentEntity2;
        return (this);
    }
    private void initRefresh() {
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



    private void dataAdd(ConentEntity<RecommendHouseEntiy> conentEntity) {
        LogUtils.e(conentEntity+"============");
        if (conentEntity!=null) {
            for (int i = 0; i < conentEntity.getContent().size(); i++) {
                RecommendHouseEntiy queryRecommendEntity = new RecommendHouseEntiy();
                queryRecommendEntity = conentEntity.getContent().get(i);
                list.add(queryRecommendEntity);
            }
            if (rvRecommended == null || page == 1) {
                rvRecommended.setNestedScrollingEnabled(false);
                rvRecommended.setFocusable(false);
                recommendHouseAdapter = new RecommendHouseAdapter((BaseActivity) getActivity(),"1");
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
        }else {
            LogUtils.e("********************");
        }
    }
}
