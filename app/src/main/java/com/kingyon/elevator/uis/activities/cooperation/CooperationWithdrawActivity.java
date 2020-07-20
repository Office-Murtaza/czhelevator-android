package com.kingyon.elevator.uis.activities.cooperation;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.entities.entities.UserCashTypeListEnity;
import com.kingyon.elevator.finger.FingerprintCallback;
import com.kingyon.elevator.finger.FingerprintVerifyManager;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.AccountDialog;
import com.kingyon.elevator.uis.dialogs.WithdrawSuccessDialog;
import com.kingyon.elevator.uis.widgets.CustomImageSpan;
import com.kingyon.elevator.uis.widgets.DecimalDigitsInputFilter;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.KEYBOARD_PAY;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 * 2.0提现申请
 */

public class CooperationWithdrawActivity extends BaseSwipeBackActivity {
    @BindView(R.id.pre_v_right)
    TextView preVRight;
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
    @BindView(R.id.tv_shuilv)
    TextView tv_shuilv;
    @BindView(R.id.ed_input_cash_money)
    EditText ed_input_cash_money;
    @BindView(R.id.tv_shuihou_suode)
    TextView tv_shuihou_suode;
    @BindView(R.id.tv_cash_all_money)
    TextView tv_cash_all_money;
    @BindView(R.id.tv_account_type)
    TextView tv_account_type;
    @BindView(R.id.tv_account_num)
    TextView tv_account_num;
    @BindView(R.id.tv_account_name)
    TextView tv_account_name;
    @BindView(R.id.tv_confirm_cash)
    TextView tv_confirm_cash;

    private CooperationInfoNewEntity entity;
    private UserCashTypeListEnity bindAccountEntity;

    private OptionsPickerView wayPicker;
    private List<NormalParamEntity> wayOptions;
    private WithdrawSuccessDialog successDialog;

