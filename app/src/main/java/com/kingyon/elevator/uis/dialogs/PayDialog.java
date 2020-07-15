package com.kingyon.elevator.uis.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.FragmentConstants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.CommitOrderEntiy;
import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.entities.FreshOrderEntity;
import com.kingyon.elevator.finger.FingerprintCallback;
import com.kingyon.elevator.finger.FingerprintVerifyManager;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.order.OrderDetailsActivity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.DialogUtils;
import com.kingyon.elevator.utils.MyActivityUtils;
import com.kingyon.elevator.utils.utilstwo.PayTimerUtils;
import com.kingyon.paylibrary.PayListener;
import com.kingyon.paylibrary.alipay.AliPayUtils;
import com.kingyon.paylibrary.entitys.PayWay;
import com.kingyon.paylibrary.wechatpay.WxPayStatusEntity;
import com.kingyon.paylibrary.wechatpay.WxPayUtils;
import com.leo.afbaselibrary.listeners.IWeakHandler;
import com.leo.afbaselibrary.nets.entities.DataEntity;
import com.leo.afbaselibrary.nets.entities.WxPayEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.PayApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.leo.afbaselibrary.utils.WeakHandler;
import com.zhaoss.weixinrecorded.util.EventBusConstants;
import com.zhaoss.weixinrecorded.util.EventBusObjectEntity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;
import static com.czh.myversiontwo.utils.CodeType.KEYBOARD_PAY;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_PAY_SUCCESS;
import static com.kingyon.elevator.photopicker.UtilsHelper.getString;
import static com.kingyon.elevator.utils.utilstwo.SoftkeyboardUtils.hideInput;

