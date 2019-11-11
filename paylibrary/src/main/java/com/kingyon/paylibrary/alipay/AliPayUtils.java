package com.kingyon.paylibrary.alipay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alipay.sdk.app.PayTask;
import com.kingyon.paylibrary.PayListener;
import com.kingyon.paylibrary.entitys.PayWay;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import static android.R.attr.order;

/**
 * Created by Leo on 2015/11/30
 */
public class AliPayUtils {

    public static final int SDK_PAY_FLAG = 1;
    public static final int SDK_CHECK_FLAG = 2;

    private Context context;
    private Handler mHandler;
    private boolean paying;
    private String payParam;

    public AliPayUtils(Context con, Handler back) {
        this.context = con;
        mHandler = back;
    }

    public void pay(final String param) {
        if (paying && TextUtils.equals(param, payParam)) {
            return;
        }
        paying = true;
        payParam = param;
        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask((Activity) context);
                // 调用支付接口，获取支付结果
                Map<String, String> result = alipay.payV2(param, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                if (mHandler != null) {
                    mHandler.sendMessage(msg);
                }
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    public void checkResult(Map<String, String> result, PayListener payListener) {
        if (payListener == null) {
            return;
        }
        PayResult payResult = new PayResult(result);
        /**
         对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
         */
        String resultStatus = payResult.getResultStatus();
        String des = payResult.getMemo();
        if (TextUtils.equals(resultStatus, "9000")) {
            // 支付成功
            payListener.onPaySuccess(PayWay.ALIPAY);
        } else if (TextUtils.equals(resultStatus, "6001")) {
            //用户取消
            payListener.onPayCancel(PayWay.ALIPAY);
        } else if (TextUtils.equals(resultStatus, "8000") || TextUtils.equals(resultStatus, "6004")) {
            // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
            payListener.onPayConfirm(PayWay.ALIPAY);
        } else {
            //支付失败
            payListener.onPayFailure(PayWay.ALIPAY, des);
        }
        paying = false;
    }
}
