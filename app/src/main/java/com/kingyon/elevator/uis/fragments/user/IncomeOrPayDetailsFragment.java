package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.ChartSelectParameterEntity;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.IncomeOrPayDetailsPresenter;
import com.kingyon.elevator.uis.adapters.IncomeDetailsAdapter;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.IncomeOrPayDetailsView;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收益或者支出详情
 */
public class IncomeOrPayDetailsFragment extends MvpBaseFragment<IncomeOrPayDetailsPresenter> implements IncomeOrPayDetailsView {


    @BindView(R.id.income_details_list_view)
    RecyclerView income_details_list_view;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;


    LinearLayoutManager linearLayoutManager;
    IncomeDetailsAdapter incomeDetailsAdapter;
    ChartSelectParameterEntity chartSelectParameterEntity;


    @Override
    public IncomeOrPayDetailsPresenter initPresenter() {
        return new IncomeOrPayDetailsPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        setStateLayout();
        smart_refresh_layout.setEnableAutoLoadMore(false);
        initData();

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_income_or_pay_details;
    }

    public static IncomeOrPayDetailsFragment newInstance() {
        Bundle args = new Bundle();
        IncomeOrPayDetailsFragment fragment = new IncomeOrPayDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private void initData() {
        chartSelectParameterEntity = RuntimeUtils.chartSelectParameterEntity;
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        income_details_list_view.setLayoutManager(linearLayoutManager);
        incomeDetailsAdapter = new IncomeDetailsAdapter(getActivity(), presenter.getIncomeDetailsEntityList());
        income_details_list_view.setAdapter(incomeDetailsAdapter);
        smart_refresh_layout.setOnRefreshListener(refreshLayout -> {
            smart_refresh_layout.setEnableLoadMore(true);
            presenter.getDetailsData(ReflashConstants.Refalshing, chartSelectParameterEntity.getSelectIncomeOrPay(),
                    chartSelectParameterEntity.getSelectCatType(),
                    chartSelectParameterEntity.getCurrentSelectYear(),
                    chartSelectParameterEntity.getCurrentSelectMonth(),
                    chartSelectParameterEntity.getCurrentSelectDay());
        });
        smart_refresh_layout.setOnLoadMoreListener(refreshLayout -> presenter.getDetailsData(ReflashConstants.LoadMore, chartSelectParameterEntity.getSelectIncomeOrPay(),
                chartSelectParameterEntity.getSelectCatType(),
                chartSelectParameterEntity.getCurrentSelectYear(),
                chartSelectParameterEntity.getCurrentSelectMonth(),
                chartSelectParameterEntity.getCurrentSelectDay()));
        smart_refresh_layout.autoRefresh();
    }

    @Override
    public void showDetailsListData(List<IncomeDetailsEntity> incomeDetailsEntities) {
        if (incomeDetailsAdapter != null) {
            incomeDetailsAdapter.reflashData(incomeDetailsEntities);
        }
    }

    @Override
    public void loadMoreIsComplete() {
        smart_refresh_layout.setEnableLoadMore(false);
        showShortToast("数据已全部加载完毕");
    }

    @Override
    public void hideProgressDailog() {
        smart_refresh_layout.finishLoadMore();
        smart_refresh_layout.finishRefresh();
    }

}
