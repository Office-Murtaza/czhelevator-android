package com.kingyon.elevator.uis.fragments.order;

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
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.order.OrderAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
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

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_LOGIN;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderFragment extends FoundFragemtUtils {
    String type, type1;
    @BindView(R.id.rcv_order_list)
    RecyclerView rcvOrderList;
    Unbinder unbinder;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rl_notlogin;
    private int page = 1;
    List<OrderDetailsEntity> list = new ArrayList<>();
    OrderAdapter orderAdapter;

    @Override
    protected void lazyLoad() {
        if (smartRefreshLayout!=null) {
            smartRefreshLayout.autoRefresh(100);
        }else {
            httpList(type, type1, page);
        }
    }

    private void httpList(String type, String type1, int page) {
        rcvOrderList = getView().findViewById(R.id.rcv_order_list);

        NetService.getInstance().orderList(type, type1, page)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<OrderDetailsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(getContext(), ex.getDisplayMessage(), 1000);
                            } else {
                                rcvOrderList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                                rl_notlogin.setVisibility(View.GONE);
                            }

                        } else if (ex.getCode() == 100200) {
                            rcvOrderList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.VISIBLE);
                        } else {
                            rcvOrderList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<OrderDetailsEntity> orderDetailsEntityPageListEntity) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        addData(orderDetailsEntityPageListEntity);
                        if (orderDetailsEntityPageListEntity.getContent().size()>0||page>1) {
                            rcvOrderList.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.GONE);
                        }else {
                            rcvOrderList.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            rl_notlogin.setVisibility(View.GONE);
                        }
                    }
                });


    }

    private void addData(ConentEntity<OrderDetailsEntity> orderDetailsEntityPageListEntity) {
        for (int i = 0; i < orderDetailsEntityPageListEntity.getContent().size(); i++) {
            OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity();
            orderDetailsEntity = orderDetailsEntityPageListEntity.getContent().get(i);
            list.add(orderDetailsEntity);
        }
        if (orderAdapter == null || page == 1) {
            orderAdapter = new OrderAdapter((BaseActivity) getActivity());
            orderAdapter.addData(list);
            rcvOrderList.setAdapter(orderAdapter);
            rcvOrderList.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        } else {
            orderAdapter.addData(list);
            orderAdapter.notifyDataSetChanged();
        }

    }

    public OrderFragment setIndex(String type, String type1) {
        this.type = type;
        this.type1 = type1;
        return (this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_ordertwo;
    }

    @Override
    public void init(Bundle savedInstanceState) {


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                list.clear();
                page = 1;
                httpList(type, type1, page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                httpList(type, type1, page);
            }
        });
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


    @OnClick({R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_error:
                page = 1;
                list.clear();
                httpList(type, type1, page);
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                break;
        }
    }
}
