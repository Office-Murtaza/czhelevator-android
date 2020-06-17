package com.kingyon.elevator.uis.fragments.user;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.mvpbase.MvpBaseFragment;
import com.kingyon.elevator.presenter.PartnerFragmentPresenter;
import com.kingyon.elevator.uis.activities.cooperation.CooperationDeviceActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.view.PartnerFragmentView;
import com.leo.afbaselibrary.widgets.StateLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 合伙人界面新
 */
public class PartnerFragment extends MvpBaseFragment<PartnerFragmentPresenter> implements PartnerFragmentView {

    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.service_phone_num)
    TextView service_phone_num;
    @BindView(R.id.btn_apply_crash)
    TextView btn_apply_crash;
    @BindView(R.id.tv_device_manager)
    TextView tv_device_manager;
    @BindView(R.id.crash_money_history)
    TextView crash_money_history;
    @BindView(R.id.tv_can_crash)
    TextView tv_can_crash;
    @BindView(R.id.tv_already_crash)
    TextView tv_already_crash;
    @BindView(R.id.yesterday_income)
    TextView yesterday_income;
    @BindView(R.id.tv_all_income)
    TextView tv_all_income;
    private CooperationInfoEntity entity;

    @Override
    public PartnerFragmentPresenter initPresenter() {
        return new PartnerFragmentPresenter(getActivity());
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        ButterKnife.bind(this, getContentView());
        stateLayout.showProgressView();

    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_partner;
    }


    @OnClick({R.id.tv_device_manager,R.id.btn_apply_crash,R.id.service_phone_num,R.id.crash_money_history})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_device_manager:
                Bundle bundle = new Bundle();
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.RoleType.PARTNER);
                MyActivityUtils.goActivity(getActivity(),CooperationDeviceActivity.class,bundle);
                break;
            case R.id.btn_apply_crash:

                break;
            case R.id.service_phone_num:

                break;
            case R.id.crash_money_history:

                break;
        }
    }


    public static PartnerFragment newInstance(CooperationInfoEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        PartnerFragment fragment = new PartnerFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
