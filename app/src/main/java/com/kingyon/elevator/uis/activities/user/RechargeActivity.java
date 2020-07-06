package com.kingyon.elevator.uis.activities.user;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.widgets.DecimalDigitsInputFilter;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.paylibrary.PayListener;
import com.kingyon.paylibrary.alipay.AliPayUtils;
import com.kingyon.paylibrary.entitys.PayWay;
import com.kingyon.paylibrary.wechatpay.WxPayStatusEntity;
import com.kingyon.paylibrary.wechatpay.WxPayUtils;
import com.leo.afbaselibrary.listeners.IWeakHandler;
import com.leo.afbaselibrary.nets.entities.WxPayEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.PayApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseSwipeBackActivity;
import com.leo.afbaselibrary.utils.WeakHandler;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class RechargeActivity extends BaseSwipeBackActivity implements IWeakHandler {
    @BindView(R.id.et_sum)
    EditText etSum;
    @BindView(R.id.img_clear)
    ImageView imgClear;
    @BindView(R.id.tv_ali_pay)
    TextView tvAliPay;
    @BindView(R.id.tv_wx_pay)
    TextView tvWxPay;
    @BindView(R.id.tv_ensure)
    TextView tvEnsure;

    private View[] payViews;
    private AliPayUtils aliPayUtils;
    private WxPayUtils wxPayUtils;
    private Subscription delaySubscribe;

    @Override
    protected String getTitleText() {
        return "充值钱包";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        payViews = new View[]{tvAliPay, tvWxPay};
        etSum.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(2)});
        etSum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
            }
        });
        etSum.setText("100.00");
        etSum.setSelection(etSum.getText().length());
        checkPayType(tvAliPay.getId());
        EventBus.getDefault().register(this);
        WeakHandler weakHandler = new WeakHandler(this);
        aliPayUtils = new AliPayUtils(this, weakHandler);
        wxPayUtils = new WxPayUtils(this);
    }

    @OnClick({R.id.img_clear, R.id.tv_ali_pay, R.id.tv_wx_pay, R.id.tv_ensure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_clear:
                etSum.setText("");
                etSum.setSelection(etSum.getText().length());
                break;
            case R.id.tv_ali_pay:
            case R.id.tv_wx_pay:
                checkPayType(view.getId());
                break;
            case R.id.tv_ensure:
                recharge();
                break;
        }
    }

    private void recharge() {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etSum))) {
            showToast("请输入的充值金额");
            return;
        }
        float sum;
        try {
            sum = Float.parseFloat(etSum.getText().toString());
        } catch (NumberFormatException e) {
            sum = 0f;
        }
        if (sum <= 0) {
            showToast("输入的充值金额不正确");
            return;
        }
        String payWay = getPayWay();
        if (TextUtils.isEmpty(payWay)) {
            showToast("请选择支付方式");
            return;
        }
        tvEnsure.setEnabled(false);
        tvAliPay.setEnabled(false);
        tvWxPay.setEnabled(false);
        showProgressDialog(getString(R.string.wait));
        setPayEnableDelay();
        NetService.getInstance().rechageWallet(payWay, sum)
                .compose(this.<WxPayEntity>bindLifeCycle())
                .subscribe(new CustomApiCallback<WxPayEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (ex instanceof PayApiException) {
                            WxPayEntity payResultEntity = ((PayApiException) ex).getPayEntity();
                            if (payResultEntity != null) {
                                hideProgress();
                                int code = ex.getCode();
                                switch (code) {
                                    case 8001:
                                        wxPayUtils.payOrder(AppContent.getInstance().getGson().toJson(payResultEntity));
                                        break;
                                    case 8002:
                                        aliPayUtils.pay(payResultEntity.getSign());
                                        break;
                                    default:
                                        showToast("返回参数异常");
                                        setPayEnable();
                                        break;
                                }
                            } else {
                                showToast("返回参数异常");
                                hideProgress();
                                setPayEnable();
                            }
                        } else {
                            setPayEnable();
                            showToast(ex.getDisplayMessage());
                            hideProgress();
                        }
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                    }

                    @Override
                    public void onNext(WxPayEntity wxPayEntity) {
                        LogUtils.e(wxPayEntity.toString());
                        if (wxPayEntity == null) {
                            throw new ResultException(9000, "返回参数异常");
                        }
                        hideProgress();
                        switch (wxPayEntity.getPayType()) {
                            case Constants.PayType.ALI_PAY:
                                aliPayUtils.pay(wxPayEntity.getSign());
                                break;
                            case Constants.PayType.WX_PAY:
                                wxPayUtils.payOrder(AppContent.getInstance().getGson().toJson(wxPayEntity));
                                break;
                        }
                    }
                });
    }

    @Subscribe
    public void onWxPayResult(WxPayStatusEntity wxPayStatusEntity) {
        if (wxPayStatusEntity != null) {
            wxPayUtils.checkResult(wxPayStatusEntity, new PayListener() {
                @Override
                public void onPaySuccess(PayWay payWay) {
                    showToast(getString(R.string.pay_Success));
                    setPayEnable();
                    finish();
                }

                @Override
                public void onPayFailure(PayWay payWay, String reason) {
                    showToast(TextUtils.isEmpty(reason) ? getString(R.string.pay_failed) : reason);
                    setPayEnable();
                }

                @Override
                public void onPayCancel(PayWay payWay) {
                    showToast(getString(R.string.pay_canceled));
                    setPayEnable();
                }

                @Override
                public void onPayConfirm(PayWay payWay) {
                    showToast(getString(R.string.pay_on_ensure));
                    setPayEnable();
                    finish();
                }
            });
        }
    }

    @Override
    public void handleMessage(Message msg) {
        if (msg.what == AliPayUtils.SDK_PAY_FLAG) {
            aliPayUtils.checkResult((Map<String, String>) msg.obj, new PayListener() {
                @Override
                public void onPaySuccess(PayWay payWay) {
                    showToast(getString(R.string.pay_Success));
                    setPayEnable();
                    finish();
                }

                @Override
                public void onPayFailure(PayWay payWay, String reason) {
                    showToast(TextUtils.isEmpty(reason) ? getString(R.string.pay_failed) : reason);
                    setPayEnable();
                }

                @Override
                public void onPayCancel(PayWay payWay) {
                    showToast(getString(R.string.pay_canceled));
                    setPayEnable();
                }

                @Override
                public void onPayConfirm(PayWay payWay) {
                    showToast(getString(R.string.pay_on_ensure));
                    setPayEnable();
                    finish();
                }
            });
        }
    }

    private void setPayEnableDelay() {
        delaySubscribe = Observable.just("")
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    public void onNext(String s) {
                        tvAliPay.setEnabled(true);
                        tvWxPay.setEnabled(true);
                    }

                    @Override
                    protected void onResultError(ApiException ex) {

                    }
                });
    }

    private void setPayEnable() {
        if (delaySubscribe != null && !delaySubscribe.isUnsubscribed()) {
            delaySubscribe.unsubscribe();
        }
        tvEnsure.setEnabled(true);
        tvAliPay.setEnabled(true);
        tvWxPay.setEnabled(true);
    }

    private void checkPayType(int viewId) {
        if (payViews == null) {
            return;
        }
        for (View view : payViews) {
            view.setSelected(view.getId() == viewId);
        }
    }

    private String getPayWay() {
        String result;
        switch (getPayViewId()) {
            case R.id.tv_ali_pay:
                result = Constants.PayType.ALI_PAY;
                break;
            case R.id.tv_wx_pay:
                result = Constants.PayType.WX_PAY;
                break;
            default:
                result = "";
                break;
        }
        return result;
    }

    private int getPayViewId() {
        int result = 0;
        if (payViews != null) {
            for (View view : payViews) {
                if (view.isSelected()) {
                    result = view.getId();
                    break;
                }
            }
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
