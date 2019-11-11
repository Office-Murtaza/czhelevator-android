package com.kingyon.paylibrary;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.kingyon.paylibrary.alipay.AliPayUtils;
import com.kingyon.paylibrary.alipay.PayResult;
import com.kingyon.paylibrary.wechatpay.WxConstants;
import com.kingyon.paylibrary.wechatpay.WxPayUtils;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Leo on 2016/5/20.
 */
public class Test {

    public void alipay(){
        AliPayUtils utils = new AliPayUtils(null,new Handler(){
            @Override
            public void dispatchMessage(Message msg) {
                super.dispatchMessage(msg);
                switch (msg.what){
                    case AliPayUtils.SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((String) msg.obj);
                        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                        String resultInfo = payResult.getResult();
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        Log.i("Dream", resultStatus + "===" + resultInfo + "===" + payResult.getMemo());
                        if (TextUtils.equals(resultStatus, "9000")) {
//                            "支付成功"
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
//                                支付结果确认中
                            } else {
//                                支付失败
                            }
                        }
                        break;
                    }
                }

            }
        });
    }

    public void wechatPay(){
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(null, null);
        msgApi.registerApp(WxConstants.APP_ID);
        new WxPayUtils(null).payOrder("");
        //注意在包名中添加payactivity 回调
    }


}
