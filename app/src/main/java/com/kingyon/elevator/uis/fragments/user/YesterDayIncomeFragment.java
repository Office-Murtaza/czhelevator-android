package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.date.DateUtils;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.YesterdayIncomeEntity;
import com.kingyon.elevator.entities.entities.EarningsYesterdayEnity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.YesterDayIncomePresenter;
import com.kingyon.elevator.uis.adapters.adapterone.IncomeAdapter;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.YesterDayIncomeView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 昨日收益详情 或者 已提现详情
 */
public class YesterDayIncomeFragment extends MvpBaseFragment<YesterDayIncomePresenter> implements YesterDayIncomeView {

    private String dataType = "";
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.income_list_container)
    RecyclerView income_list_container;
    @BindView(R.id.yesterday_time)
    TextView yesterday_time;
    @BindView(R.id.total_money)
    TextView total_money;

    LinearLayoutManager linearLayoutManager;
    IncomeAdapter incomeAdapter;
    CooperationInfoNewEntity cooperationInfoNewEntity;


    @Override
    public YesterDayIncomePresenter initPresenter() {
        return new YesterDayIncomePresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        setStateLayout();
        if (getArguments() != null) {
            dataType = getArguments().getString("dataType");
            cooperationInfoNewEntity = RuntimeUtils.cooperationInfoNewEntity;
            initData();
        } else {
            getActivity().finish();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_yester_day_income;
    }


    private void initData() {
        if (dataType.equals(FragmentConstants.YesterDayIncomeFragment)) {
            //查询昨日收益
            yesterday_time.setText(DateUtils.getYesterDayTime());
            total_money.setText("合计\t\t¥" + cooperationInfoNewEntity.getYesterdayIncome());

        } else {
            yesterday_time.setText(DateUtils.getCurrentTime());
            total_money.setText("合计\t\t¥" + cooperationInfoNewEntity.getFulfilledIncome());
        }
        stateLayout.setEmptyAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smart_refresh_layout.autoRefresh();
            }
        });
        stateLayout.setErrorAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smart_refresh_layout.autoRefresh();
            }
        });
        smart_refresh_layout.setEnableAutoLoadMore(false);
        smart_refresh_layout.setOnRefreshListener(refreshLayout -> {
            smart_refresh_layout.setEnableLoadMore(true);
            if (dataType.equals(FragmentConstants.YesterDayIncomeFragment)) {
                //查询昨日收益
                presenter.getYesterdayIncomeData(ReflashConstants.Refalshing);
            } else {
                presenter.getCashData(ReflashConstants.Refalshing);
            }
        });
        smart_refresh_layout.setOnLoadMoreListener(refreshLayout -> {
            if (dataType.equals(FragmentConstants.YesterDayIncomeFragment)) {
                //查询昨日收益
                presenter.getYesterdayIncomeData(ReflashConstants.LoadMore);
            } else {
                presenter.getCashData(ReflashConstants.LoadMore);
            }
        });
        smart_refresh_layout.autoRefresh();
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        income_list_container.setLayoutManager(linearLayoutManager);
        incomeAdapter = new IncomeAdapter(getActivity(), presenter.getYesterdayIncomeEntityList());
        income_list_container.setAdapter(incomeAdapter);
    }


    public static YesterDayIncomeFragment newInstance(String dataType) {
        Bundle args = new Bundle();
        args.putString("dataType", dataType);
        YesterDayIncomeFragment fragment = new YesterDayIncomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void showDetailsListData(List<EarningsYesterdayEnity> incomeDetailsEntities) {
        if (incomeAdapter != null) {
            incomeAdapter.reflashData(incomeDetailsEntities);
        }
        if (incomeDetailsEntities.size() == 0) {
            showEmptyContentView("暂无数据哦！");
        }
    }

    @Override
    public void hideProgressDailog() {
        smart_refresh_layout.finishRefresh();
        smart_refresh_layout.finishLoadMore();
    }

    @Override
    public void loadMoreIsComplete() {
        smart_refresh_layout.setEnableLoadMore(false);
        showShortToast("数据全部加载完毕");
    }
}
