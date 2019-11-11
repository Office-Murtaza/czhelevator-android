package com.kingyon.elevator.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.tencent.mm.opensdk.utils.Log;


/**
 * 微信客户端回调activity示例
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("WXEntryActivity", "onCreate");
        api = WXAPIFactory.createWXAPI(this, "wxcb74db9ed572ffdc", false);
        api.registerApp("wxcb74db9ed572ffdc");
        handleIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.e("WXEntryActivity", "onNewIntent");
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq arg0) {
        // TODO Auto-generated method stub
        Log.e("WXEntryActivity", "onReq");
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.e("WXEntryActivity", "onResp");
        // TODO Auto-generated method stub
        if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
            // 用户同意
            Log.e("WXEntryActivity", "onResp" + resp.errCode);
            Log.e("WXEntryActivity", "onResp" + resp.errStr);
            Log.e("WXEntryActivity", "onResp" + resp.openId);
        }
    }
}

