package com.kingyon.elevator.uis.activities.cooperation;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.CooperationInfoEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.TipDialog;
import com.kingyon.elevator.uis.dialogs.WithdrawSuccessDialog;
import com.kingyon.elevator.uis.widgets.CustomImageSpan;
import com.kingyon.elevator.uis.widgets.DecimalDigitsInputFilter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationWithdrawActivity extends BaseSwipeBackActivity {
    @BindView(R.id.pre_v_right)
    TextView preVRight;
    @BindView(R.id.et_money)
    EditText etMoney;
    @BindView(R.id.tv_taxation_percent)
    TextView tvTaxationPercent;
    @BindView(R.id.tv_taxation)
    TextView tvTaxation;
    @BindView(R.id.tv_way)
    TextView tvWay;
    @BindView(R.id.et_ali_account)
    EditText etAliAccount;
    @BindView(R.id.ll_ali_info)
    LinearLayout llAliInfo;
    @BindView(R.id.et_bank_holder)
    EditText etBankHolder;
    @BindView(R.id.et_bank_no)
    EditText etBankNo;
    @BindView(R.id.et_bank_name)
    EditText etBankName;
    @BindView(R.id.ll_bank_info)
    LinearLayout llBankInfo;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;
    @BindView(R.id.tv_tip)
    TextView tvTip;

    private CooperationInfoEntity entity;

    private OptionsPickerView wayPicker;
    private List<NormalParamEntity> wayOptions;
    private WithdrawSuccessDialog successDialog;

    @Override
    protected String getTitleText() {
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        if (entity == null) {
            entity = new CooperationInfoEntity();
        }
        return "申请提现";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_withdraw;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        preVRight.setText("提现记录");
        tvTip.setText(getTipSpan(getString(R.string.cooperation_withdraw_tip)));
        updateMoneyInfo();
        etMoney.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Double money;
                try {
                    money = Double.parseDouble(s.toString());
                } catch (NumberFormatException e) {
                    money = 0D;
                }
                Double taxation = money * entity.getTaxation();
                tvTaxation.setText(CommonUtil.getTwoFloat(taxation));
            }
        });
        updateWayUi(Constants.WithdrawType.BANKCARD, "银行卡");
    }

    private void updateMoneyInfo() {
        etMoney.setHint(String.format("您当前最多可提现￥%s元", CommonUtil.getTwoFloat(entity.getUsefulIncome())));
        tvTaxationPercent.setText(String.format("税点%s%%", CommonUtil.getTwoFloat(entity.getTaxation() * 100)));
    }

    @OnClick({R.id.pre_v_right, R.id.ll_way, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.pre_v_right:
                startActivity(CooperationWithdrawRecordsActivity.class);
                break;
            case R.id.ll_way:
//                showWayPicker();
                break;
            case R.id.tv_ensure:
                onEnsureClick();
                break;
        }
    }

    private void onEnsureClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etMoney))) {
            showToast("请输入提现金额");
            return;
        }
        Double money;
        try {
            money = Double.parseDouble(CommonUtil.getEditText(etMoney));
        } catch (NumberFormatException e) {
            money = 0D;
        }
        if (money <= 0) {
            showToast("提现金额必须大于0");
            return;
        }
        if (money > entity.getUsefulIncome()) {
            showToast("超过可提现金额");
            return;
        }
        if (llAliInfo.getVisibility() == View.GONE && llBankInfo.getVisibility() == View.GONE) {
            showToast("请选择提现方式");
            return;
        }
        if (llAliInfo.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(CommonUtil.getEditText(etAliAccount))) {
                showToast("请输入支付宝账号");
                return;
            }
            requestWithdraw(money, Constants.WithdrawType.ALI, etAliAccount.getText().toString(), null, null, null);
        }
        if (llBankInfo.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(CommonUtil.getEditText(etBankHolder))) {
                showToast("请输入持卡人姓名");
                return;
            }
            if (TextUtils.isEmpty(CommonUtil.getEditText(etBankNo))) {
                showToast("请输入银行卡号");
                return;
            }
//            if (TextUtils.isEmpty(CommonUtil.getEditText(etBankName))) {
//                showToast("请输入银行名称");
//                return;
//            }
            requestWithdraw(money, Constants.WithdrawType.BANKCARD, null, etBankName.getText().toString(), etBankNo.getText().toString(), etBankHolder.getText().toString());
        }
    }

    private void requestWithdraw(final double money, String way, String aliAccount, String bankName, String bankNo, String bankHolder) {
        tvEnsure.setEnabled(false);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().partnerWithdraw(money, way, aliAccount, bankName, bankNo, bankHolder)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                        tvEnsure.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("提交成功");
                        hideProgress();
                        entity.setUsefulIncome(entity.getUsefulIncome() - money);
                        updateMoneyInfo();
                        etMoney.setText("");
                        etMoney.setSelection(etMoney.getText().length());
                        showSuccessDialog();
                    }
                });
    }

    private void showSuccessDialog() {
        if (successDialog == null) {
            successDialog = new WithdrawSuccessDialog(this);
            successDialog.setCanceledOnTouchOutside(false);
            successDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    tvEnsure.setEnabled(true);
                    setResult(RESULT_OK);
                    finish();
                }
            });
        }
        successDialog.show();
    }

    private void showWayPicker() {
        if (wayPicker == null || wayOptions == null) {
            wayOptions = new ArrayList<>();
            wayOptions.add(new NormalParamEntity(Constants.WithdrawType.ALI, "支付宝"));
            wayOptions.add(new NormalParamEntity(Constants.WithdrawType.BANKCARD, "银行卡"));
            wayPicker = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
                @Override
                public void onOptionsSelect(int options1, int options2, int options3, View v) {
                    if (wayOptions == null || wayOptions.size() <= options1) {
                        return;
                    }
                    String way = wayOptions.get(options1).getType();
                    String name = wayOptions.get(options1).getName();
                    updateWayUi(way, name);
                }
            }).setCyclic(false, false, false).build();
        }
        KeyBoardUtils.closeKeybord(this);
        wayPicker.setPicker(wayOptions);
        wayPicker.show();
    }

    private void updateWayUi(String way, String name) {
        llAliInfo.setVisibility(TextUtils.equals(Constants.WithdrawType.ALI, way) ? View.VISIBLE : View.GONE);
        llBankInfo.setVisibility(TextUtils.equals(Constants.WithdrawType.BANKCARD, way) ? View.VISIBLE : View.GONE);
        tvWay.setText(name);
    }

    private SpannableString getTipSpan(String tip) {
        String addressDefault = String.format("%s %s", "提示", tip);
        SpannableString spannableString = new SpannableString(addressDefault);
        ImageSpan span = new CustomImageSpan(this, R.drawable.ic_tip, 2);
        spannableString.setSpan(span, 0, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spannableString;
    }
}
