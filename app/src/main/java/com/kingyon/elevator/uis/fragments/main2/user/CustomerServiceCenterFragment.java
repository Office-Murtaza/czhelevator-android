package com.kingyon.elevator.uis.fragments.main2.user;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.CustomerServiceCenterAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.order.OrderAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/7/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 客服中心
 */

public class CustomerServiceCenterFragment extends FoundFragemtUtils {
    String type;
    @BindView(R.id.rcv_order_list)
    RecyclerView rcvOrderList;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    Unbinder unbinder;
    CustomerServiceCenterAdapter adapter;

    public CustomerServiceCenterFragment setIndex(String type) {
        this.type = type;
        return (this);
    }

    @Override
    protected void lazyLoad() {
        if (rcvOrderList!=null) {
            adapter = new CustomerServiceCenterAdapter((BaseActivity) getActivity(), type);
            rcvOrderList.setAdapter(adapter);
            rcvOrderList.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        }else {
            LogUtils.e("***************");

        }

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_customer_service_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {

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
                break;
            case R.id.rl_notlogin:
                break;
        }
    }
}