/**
 * @Created By Admin  on 2020/6/4
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class PayDialog extends Dialog implements IWeakHandler,TipDialog.OnOperatClickListener<Double> {
    CommitOrderEntiy orderEntiy;

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_pay_time)
    TextView tvPayTime;
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.img_balance)
    ImageView imgBalance;
    @BindView(R.id.ll_balance)
    LinearLayout llBalance;
    @BindView(R.id.img_wechat)
    ImageView imgWechat;
    @BindView(R.id.ll_wechat)
    LinearLayout llWechat;
    @BindView(R.id.img_alipay)
    ImageView imgAlipay;
    @BindView(R.id.tv_alipay)
    LinearLayout tvAlipay;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_payment)
    TextView tvPayment;

    private AliPayUtils aliPayUtils;
    private WxPayUtils wxPayUtils;
    private MvpBaseActivity context;
    private Subscription delaySubscribe;
//    private OrderDetailsEntity detailsEntity;
    private Float myWallet;
    private Float priceActual;
    private boolean isSetPayPassword;

    public PayDialog(@NonNull MvpBaseActivity context, CommitOrderEntiy orderEntiy,float priceActual) {
        super(context, R.style.ShareDialog);
        setContentView(R.layout.dialog_pay);
        this.orderEntiy = orderEntiy;
        this.context = context;
        this.priceActual = priceActual;
        WeakHandler weakHandler = new WeakHandler(this);
        aliPayUtils = new AliPayUtils(context, weakHandler);
        wxPayUtils = new WxPayUtils(context);
        Window window = getWindow();
        if (window != null) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT);
            window.setWindowAnimations(com.kingyon.library.social.R.style.dialog_show_anim);
            window.setGravity(Gravity.BOTTOM);
        }

        httpWallet();
    }

    private void setPayEnableDelay() {
        delaySubscribe = Observable.just("")
                .delay(2000, TimeUnit.MILLISECONDS)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    public void onNext(String s) {
                        llWechat.setEnabled(true);
                        tvAlipay.setEnabled(true);
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
        tvAlipay.setEnabled(true);
        llBalance.setEnabled(true);
        llWechat.setEnabled(true);
    }

    private void httpWallet() {
        NetService.getInstance().myWallet()
                .subscribe(new CustomApiCallback<DataEntity<Float>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
//                        showToast(ex.getDisplayMessage());
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(DataEntity<Float> floatDataEntity) {
                        if (floatDataEntity == null) {
                            throw new ResultException(9001, "没有获取到钱包余额");
                        }
                        myWallet = floatDataEntity.getData();
                        tvBalance.setText(String.format("T币（￥%s可用）", CommonUtil.getMayTwoFloat(myWallet != null ? myWallet : 0)));
                    }
                });
        NetService.getInstance().vaildInitPayPwd()
                .subscribe(new CustomApiCallback<CooperationInfoNewEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {

                    }

                    @Override
                    public void onNext(CooperationInfoNewEntity cooperationInfoNewEntity) {
                        isSetPayPassword = cooperationInfoNewEntity.isSetPayPassword();
                    }
                });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        imgBalance.setSelected(true);
        tvTitle.setText(orderEntiy.getPayMoney()+"");
        PayTimerUtils payTimerUtils = new PayTimerUtils(tvPayTime, 60000, 1000);
        payTimerUtils.start();
    }


    public void setPaddingTop(int top) {
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.y = top;
            window.setAttributes(lp);
        }
    }


    @OnClick({R.id.ll_balance, R.id.ll_wechat, R.id.tv_alipay, R.id.tv_cancel, R.id.tv_payment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_balance:
                /*余额选项*/
                initview();
                imgBalance.setSelected(true);
                break;
            case R.id.ll_wechat:
                /*微信选项*/
                initview();
                imgWechat.setSelected(true);
                break;
            case R.id.tv_alipay:
                /*支付宝选项*/
                initview();
                imgAlipay.setSelected(true);
                break;
            case R.id.tv_cancel:
                /*取消*/
                dismiss();
                break;
            case R.id.tv_payment:
                /*点击支付*/
                LogUtils.e(imgBalance.isSelected(),imgWechat.isSelected(),imgAlipay.isSelected());
                if (isOverdue()) {
                    /*是否是余额支付*/
                    if (imgBalance.isSelected()) {
                        /*是否设置支付密码*/
                        if (isSetPayPassword) {
                            /*余额是否充足*/
                            if (myWallet >= priceActual) {
                                /*是否设置指纹*/
                                if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)) {
                                    fingerprintInit();
                                } else {
                                    /*支付密码支付*/
                                    DialogUtils.getInstance().showInputPayPwdToCashDailog(getContext(), KEYBOARD_PAY, password -> {
                                        if (password.equals("susser")) {
                                            /*判断是否设置指纹*/
                                            if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)) {
                                                fingerprintInit();
                                            } else {
                                                ToastUtils.showToast(context, "你还没有设置指纹支付，请设置再试", 1000);
                                            }
                                        } else {
                                            /*密码支付*/
                                            httpPatment("BALANCE", password);
                                        }
                                    });
                                }
                            } else {
                                ToastUtils.showToast(context, "余额不足请使用其他支付方式", 1000);
                            }
                        } else {
                            /*跳转设置支付密码*/
                            context.showShortToast("当前还未设置支付密码，请先设置支付密码");
//                            MyActivityUtils.goFragmentContainerActivity(getContext(), FragmentConstants.SetPasswordFragment, "setting");
                        }
                    }else {
                        /*微信支付宝支付*/
                        if (imgAlipay.isSelected()){
                            httpPatment("ALI", "");
                        }else {
                            httpPatment("WECHAT", "");
                        }

                    }

                }else {
                    /*超时*/
                    dismiss();
                    ToastUtils.showToast(context,"请重新支付",1000);
                }
                break;
        }
    }

    /*初始化选项*/
    private void initview() {
        imgAlipay.setSelected(false);
        imgBalance.setSelected(false);
        imgWechat.setSelected(false);

    }

    /*打开指纹支付*/
    private void fingerprintInit() {
        FingerprintVerifyManager.Builder builder = new FingerprintVerifyManager.Builder((Activity) context);
        builder.callback(fingerprintCallback)
                .fingerprintColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .build(KEYBOARD_PAY);
    }


    /*指纹对象*/
    private FingerprintCallback fingerprintCallback = new FingerprintCallback() {
        @Override
        public void onSucceeded() {
            ToastUtils.showToast(context,"指纹验证成功！",1000);
            httpPatment("FINGERPRINT_PAY","");
        }
        @Override
        public void onFailed() {
        LogUtils.e("onFailed");
        }
        @Override
        public void onUsepwd() {
            LogUtils.e("onUsepwd");

            DialogUtils.getInstance().showInputPayPwdToCashDailog(getContext(),KEYBOARD_PAY, password -> {
//                DialogUtils.getInstance().hideInputPayPwdToCashDailog();
                LogUtils.e(password);
                if (password.equals("susser")){
                    if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)){
                        fingerprintInit();
                        hideInput(context);
                    }else {
                        fingerprintInit();
                        ToastUtils.showToast(context,"你还没有设置指纹支付，请设置再试",1000);
                    }
                }else {
                    httpPatment("BALANCE", password);
                }
            });

        }
        @Override
        public void onCancel() {
            ToastUtils.showToast(context,"您取消了指纹验证",1000);
        }
        @Override
        public void tooManyAttempts() {
            ToastUtils.showToast(context,"验证错误次数过多，请30s稍后再试",10000);
        }

        @Override
        public void onHwUnavailable() {
            ToastUtils.showToast(context,"您的手机暂不支持指纹识别或指纹识别不可用",1000);
        }

        @Override
        public void onNoneEnrolled() {
            ToastUtils.showToast(context,"您还未录入指纹，请先去系统设置里录入指纹！",1000);
        }

    };

    /*请求支付*/
    private void httpPatment(String type,String payPwd) {
        LogUtils.e(orderEntiy.getOrderId(),type,payPwd);

        context.showProgressDialog("请稍候...", false);
        setPayEnableDelay();
        NetService.getInstance().orderPay(orderEntiy.getOrderId(),type,payPwd)
                .subscribe(new CustomApiCallback<WxPayEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(),ex.getCode());
                        context.hideProgressDailog();
                        DialogUtils.getInstance().hideInputPayPwdToCashDailog();
                        ToastUtils.showToast(context,ex.getDisplayMessage(),1000);
                        if (type.equals("BALANCE")) {
                            if (ex.getCode() == -100) {
                                DialogUtils.getInstance().showInputPayPwdToCashDailog(getContext(), KEYBOARD_PAY, password -> {
                                    LogUtils.e(password);
                                    if (password.equals("susser")) {
                                        if (DataSharedPreferences.getBoolean(DataSharedPreferences.IS_OPEN_FINGER, false)) {
                                            fingerprintInit();
                                        } else {
                                            ToastUtils.showToast(context, "你还没有设置指纹支付，请设置再试", 1000);
                                        }
                                    } else {
                                        httpPatment("BALANCE", password);
                                    }
                                });
                            }
                        }
                        if (ex instanceof PayApiException) {
                            WxPayEntity payResultEntity = ((PayApiException) ex).getPayEntity();
                            if (payResultEntity != null) {
                                int code = ex.getCode();
                                switch (code) {
                                    case 8001:
                                        wxPayUtils.payOrder(AppContent.getInstance().getGson().toJson(payResultEntity));
                                        LogUtils.e(AppContent.getInstance().getGson().toJson(payResultEntity));
                                        break;
                                    case 8002:
                                        aliPayUtils.pay(payResultEntity.getSign());
                                        LogUtils.e(payResultEntity.getSign());
                                        break;
                                    default:
                                       ToastUtils.showToast(context,"返回参数异常",1000);
//                                        setPayEnable();
                                        LogUtils.e("返回参数异常");
                                        break;
                                }
                            } else {
                                ToastUtils.showToast(context,"返回参数异常",1000);
                                LogUtils.e("返回参数异常");
//                                hideProgress();
//                                setPayEnable();
                            }
                        }
                    }

                    @Override
                    public void onNext(WxPayEntity wxPayEntity) {

                        DialogUtils.getInstance().hideInputPayPwdToCashDailog();
                        context.hideProgressDailog();
                        if (wxPayEntity == null) {
                            throw new ResultException(9000, "返回参数异常");
                        }
//                        hideProgress();
                        switch (wxPayEntity.getPayType()) {
                            case Constants.PayType.ALI_PAY:
                                aliPayUtils.pay(wxPayEntity.getSign());
                                break;
                            case Constants.PayType.WX_PAY:
                                wxPayUtils.payOrder(AppContent.getInstance().getGson().toJson(wxPayEntity));
                                break;
                            case Constants.PayType.BALANCE_PAY:
                                LogUtils.e(orderEntiy.getOrderId(),Constants.PayType.BALANCE_PAY);
                                if (wxPayEntity.getReturn_code().equals("SUCCESS")){
                                    EventBus.getDefault().post(new FreshOrderEntity());
                                    dismiss();
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString(CommonUtil.KEY_VALUE_1, orderEntiy.getOrderId());
//                                        bundle.putString(CommonUtil.KEY_VALUE_2, Constants.PayType.BALANCE_PAY);
//                                        startActivity(PaySuccessActivity.class, bundle)
                                    ActivityUtils.setActivity(ACTIVITY_PAY_SUCCESS,"orderId",orderEntiy.getOrderId(),
                                            "payType",Constants.PayType.BALANCE_PAY,"priceActual", String.valueOf(priceActual));
                                        context.finish();
                                }else {
                                    /* */
                                    ToastUtils.showToast(context,"支付失败",1000);
                                }
                                break;
                            default:
//                                showToast(getString(R.string.pay_Success));
                                LogUtils.e("------------------");
//                                Bundle bundle = new Bundle();
//                                bundle.putString(CommonUtil.KEY_VALUE_1, orderEntiy.getOrderId());
//                                bundle.putString(CommonUtil.KEY_VALUE_2, Constants.PayType.BALANCE_PAY);
//                                startActivity(new Intent(context,PaySuccessActivity.class), bundle);
                                ActivityUtils.setActivity(ACTIVITY_PAY_SUCCESS,"orderId",orderEntiy.getOrderId(),
                                        "payType",Constants.PayType.BALANCE_PAY,"priceActual", String.valueOf(priceActual));
                                context.finish();
                                break;
                        }
                    }
                });


    }

    @Override
    public void handleMessage(Message msg) {
        LogUtils.e(msg.obj,msg.arg1,msg.arg2,msg.replyTo,msg.toString());
        if (msg.what == AliPayUtils.SDK_PAY_FLAG) {
            aliPayUtils.checkResult((Map<String, String>) msg.obj, new PayListener() {
                @Override
                public void onPaySuccess(PayWay payWay) {
                    ToastUtils.showToast(context,getString(R.string.pay_Success),1000);
                    dismiss();
                    EventBus.getDefault().post(new FreshOrderEntity());
//                    if (detailsEntity != null) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString(CommonUtil.KEY_VALUE_1, orderEntiy.getOrderId());
//                        bundle.putString(CommonUtil.KEY_VALUE_2, Constants.PayType.ALI_PAY);
//                        startActivity(PaySuccessActivity.class, bundle);
                    ActivityUtils.setActivity(ACTIVITY_PAY_SUCCESS,"orderId",orderEntiy.getOrderId(),
                            "payType",Constants.PayType.ALI_PAY,"priceActual", String.valueOf(priceActual));

//                    }
                    EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.ReflashPlanList, null));
                    setPayEnable();
                    context.finish();
                    LogUtils.e(payWay.toString());
                }

                @Override
                public void onPayFailure(PayWay payWay, String reason) {
                    ToastUtils.showToast(context,TextUtils.isEmpty(reason) ? getString(R.string.pay_failed) : reason,1000);
                    setPayEnable();
                    LogUtils.e(payWay.toString(),reason);
                }

                @Override
                public void onPayCancel(PayWay payWay) {
                    ToastUtils.showToast(context,getString(R.string.pay_canceled),1000);
                    setPayEnable();
                    LogUtils.e(payWay.toString());
                }

                @Override
                public void onPayConfirm(PayWay payWay) {
                    LogUtils.e(payWay.toString());
                    ToastUtils.showToast(context,getString(R.string.pay_on_ensure),1000);
                    EventBus.getDefault().post(new FreshOrderEntity());
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonUtil.KEY_VALUE_1,orderEntiy.getOrderId() );
                    startActivity(OrderDetailsActivity.class, bundle);
                    setPayEnable();
                    context.finish();
                }
            });
        }
    }

    @Subscribe
    public void onWxPayResult(WxPayStatusEntity wxPayStatusEntity) {
        LogUtils.e(wxPayStatusEntity.getCode()+"=========");
        if (wxPayStatusEntity != null) {
            wxPayUtils.checkResult(wxPayStatusEntity, new PayListener() {
                @Override
                public void onPaySuccess(PayWay payWay) {
                    dismiss();
                    EventBus.getDefault().post(new FreshOrderEntity());
//                        Bundle bundle = new Bundle();
//                        bundle.putString(CommonUtil.KEY_VALUE_1, orderEntiy.getOrderId());
//                        bundle.putString(CommonUtil.KEY_VALUE_2, Constants.PayType.WX_PAY);
//                        startActivity(PaySuccessActivity.class, bundle);
                    ActivityUtils.setActivity(ACTIVITY_PAY_SUCCESS,"orderId",orderEntiy.getOrderId(),
                            "payType",Constants.PayType.WX_PAY,"priceActual", String.valueOf(priceActual));
                    EventBus.getDefault().post(new EventBusObjectEntity(EventBusConstants.ReflashPlanList, null));
                    setPayEnable();
                    context.finish();
                }

                @Override
                public void onPayFailure(PayWay payWay, String reason) {
//                    showToast(TextUtils.isEmpty(reason) ? getString(R.string.pay_failed) : reason);
                    setPayEnable();
                }

                @Override
                public void onPayCancel(PayWay payWay) {
//                    showToast(getString(R.string.pay_canceled));
                    setPayEnable();
                }

                @Override
                public void onPayConfirm(PayWay payWay) {
//                    showToast(getString(R.string.pay_on_ensure));
                    EventBus.getDefault().post(new FreshOrderEntity());
                    Bundle bundle = new Bundle();
                    bundle.putString(CommonUtil.KEY_VALUE_1, orderEntiy.getOrderId());
                    startActivity(OrderDetailsActivity.class, bundle);
                    setPayEnable();
                    context.finish();
                }
            });
        }
    }


    public boolean isOverdue(){
        if (tvPayTime.getText().toString().equals("订单已过期")){
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onEnsureClick(Double param) {
        httpPatment("BALANCE","");
    }

    @Override
    public void onCancelClick(Double param) {

    }
}
