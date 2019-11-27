package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.IncomeOrPayDetailsPresenter;
import com.kingyon.elevator.uis.adapters.IncomeDetailsAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 收益或者支出详情
 */
public class IncomeOrPayDetailsFragment extends MvpBaseFragment<IncomeOrPayDetailsPresenter> {


    @BindView(R.id.income_details_list_view)
    RecyclerView income_details_list_view;
    LinearLayoutManager linearLayoutManager;
    IncomeDetailsAdapter incomeDetailsAdapter;

    @Override
    public IncomeOrPayDetailsPresenter initPresenter() {
        return new IncomeOrPayDetailsPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
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
        List<IncomeDetailsEntity> incomeEntityList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            incomeEntityList.add(new IncomeDetailsEntity());
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        income_details_list_view.setLayoutManager(linearLayoutManager);
        incomeDetailsAdapter = new IncomeDetailsAdapter(getActivity(), incomeEntityList);
        income_details_list_view.setAdapter(incomeDetailsAdapter);
    }
}