    @Override
    protected String getTitleText() {
        entity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_1);
        bindAccountEntity = getIntent().getParcelableExtra(CommonUtil.KEY_VALUE_2);
        if (entity == null) {
            entity = new CooperationInfoNewEntity();
        }
        LogUtils.e(entity.toString());
        return "申请提现";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_withdraw;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        // preVRight.setText("提现记录");
        if (bindAccountEntity == null) {
            LogUtils.d("跳转到提现界面了-----------------------异常");
            showToast("数据异常，请重试");
            finish();
            return;
        }
        setCashInfoData(bindAccountEntity);
        preVRight.setVisibility(View.GONE);
        tvTip.setText(getString(R.string.cooperation_withdraw_tip));
        updateMoneyInfo();
        ed_input_cash_money.setFilters( new InputFilter[]{new InputFilter.LengthFilter(10)});
        ed_input_cash_money.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(this,10,true,"提现金额超过限制值，请重新输入")});
        ed_input_cash_money.addTextChangedListener(new TextWatcher() {
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
//                tv_shuihou_suode.setText(CommonUtil.getMayTwoFloat(money - Float.parseFloat(CommonUtil.getTwoFloat(taxation))));
                tv_shuihou_suode.setText(CommonUtil.getMayTwoFloat(money * (1-0.06))+"元");
                tv_shuilv.setText(String.format("（扣除%s元税费）", CommonUtil.getTwoFloat(money * 0.06)));

        }
        });
        updateWayUi(Constants.WithdrawType.BANKCARD, "银行卡");
    }

    private void setCashInfoData(UserCashTypeListEnity bindAccountEntity) {
        if (bindAccountEntity.cashType== 1) {
            tv_account_type.setText(bindAccountEntity.openingBank+"("+(bindAccountEntity.cashAccount.substring(bindAccountEntity.cashAccount.length()-4))+")");
            tv_account_num.setText(AccountNumUtils.hideBankCardNum(bindAccountEntity.cashAccount));
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.ic_cashout_bank);
            Drawable rightDrawable1 = getResources().getDrawable(R.mipmap.ic_arrow_right);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());  // left, top, right, bottom
            tv_account_type.setCompoundDrawables(rightDrawable, null, rightDrawable1, null);
        } else if (bindAccountEntity.cashType == 2){
            tv_account_type.setText("支付宝（"+AccountNumUtils.hidePhoneNum(bindAccountEntity.cashAccount)+")");
            tv_account_num.setText(AccountNumUtils.hidePhoneNum(bindAccountEntity.cashAccount));
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.ic_cashout_alipay);
            Drawable rightDrawable1 = getResources().getDrawable(R.mipmap.ic_arrow_right);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());  // left, top, right, bottom
            tv_account_type.setCompoundDrawables(rightDrawable, null, rightDrawable1, null);
        }else {
            tv_account_type.setText("微信("+AccountNumUtils.hidePhoneNum(bindAccountEntity.cashAccount)+")");
            tv_account_num.setText(AccountNumUtils.hidePhoneNum(bindAccountEntity.cashAccount));
            Drawable rightDrawable = getResources().getDrawable(R.mipmap.ic_cashout_wechat);
            Drawable rightDrawable1 = getResources().getDrawable(R.mipmap.ic_arrow_right);
            rightDrawable.setBounds(0, 0, rightDrawable.getMinimumWidth(), rightDrawable.getMinimumHeight());  // left, top, right, bottom
            tv_account_type.setCompoundDrawables(rightDrawable, null, rightDrawable1, null);
        }
        tv_account_name.setText(bindAccountEntity.cashName);
    }

    private void updateMoneyInfo() {
//        tv_shuilv.setText(String.format("(含税%s元)", CommonUtil.getTwoFloat(entity.getRealizableIncome() * 0.06)));
        ed_input_cash_money.setHint(String.format("可提现金额为￥%s元", CommonUtil.getTwoFloat(entity.getRealizableIncome())));
    }

    @OnClick({R.id.pre_v_right, R.id.ll_way, R.id.tv_ensure, R.id.tv_confirm_cash, R.id.tv_cash_all_money,R.id.ll_account_type})
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
            case R.id.tv_confirm_cash:
                checkData();
                break;
            case R.id.tv_cash_all_money:
                ed_input_cash_money.setText(entity.getRealizableIncome() + "");
                break;
            case R.id.ll_account_type:
                AccountDialog accountDialog = new AccountDialog(this);
                accountDialog.show();
                accountDialog.setDialogOnClick(new AccountDialog.DialogOnClick() {
                    @Override
                    public void onClick(UserCashTypeListEnity enity) {
                        LogUtils.e(enity.toString());
                        setCashInfoData(enity);
                        bindAccountEntity = enity;
                    }
                });
                break;
        }
    }


    private void cashHandler() {
        if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)) {
            //开启了指纹识别，验证指纹
            fingerprintInit();
        } else {
            showPayPwdDialog();
        }
    }

    private void showPayPwdDialog() {
        DialogUtils.getInstance().showInputPayPwdToCashDailog(CooperationWithdrawActivity.this,KEYBOARD_PAY, password -> {
            DialogUtils.getInstance().hideInputPayPwdToCashDailog();
            if (password.equals("susser")){
                if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)) {
                    fingerprintInit();
                }else {
                    ToastUtils.showShort("你还没有开通指纹 开通再试吧");
                }
            }else {
                checkPayPasswordIsRight(password);
            }
        });
    }


    public void checkPayPasswordIsRight(String pwd) {
        showProgressDialog("支付密码验证中...");
        NetService.getInstance().vaildPasswordIsRight(pwd)
                .compose(this.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        hideProgress();
                    }

                    @Override
                    public void onNext(String content) {
                        hideProgress();
                        onEnsureClick();
//                        if (content.equals("成功")) {
//
//                        } else {
//                            showToast("支付密码错误");
//                        }
                    }
                });
    }

    private void fingerprintInit() {
        FingerprintVerifyManager.Builder builder = new FingerprintVerifyManager.Builder(this);
        builder.callback(fingerprintCallback)
                .fingerprintColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .build(KEYBOARD_PAY);
    }


    private FingerprintCallback fingerprintCallback = new FingerprintCallback() {
        @Override
        public void onSucceeded() {
            onEnsureClick();
        }

        @Override
        public void onFailed() {
            LogUtils.d("指纹识别失败--------");
        }

        @Override
        public void onUsepwd() {
            showPayPwdDialog();
        }

        @Override
        public void onCancel() {
            LogUtils.d("指纹识别取消");
            showPayPwdDialog();
        }

        @Override
        public void tooManyAttempts() {
            LogUtils.d("指纹识别尝试次数过多-------------");
            DialogUtils.getInstance().hideFingerCheckDailog();
            showPayPwdDialog();
        }

        @Override
        public void onHwUnavailable() {
            LogUtils.d("指纹识别模块不可用");
            showToast("您的手机暂不支持指纹识别或指纹识别不可用");
            showPayPwdDialog();
        }

        @Override
        public void onNoneEnrolled() {
            LogUtils.d("指纹识别模块不可用");
            showToast("您还未录入指纹，请先去系统设置里录入指纹！");
            DataSharedPreferences.saveBoolean(DataSharedPreferences.IS_OPEN_FINGER, false);
            showPayPwdDialog();
        }
    };

    private void checkData() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(ed_input_cash_money))) {
            showToast("请输入提现金额");
            return;
        }
        Double money;
        try {
            money = Double.parseDouble(CommonUtil.getEditText(ed_input_cash_money));
        } catch (NumberFormatException e) {
            money = 0D;
        }
        if (money <= 0) {
            showToast("提现金额必须大于0");
            return;
        }
        if (money > entity.getRealizableIncome()) {
            showToast("超过可提现金额");
            return;
        }
        cashHandler();
    }


    private void onEnsureClick() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(ed_input_cash_money))) {
            showToast("请输入提现金额");
            return;
        }
        Double money;
        try {
            money = Double.parseDouble(CommonUtil.getEditText(ed_input_cash_money));
        } catch (NumberFormatException e) {
            money = 0D;
        }
        if (money <= 0) {
            showToast("提现金额必须大于0");
            return;
        }
        if (money > entity.getRealizableIncome()) {
            showToast("超过可提现金额");
            return;
        }
        if (bindAccountEntity.cashType == 1) {
            //银行卡
            requestWithdraw(money, Constants.WithdrawType.BANKCARD, null,"",
                    bindAccountEntity.openingBank, bindAccountEntity.cashAccount, bindAccountEntity.cashName);
        } else if (bindAccountEntity.cashType == 2){
            //支付宝
            requestWithdraw(money, Constants.WithdrawType.ALI, bindAccountEntity.cashAccount, "",null, null, bindAccountEntity.cashName);
        }else {
            requestWithdraw(money, Constants.WithdrawType.WCHAT, "",bindAccountEntity.cashAccount, null, null, bindAccountEntity.cashName);

        }
    }

    private void requestWithdraw(final double money, String way, String aliAccount,String wChatAcount, String bankName, String bankNo, String bankHolder) {
        tvEnsure.setEnabled(false);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().partnerWithdraw(money, way, aliAccount,wChatAcount, bankName, bankNo, bankHolder)
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
                        EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.ReflashPartnerInfo,null));
                        hideProgress();
                        entity.setRealizableIncome(entity.getRealizableIncome() - money);
                        updateMoneyInfo();
                        ed_input_cash_money.setText("");
                        ed_input_cash_money.setSelection(ed_input_cash_money.getText().length());
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
