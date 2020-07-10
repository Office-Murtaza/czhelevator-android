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
import com.kingyon.elevator.entities.entities.WikipediaEntiy;
import com.kingyon.elevator.uis.adapters.adaptertwo.CustomerServiceCenterAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.order.OrderAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

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

public class CustomerServiceCenterFragment extends BaseFragment {
    String type;
    @BindView(R.id.rcv_order_list)
    RecyclerView rcvOrderList;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    Unbinder unbinder;
    CustomerServiceCenterAdapter adapter;
    List<WikipediaEntiy.WikipediaBean.ItemBean> item;

    public CustomerServiceCenterFragment setIndex(List<WikipediaEntiy.WikipediaBean.ItemBean> item) {
        this.item = item;
        return (this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_customer_service_center;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        adapter = new CustomerServiceCenterAdapter((BaseActivity) getActivity(), item);
        rcvOrderList.setAdapter(adapter);
        rcvOrderList.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

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
