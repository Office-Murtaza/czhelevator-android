package com.kingyon.elevator.uis.fragments.cooperation;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.customview.MyActionBar;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.FragmentContainerActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationDeviceActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationEarningsActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationIncomeActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawActivity;
import com.kingyon.elevator.uis.activities.cooperation.CooperationWithdrawRecordsActivity;
import com.kingyon.elevator.uis.activities.cooperation.OpticalFeeActivity;
import com.kingyon.elevator.uis.activities.cooperation.PropertyFeeActivity;
import com.kingyon.elevator.uis.activities.devices.CellChooseActivity;
import com.kingyon.elevator.uis.activities.salesman.SalesmanActivity;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.leo.afbaselibrary.widgets.StateLayout;

import butterknife.BindView;
import butterknife.OnClick;

public class CooperationInfoFragment extends BaseFragment implements OnParamsChangeInterface {

    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_withdraw_money)
    TextView tvWithdrawMoney;
    @BindView(R.id.tv_income_today)
    TextView tvIncomeToday;
    @BindView(R.id.tv_income_month)
    TextView tvIncomeMonth;
    @BindView(R.id.tv_income_year)
    TextView tvIncomeYear;
    @BindView(R.id.tv_fee_property)
    TextView tvFeeProperty;
    @BindView(R.id.tv_fee_optical)
    TextView tvFeeOptical;

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
    @BindView(R.id.my_action_bar)
    MyActionBar my_action_bar;
    @BindView(R.id.container_view)
    RelativeLayout container_view;


    private CooperationInfoEntity entity;
    private TipDialog<String> tipDialog;

    public static CooperationInfoFragment newInstance(CooperationInfoEntity entity) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        CooperationInfoFragment fragment = new CooperationInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_cooperation_info;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            entity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
        } else {
            entity = new CooperationInfoEntity();
        }
        StatusBarUtil.setHeadViewPadding(getActivity(), container_view);
        preVBack.setImageDrawable(getBackDrawable(0xFFFFFFFF));
        updateUI(entity);
    }

    @Override
    public void onParamsChange(Object... objects) {
        entity = (CooperationInfoEntity) objects[0];
        updateUI(entity);
    }

    private void updateUI(CooperationInfoEntity entity) {
        tv_all_income.setText(getSumSpan(CommonUtil.getTwoFloat(entity.getAllIncome())));
        tv_can_crash.setText(CommonUtil.getTwoFloat(entity.getUsefulIncome()));
        yesterday_income.setText(CommonUtil.getMayTwoFloat(entity.getTodayIncome()));
        tvIncomeMonth.setText(CommonUtil.getMayTwoFloat(entity.getMouthIncome()));
        tvIncomeYear.setText(CommonUtil.getMayTwoFloat(entity.getYearIncome()));
        tvFeeProperty.setText(CommonUtil.getMayTwoFloat(entity.getPropertyPay()));
        tvFeeOptical.setText(CommonUtil.getMayTwoFloat(entity.getNetworkPay()));
    }

    private CharSequence getSumSpan(String twoFloat) {
        SpannableString spannableString = new SpannableString(twoFloat);
        spannableString.setSpan(new AbsoluteSizeSpan(20, true), twoFloat.indexOf(".") + 1, twoFloat.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @OnClick({R.id.pre_v_back, R.id.btn_apply_crash, R.id.ll_income_today, R.id.ll_income_month,
            R.id.ll_income_year, R.id.ll_fee_property, R.id.ll_fee_optical, R.id.tv_income,
            R.id.tv_device_manager, R.id.ll_cells, R.id.pre_v_right, R.id.crash_money_history,
            R.id.yesterday_income_container, R.id.tv_all_income, R.id.already_crash_container})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.pre_v_back:
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
                break;
            case R.id.crash_money_history:
                startActivity(CooperationWithdrawRecordsActivity.class);
                break;
            case R.id.tv_all_income:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.IncomeRecordFragment);
                break;
            case R.id.yesterday_income_container:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.YesterDayIncomeFragment);
                break;
            case R.id.already_crash_container:
                MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.AlreadyCrashFragment);
                break;
            case R.id.btn_apply_crash:
//                bundle.putParcelable(CommonUtil.KEY_VALUE_1, entity);
//                startActivityForResult(CooperationWithdrawActivity.class, CommonUtil.REQ_CODE_1, bundle);
                MyActivityUtils.goActivity(getActivity(), FragmentContainerActivity.class,FragmentConstants.CashMethodSettingFragment, bundle);
                break;
            case R.id.ll_income_today:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.INCOME_FILTER.DAY);
                startActivity(CooperationIncomeActivity.class, bundle);
                break;
            case R.id.ll_income_month:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.INCOME_FILTER.MONTH);
                startActivity(CooperationIncomeActivity.class, bundle);
                break;
            case R.id.ll_income_year:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.INCOME_FILTER.YEAR);
                startActivity(CooperationIncomeActivity.class, bundle);
                break;
            case R.id.ll_fee_property:
                startActivity(PropertyFeeActivity.class);
                break;
            case R.id.ll_fee_optical:
                startActivity(OpticalFeeActivity.class);
                break;
            case R.id.tv_income:
                startActivity(CooperationEarningsActivity.class);
//                startActivity(CooperationIncomeActivity.class);
                break;
            case R.id.tv_device_manager:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.RoleType.PARTNER);
                startActivity(CooperationDeviceActivity.class, bundle);
                break;
            case R.id.ll_cells:
//                bundle.putBoolean(CommonUtil.KEY_VALUE_3, true);
//                startActivity(CellChooseActivity.class, bundle);
                startActivity(SalesmanActivity.class);
                break;
            case R.id.pre_v_right:
                onTipClick();
                break;
        }
    }

    private void onTipClick() {
        if (tipDialog == null) {
            tipDialog = new TipDialog<>(getContext(), null);
        }
        tipDialog.showEnsureNoClose(String.format("每月提现时间为16-20号，若有疑问可致电：%s", getString(R.string.service_phone)), "知道了", "");
    }

    private Drawable getBackDrawable(int color) {
        Drawable up = ContextCompat.getDrawable(getContext(), R.drawable.ic_back_gray_tint);
        Drawable drawableUp = null;
        if (up != null) {
            drawableUp = DrawableCompat.wrap(up);
            DrawableCompat.setTint(drawableUp, color);
        }
        Drawable up1 = ContextCompat.getDrawable(getContext(), R.drawable.ic_back_gray_tint);
        if (up1 != null) {
            Drawable drawableUp1 = DrawableCompat.unwrap(up1);
            DrawableCompat.setTintList(drawableUp1, null);
        }
        return drawableUp;
    }

    @Override
    protected void dealLeackCanary() {
        LeakCanaryUtils.watchLeakCanary(this);
    }
}
