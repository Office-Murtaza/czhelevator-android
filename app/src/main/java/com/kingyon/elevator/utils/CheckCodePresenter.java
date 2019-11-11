package com.kingyon.elevator.utils;

import android.os.Message;
import android.text.TextUtils;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.listeners.IWeakHandler;
import com.leo.afbaselibrary.mvp.presenters.BasePresenter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.WeakHandler;

/**
 * created by arvin on 16/12/12 10:20
 * email：1035407623@qq.com
 */
public class CheckCodePresenter extends BasePresenter implements IWeakHandler {
    private TextView tvGetCode;
    private WeakHandler mHandler;
    private String verifyCode;
    private int maxCount = 60;
    private BaseActivity baseActivity;

    public CheckCodePresenter(BaseActivity mActivity, TextView tvGetCode) {
        super(mActivity);
        this.baseActivity = mActivity;
        this.tvGetCode = tvGetCode;
        mHandler = new WeakHandler(this);
    }

    public void getCode(String phone, VerifyCodeType verifyCodeType) {
        if (TextUtils.isEmpty(phone)) {
            showToast("请输入手机号");
            return;
        }
//        if (phone.length() < 11) {
//            showToast("手机号格式不正确");
//            return;
//        }
        tvGetCode.setText(baseActivity.getString(R.string.wait));
        tvGetCode.setEnabled(false);

        NetService.getInstance().sendVerifyCode(phone, verifyCodeType)
                .compose(baseActivity.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        maxCount = 0;
                        mHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onNext(String s) {
                        showToast("验证码发送成功");
                        mHandler.sendEmptyMessage(0);
                    }
                });
    }

    @Override
    public void handleMessage(Message msg) {
        if (maxCount == 0) {
            tvGetCode.setText("获取验证码");
            tvGetCode.setEnabled(true);
            maxCount = 60;
            return;
        }

        tvGetCode.setText(maxCount-- + "s");
        mHandler.sendEmptyMessageDelayed(0, 1000);
    }

    @Override
    public void onDestroy() {
        mHandler.removeMessages(0);
        super.onDestroy();
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public enum VerifyCodeType {
        REGISTER("REGISTER"), PERFECTION("PERFECTION"), RESETPASSWORD("RESETPASSWORD"), UNBIND_OLD("UNBIND_OLD"), BIND_NEW("BIND_NEW");
        private String value;

        VerifyCodeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
