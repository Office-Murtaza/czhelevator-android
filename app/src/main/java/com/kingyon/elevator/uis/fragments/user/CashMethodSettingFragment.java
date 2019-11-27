package com.kingyon.elevator.uis.fragments.user;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.CashMethodSettingPresenter;
import com.kingyon.elevator.uis.activities.cooperation.AddNewBankCardActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawActivity;
import com.kingyon.elevator.uis.adapters.BindedCardAdapter;
import com.kingyon.elevator.uis.adapters.IncomeDetailsAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现方式选择界面
 */
public class CashMethodSettingFragment extends MvpBaseFragment<CashMethodSettingPresenter> {

    @BindView(R.id.tv_add_card)
    TextView tv_add_card;
    @BindView(R.id.bank_card_list_view)
    RecyclerView bank_card_list_view;

    BindedCardAdapter bindedCardAdapter;
    LinearLayoutManager linearLayoutManager;
    private CooperationInfoEntity entity;

    @Override
    public CashMethodSettingPresenter initPresenter() {
        return new CashMethodSettingPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        entity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        initData();
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_cash_method_setting;
    }


    @OnClick({R.id.tv_add_card})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_card:
                MyActivityUtils.goActivity(getActivity(), AddNewBankCardActivity.class, "ZFB");
                break;
        }
    }


    public static CashMethodSettingFragment newInstance(CooperationInfoEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        CashMethodSettingFragment fragment = new CashMethodSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void initData() {
        List<IncomeDetailsEntity> incomeEntityList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            incomeEntityList.add(new IncomeDetailsEntity());
        }
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bank_card_list_view.setLayoutManager(linearLayoutManager);
        bindedCardAdapter = new BindedCardAdapter(getActivity(), incomeEntityList);
        bank_card_list_view.setAdapter(bindedCardAdapter);
        bindedCardAdapter.setBaseOnItemClick(new BaseOnItemClick<Integer>() {
            @Override
            public void onItemClick(Integer data, int position) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
                MyActivityUtils.goActivity(getActivity(), CooperationWithdrawActivity.class, bundle);
            }
        });
    }

}
