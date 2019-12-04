package com.kingyon.elevator.uis.fragments.user;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.EventBusConstants;
import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.EventBusObjectEntity;
import com.kingyon.elevator.entities.IncomeDetailsEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.interfaces.SelectCashBindTypeListener;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.CashMethodSettingPresenter;
import com.kingyon.elevator.uis.activities.cooperation.AddNewBankCardActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawActivity;
import com.kingyon.elevator.uis.adapters.BindedCardAdapter;
import com.kingyon.elevator.uis.adapters.IncomeDetailsAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.CashMethodSettingView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 提现方式选择界面
 */
public class CashMethodSettingFragment extends MvpBaseFragment<CashMethodSettingPresenter> implements CashMethodSettingView {

    @BindView(R.id.tv_add_card)
    TextView tv_add_card;
    @BindView(R.id.bank_card_list_view)
    RecyclerView bank_card_list_view;
    @BindView(R.id.card_container)
    LinearLayout card_container;
    BindedCardAdapter bindedCardAdapter;
    LinearLayoutManager linearLayoutManager;


    private CooperationInfoNewEntity entity;

    @Override
    public CashMethodSettingPresenter initPresenter() {
        return new CashMethodSettingPresenter(getActivity());
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        EventBus.getDefault().register(this);
        setStateLayout();
        stateLayout.setErrorAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.getAccountList();
            }
        });
        if (getArguments() != null) {
            entity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        }
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
                Bundle bundle = new Bundle();
                bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
                DialogUtils.getInstance().showSelectCashBindTypeDialog(getActivity(), new SelectCashBindTypeListener() {
                    @Override
                    public void selectBindZfb() {
                        MyActivityUtils.goActivity(getActivity(), AddNewBankCardActivity.class, bundle, "ZFB");
                    }

                    @Override
                    public void selectBindBankCard() {
                        MyActivityUtils.goActivity(getActivity(), AddNewBankCardActivity.class, bundle, "BANK");
                    }
                });
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventHandler(EventBusObjectEntity eventBusObjectEntity) {
        if (eventBusObjectEntity.getEventCode() == EventBusConstants.ReflashBindAccountList) {
            LogUtils.d("绑定完成了刷新列表数据-----------------");
            presenter.getAccountList();
        }
    }


    public static CashMethodSettingFragment newInstance(CooperationInfoNewEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        CashMethodSettingFragment fragment = new CashMethodSettingFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void initData() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        bank_card_list_view.setLayoutManager(linearLayoutManager);
        bindedCardAdapter = new BindedCardAdapter(getActivity(), presenter.getBindAccountEntityList());
        bank_card_list_view.setAdapter(bindedCardAdapter);
        bindedCardAdapter.setBaseOnItemClick((bindAccountEntity, position) -> {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
            RuntimeUtils.selectBindAccountEntity = bindAccountEntity;
            MyActivityUtils.goActivity(getActivity(), CooperationWithdrawActivity.class, bundle);
        });
        presenter.getAccountList();
    }

    @Override
    public void onPause() {
        super.onPause();
        DialogUtils.getInstance().hideSelectCashBindTypeDialog();
    }

    @Override
    public void showListData(List<BindAccountEntity> bindAccountEntities) {
        if (bindedCardAdapter != null) {
            bindedCardAdapter.reflashData(bindAccountEntities);
            if (bindAccountEntities.size() == 0) {
                card_container.setVisibility(View.GONE);
            }else {
                card_container.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
