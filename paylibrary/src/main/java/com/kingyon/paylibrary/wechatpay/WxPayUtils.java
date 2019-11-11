package com.kingyon.paylibrary.wechatpay;

import android.content.Context;
import android.widget.Toast;

import com.kingyon.paylibrary.PayListener;
import com.kingyon.paylibrary.entitys.PayWay;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Leo on 2015/11/30
 */
public class WxPayUtils {

    private IWXAPI api;
    private Context context;

    public WxPayUtils(Context context) {
        this.context = context;
        api = WXAPIFactory.createWXAPI(context, WxConstants.APP_ID);
        api.registerApp(WxConstants.APP_ID);
    }

    public void payOrder(String str) {
        if (checkPayEnvironment()) {
            try {
                pay(new JSONObject(str));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(context, "未安装微信或版本过低", Toast.LENGTH_SHORT).show();
        }
    }

    private void pay(JSONObject json) throws JSONException {
        PayReq req = new PayReq();
        req.appId = json.getString("appid");
        req.partnerId = json.getString("partnerid");
        req.prepayId = json.getString("prepayid");
        req.nonceStr = json.getString("noncestr");
        req.timeStamp = json.getString("timestamp");
        req.packageValue = json.getString("packages");
        req.sign = json.getString("sign");
        req.extData = "app data"; // optional
        // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
        api.sendReq(req);
    }

    private boolean checkPayEnvironment() {
        return api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
    }

    public void checkResult(WxPayStatusEntity wxPayStatusEntity, PayListener payListener) {
        if (payListener == null) {
            return;
        }
        switch (wxPayStatusEntity.getCode()) {
            case 0:
                payListener.onPaySuccess(PayWay.WECHAT);
                break;
            case -1:
                payListener.onPayFailure(PayWay.WECHAT,null);
                break;
            case -2:
                payListener.onPayCancel(PayWay.WECHAT);
                break;
            default:
                payListener.onPayConfirm(PayWay.WECHAT);
                break;
        }
    }
}
