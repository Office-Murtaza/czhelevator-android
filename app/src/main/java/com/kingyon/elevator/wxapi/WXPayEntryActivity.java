package com.kingyon.elevator.wxapi;


import android.content.Intent;
import android.os.Bundle;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.paylibrary.PayListener;
import com.kingyon.paylibrary.entitys.PayWay;
import com.kingyon.paylibrary.wechatpay.WxConstants;
import com.kingyon.paylibrary.wechatpay.WxPayStatusEntity;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;

public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {
    private IWXAPI api;

    @Override
    public int getContentViewId() {
        return R.layout.activity_wx_pay;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        api = WXAPIFactory.createWXAPI(this, WxConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        LogUtils.e(resp.errCode,resp.getType());
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            EventBus.getDefault().post(new WxPayStatusEntity(resp.errCode));
        }

        finish();
    }
}