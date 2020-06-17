package com.kingyon.elevator.uis.fragments.property;

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
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.PropertyInfoEntity;
import com.kingyon.elevator.others.OnParamsChangeInterface;
import com.kingyon.elevator.uis.activities.property.PropertyDeviceActivity;
import com.kingyon.elevator.uis.activities.property.PropertyEarningsActivity;
import com.kingyon.elevator.uis.activities.property.PropertyIncomeActivity;
import com.kingyon.elevator.uis.activities.property.PropertyInfomationsActivity;
import com.kingyon.elevator.uis.activities.property.PropertySettlementActivity;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.LeakCanaryUtils;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyInfoFragment extends BaseFragment implements OnParamsChangeInterface {
    @BindView(R.id.pre_v_back)
    ImageView preVBack;
    @BindView(R.id.fl_title)
    FrameLayout flTitle;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_withdraw_money)
    TextView tvWithdrawMoney;
    @BindView(R.id.tv_records)
    TextView tvRecords;
    @BindView(R.id.tv_income_today)
    TextView tvIncomeToday;
    @BindView(R.id.tv_income_month)
    TextView tvIncomeMonth;
    @BindView(R.id.tv_income_year)
    TextView tvIncomeYear;
    @BindView(R.id.v_infomation)
    View vInfomation;
    @BindView(R.id.tv_infomation)
    TextView tvInfomation;

    private boolean propertyCell;
    private PropertyInfoEntity entity;
    private TipDialog<String> tipDialog;

    public static PropertyInfoFragment newInstance(PropertyInfoEntity entity, boolean propertyCell) {
        Bundle args = new Bundle();
        args.putParcelable(CommonUtil.KEY_VALUE_1, entity);
        args.putBoolean(CommonUtil.KEY_VALUE_2, propertyCell);
        PropertyInfoFragment fragment = new PropertyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_property_info;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (getArguments() != null) {
            entity = getArguments().getParcelable(CommonUtil.KEY_VALUE_1);
            propertyCell = getArguments().getBoolean(CommonUtil.KEY_VALUE_2);
        } else {
            entity = new PropertyInfoEntity();
        }
        StatusBarUtil.setHeadViewPadding(getActivity(), flTitle);
        preVBack.setImageDrawable(getBackDrawable(0xFFFFFFFF));
        updateUI(entity);
    }

    @Override
    public void onResume() {
//        tvRecords.setVisibility(propertyCell ? View.GONE : View.VISIBLE);
        vInfomation.setVisibility(propertyCell ? View.VISIBLE : View.GONE);
        tvInfomation.setVisibility(propertyCell ? View.VISIBLE : View.GONE);
        super.onResume();
    }

    @Override
    public void onParamsChange(Object... objects) {
        entity = (PropertyInfoEntity) objects[0];
        updateUI(entity);
    }

    private void updateUI(PropertyInfoEntity entity) {
        tvTotalMoney.setText(getSumSpan(CommonUtil.getTwoFloat(entity.getAllIncome())));
        tvWithdrawMoney.setText(CommonUtil.getTwoFloat(entity.getUsefulIncome()));
        tvIncomeToday.setText(CommonUtil.getMayTwoFloat(entity.getTodayIncome()));
        tvIncomeMonth.setText(CommonUtil.getMayTwoFloat(entity.getMouthIncome()));
        tvIncomeYear.setText(CommonUtil.getMayTwoFloat(entity.getYearIncome()));
    }

    private CharSequence getSumSpan(String twoFloat) {
        SpannableString spannableString = new SpannableString(twoFloat);
        spannableString.setSpan(new AbsoluteSizeSpan(20, true), twoFloat.indexOf(".") + 1, twoFloat.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }

    @OnClick({R.id.pre_v_back, R.id.pre_v_right, R.id.tv_records, R.id.ll_income_today, R.id.ll_income_month, R.id.ll_income_year, R.id.tv_income, R.id.tv_devices, R.id.tv_infomation})
    public void onViewClicked(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.pre_v_back:
                FragmentActivity activity = getActivity();
                if (activity != null && !activity.isFinishing()) {
                    activity.finish();
                }
                break;
            case R.id.pre_v_right:
                onTipClick();
                break;
            case R.id.tv_records:
                if (propertyCell) {
                    bundle.putString(CommonUtil.KEY_VALUE_1, Constants.RoleType.NEIGHBORHOODS);
                } else {
                    bundle.putString(CommonUtil.KEY_VALUE_1, Constants.RoleType.PROPERTY);
                }
                startActivity(PropertySettlementActivity.class, bundle);
                break;
            case R.id.ll_income_today:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.INCOME_FILTER.DAY);
                startActivity(PropertyIncomeActivity.class, bundle);
                break;
            case R.id.ll_income_month:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.INCOME_FILTER.MONTH);
                startActivity(PropertyIncomeActivity.class, bundle);
                break;
            case R.id.ll_income_year:
                bundle.putString(CommonUtil.KEY_VALUE_1, Constants.INCOME_FILTER.YEAR);
                startActivity(PropertyIncomeActivity.class, bundle);
                break;
            case R.id.tv_income:
                startActivity(PropertyEarningsActivity.class);
//                startActivity(PropertyIncomeActivity.class);
                break;
            case R.id.tv_devices:
                if (propertyCell) {
                    bundle.putString(CommonUtil.KEY_VALUE_1, Constants.RoleType.NEIGHBORHOODS);
                } else {
                    bundle.putString(CommonUtil.KEY_VALUE_1, Constants.RoleType.PROPERTY);
                }
                startActivity(PropertyDeviceActivity.class, bundle);
                break;
            case R.id.tv_infomation:
                startActivity(PropertyInfomationsActivity.class);
                break;
        }
    }

    private void onTipClick() {
        if (tipDialog == null) {
            tipDialog = new TipDialog<>(getContext(), null);
        }
        tipDialog.showEnsureNoClose(String.format("每月结算时间为15号，若有疑问可致电：%s", getString(R.string.service_phone)), "知道了", "");
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
