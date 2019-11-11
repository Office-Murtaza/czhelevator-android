package com.kingyon.paylibrary.unionpay;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.kingyon.paylibrary.PayListener;
import com.kingyon.paylibrary.entitys.PayWay;
import com.unionpay.UPPayAssistEx;
import com.unionpay.uppay.PayActivity;

/**
 * Created by gongli on 2017/7/14 14:39
 * email: lc824767150@163.com
 */

public class UPPayUtils {
    /*****************************************************************
     * mMode参数解释： "00" - 启动银联正式环境 "01" - 连接银联测试环境
     *****************************************************************/
    public static final String mMode = "00";
    public static final String TN_URL_01 = "http://101.231.204.84:8091/sim/getacptn";
    private Context context;

    public UPPayUtils(Context context) {
        this.context = context;
    }

    public void pay(String tn) {
        UPPayAssistEx.startPayByJAR(context, PayActivity.class, null, null,
                tn, mMode);
    }

    public void checkResult(int requestCode, int resultCode, Intent data, PayListener payListener) {
        if (requestCode == 10 && resultCode == Activity.RESULT_OK && data != null && payListener != null) {
            /*************************************************
             * 处理银联手机支付控件返回的支付结果
             ************************************************/
            /*
             * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
             */
            String str = data.getExtras().getString("pay_result");
            if (str.equalsIgnoreCase("success")) {
                // 支付成功后，extra中如果存在result_data，取出校验
                // result_data结构见c）result_data参数说明
                if (data.hasExtra("result_data")) {
                    payListener.onPaySuccess(PayWay.UNIONPAY);
                } else if (str.equalsIgnoreCase("fail")) {
                    payListener.onPayFailure(PayWay.UNIONPAY,null);
                } else if (str.equalsIgnoreCase("cancel")) {
                    payListener.onPayCancel(PayWay.UNIONPAY);
                } else {
                    payListener.onPayConfirm(PayWay.UNIONPAY);
                }
//                AlertDialog.Builder builder = new AlertDialog.Builder(this);
//                builder.setTitle("支付结果通知");
//                builder.setMessage(msg);
//                builder.setInverseBackgroundForced(true);
//                // builder.setCustomTitle();
//                builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                });
//                builder.create().show();
            } else if (str.equalsIgnoreCase("cancel")) {
                payListener.onPayCancel(PayWay.UNIONPAY);
            }
        }
    }
}
