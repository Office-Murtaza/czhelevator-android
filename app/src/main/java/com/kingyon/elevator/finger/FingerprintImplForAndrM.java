package com.kingyon.elevator.finger;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;

import androidx.annotation.RequiresApi;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.finger.bean.VerificationDialogStyleBean;
import com.kingyon.elevator.finger.uitls.CipherHelper;
import com.kingyon.elevator.interfaces.FingerCheckListener;
import com.kingyon.elevator.utils.DialogUtils;

/**
 * Android M == 6.0
 * Created by ZuoHailong on 2019/7/9.
 */
@RequiresApi(api = Build.VERSION_CODES.M)
public class FingerprintImplForAndrM implements IFingerprint {

    private final String TAG = FingerprintImplForAndrM.class.getName();
    private Activity context;

    private static FingerprintImplForAndrM fingerprintImplForAndrM;
    //指纹验证框
    //指向调用者的指纹回调
    private FingerprintCallback fingerprintCallback;

    //用于取消扫描器的扫描动作
    private CancellationSignal cancellationSignal;
    //指纹加密
    private static FingerprintManagerCompat.CryptoObject cryptoObject;
    //Android 6.0 指纹管理
    private FingerprintManagerCompat fingerprintManagerCompat;

    @Override
    public void authenticate(Activity context, VerificationDialogStyleBean bean, FingerprintCallback callback) {

        this.context = context;
        this.fingerprintCallback = callback;
        //Android 6.0 指纹管理 实例化
        fingerprintManagerCompat = FingerprintManagerCompat.from(context);

        //取消扫描，每次取消后需要重新创建新示例
        cancellationSignal = new CancellationSignal();
        cancellationSignal.setOnCancelListener(() -> DialogUtils.getInstance().hideFingerCheckDailog());

        //调起指纹验证
        fingerprintManagerCompat.authenticate(cryptoObject, 0, cancellationSignal, authenticationCallback, null);
        DialogUtils.getInstance().showFingerCheckDailog(context, new FingerCheckListener() {
            @Override
            public void onUsepwd() {
                if (fingerprintCallback != null)
                    fingerprintCallback.onUsepwd();
            }

            @Override
            public void onCancle() {
                if (fingerprintCallback != null)
                    fingerprintCallback.onCancel();
            }

            @Override
            public void onDismiss() {
                if (cancellationSignal != null && !cancellationSignal.isCanceled())
                    cancellationSignal.cancel();
            }
        });
    }

    public static FingerprintImplForAndrM newInstance() {
        if (fingerprintImplForAndrM == null) {
            synchronized (FingerprintImplForAndrM.class) {
                if (fingerprintImplForAndrM == null) {
                    fingerprintImplForAndrM = new FingerprintImplForAndrM();
                }
            }
        }
        //指纹加密，提前进行Cipher初始化，防止指纹认证时还没有初始化完成
        try {
            cryptoObject = new FingerprintManagerCompat.CryptoObject(new CipherHelper().createCipher());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fingerprintImplForAndrM;
    }

    /**
     * 指纹验证结果回调
     */
    private FingerprintManagerCompat.AuthenticationCallback authenticationCallback = new FingerprintManagerCompat.AuthenticationCallback() {
        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
            super.onAuthenticationError(errMsgId, errString);
            LogUtils.d("指纹识别错误信息：" + errString);
            if (errMsgId != 5)//用户取消指纹验证
            {
                DialogUtils.getInstance().setFingerTips(errString.toString(), R.color.biometricprompt_color_FF5555);
                fingerprintCallback.tooManyAttempts();
            }
        }

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
            super.onAuthenticationHelp(helpMsgId, helpString);
            LogUtils.d("指纹识别错误帮主信息：" + helpString);
            DialogUtils.getInstance().setFingerTips(helpString.toString(), R.color.biometricprompt_color_FF5555);
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
            super.onAuthenticationSucceeded(result);
            DialogUtils.getInstance().setFingerTips(context.getString(R.string.biometricprompt_verify_success), R.color.biometricprompt_color_82C785);
            fingerprintCallback.onSucceeded();
            DialogUtils.getInstance().hideFingerCheckDailog();
        }

        @Override
        public void onAuthenticationFailed() {
            super.onAuthenticationFailed();
            DialogUtils.getInstance().setFingerTips(context.getString(R.string.biometricprompt_verify_failed), R.color.biometricprompt_color_FF5555);
            fingerprintCallback.onFailed();
        }
    };

    /*
     * 在 Android Q，Google 提供了 Api BiometricManager.canAuthenticate() 用来检测指纹识别硬件是否可用及是否添加指纹
     * 不过尚未开放，标记为"Stub"(存根)
     * 所以暂时还是需要使用 Andorid 6.0 的 Api 进行判断
     * */
    @Override
    public boolean canAuthenticate(Context context, FingerprintCallback fingerprintCallback) {
        /*
         * 硬件是否支持指纹识别
         * */
        if (!FingerprintManagerCompat.from(context).isHardwareDetected()) {
            fingerprintCallback.onHwUnavailable();
            return false;
        }
        //是否已添加指纹
        if (!FingerprintManagerCompat.from(context).hasEnrolledFingerprints()) {
            fingerprintCallback.onNoneEnrolled();
            return false;
        }
        return true;
    }

}
