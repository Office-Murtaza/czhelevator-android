package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.YesterDayIncomePresenter;
import com.kingyon.elevator.uis.adapters.IncomeAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 昨日收益详情 或者 已提现详情
 */
public class YesterDayIncomeFragment extends MvpBaseFragment<YesterDayIncomePresenter> {

    private String dataType = "";
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.income_list_container)
    RecyclerView income_list_container;

    LinearLayoutManager linearLayoutManager;
    IncomeAdapter incomeAdapter;


    @Override
    public YesterDayIncomePresenter initPresenter() {
        return new YesterDayIncomePresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        if (getArguments() != null) {
            dataType = getArguments().getString("dataType");
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
        List<IncomeEntity> incomeEntityList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            incomeEntityList.add(new IncomeEntity());
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        income_list_container.setLayoutManager(linearLayoutManager);
        incomeAdapter = new IncomeAdapter(getActivity(), incomeEntityList);
        income_list_container.setAdapter(incomeAdapter);
    }


    public static YesterDayIncomeFragment newInstance(String dataType) {
        Bundle args = new Bundle();
        args.putString("dataType", dataType);
        YesterDayIncomeFragment fragment = new YesterDayIncomeFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
