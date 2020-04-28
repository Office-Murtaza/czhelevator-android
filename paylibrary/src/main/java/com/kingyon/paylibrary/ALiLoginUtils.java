package com.kingyon.paylibrary;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.AuthTask;
import com.kingyon.paylibrary.alipay.test.AuthResult;
import com.kingyon.paylibrary.alipay.test.OrderInfoUtil2_0;

import java.util.Map;

/**
 * @Created By Admin  on 2020/4/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public abstract class ALiLoginUtils {

    public Activity activity;

    /** 支付宝支付业务：入参app_id */
    public static final String APPID = "2018122462671611";
    /** 支付宝账户登录授权业务：入参pid值 */
    public static final String PID = "2088721030369801";
    /** 支付宝账户登录授权业务：入参target_id值 可自定义，保证唯一性即可*/
    public static final String TARGET_ID = "123456789";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /** 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1 */
    public static final String RSA2_PRIVATE = "MIIEowIBAAKCAQEAltbqNxCnpP4CE1Ps4FK1CqKA/SyGHbMgC6/TtzLgw9uELyTo3exXaFfOUqvxTFnoJLFKXSIAQ4i3WnNcRn/qfV/QGwcKNPRzHgr6sLRofpYXS37mCGHEM1dQxKYIwQuPWNkYDC8NMSXoiftXEFEOEre4/yFfshPi7l8R9aJUoBo4u91OdEbeT83GDmVRQBUThqGyk6GdZU8XaOg6966zuP8ojFFZyPqcsRYFJFv96bdg+eJX+D6hmNvH676WRfV0UagLbGd5Cz9GUqIilXCeN8Ji5z7nCFAV+vDCAhzo4bhuhYq3l8zOExoXha5IFm7IeqGVDdeBt/DJvwa/OTC9gQIDAQABAoIBAFPVToccMpB4vB6R3m0uioBGoPoU7NVE4OKd7kU7ecBhGK47DVGKMWpEot8/wPR2Y5dBEduBMjRWn9EdnINyRJk5zye2We3kjrZ706iyHAF+2aOT+7Vc4PbH6JPi06q3CgyPW9YpsGNIdl1524GuCuxMP/1MRytp8g4ZuJmUhwQTs7y96ZLdG158TEUuGMand9vtMWfVEPk2+odRYOqCWjk/qjYReVAuH4zNrrzmD93P8bq6+fLHd1AUHred3+X6RBF1NM9sIEmW+NZvS35Io7eNL9yjkABVtx+pZlCMvxNKq4GqVkzSnsc/w861W69a0abDYFoC4sxAfxM6IkCAL6ECgYEA05qlNTZfl828w9He5oD3Hm6idsuzoYYDujQbuiL44XcAjOCCnuwfuE8W0apBs1TR8phI7G+Ud2mQyeX9fItgdl82QCCoY9yhIH7hcurdsP9ahpNP8vpc3+Rry7J/BV2BwmpecRFZcxyS3PfAyZUT0pCvaX5zHZh+ol17yZcwK2sCgYEAtnyTbCux/CazUNS2yUlSCKMCkdQkPKhOF8c96SkG+rc2wRPp1eVnzEwWgqA/MuIKYBS0UdqiHBM2MmZDYY83TdGnyK1LCG9InCVy96dIoxy8X5qy3uLItS28wNNj+lgenaAbofSl/YBLNqEXwoGln16xhAXlLC7w+lE6hFFMwcMCgYEAoJ8rtre+WknGdtIRI+FTM9uBITHA+ePzYl15ipJPXz3owgcNv7q23T3TsXVOh+TjtQVZnXeZOAd+63LUNrzJjTD6yjS//YWVHUiSs5uKinda7PpXXrYi16TZGnc0qgrvm3cIl0U3JATVijvaMGoj8Nr4+9gLY7TEs1TIyR4lZ18CgYBVvKvq65Cg/nHLLVV/trITGrtPVpN76Dtgkpy02jDMRJ42EqwPccvVwX3l+3IV7vue7rtVpdn6RNB213Ma+BWsuy8QatLQ8K/1pOaiEndfI1YhuOYHMBfKf+Wp1uenLBWeSyYQbog6VTeBQCiQmyb8fJn8hGSK4LcZdGaA2k2szwKBgA/fxWEqYsd2NyFr8FwPgPHQmb0TSca361EGYzAxLEYox/m+ykbL3VmrsL/ZmCS641kdf0FoIDvnOk0ltEcTNSjSx7qFqFYq15ZxQFDqEqNmP5fs6SEjMQtYDGkVmhxdybe7zbjIyGKs1BMfmXEKM6jOCgHvYSwB90tTBytZ0DlT";

    public static final String RSA_PRIVATE = "";

    private static final int SDK_AUTH_FLAG = 2;

    public   String opentid = "";

    public ALiLoginUtils(Activity activity){
        this.activity = activity;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();
                    String resultCode = authResult.getResultCode();
                    Log.e("ALiLoginActivity", "=====resultStatus=====" + resultStatus);
                    Log.e("ALiLoginActivity", "=====resultCode=====" + resultCode);

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Log.e("ALiLoginActivity", "授权成功\n" + String.format("authCode:%s", authResult.getAuthCode()+"==="+authResult.toString()));
                        opentid = authResult.getAlipayOpenId();
                        getOpentid(authResult.getAlipayOpenId());
                        Toast.makeText(activity, "授权成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他状态值则为授权失败
                        Log.e("ALiLoginActivity", "授权失败\n" + String.format("authCode:%s", authResult.getAuthCode()));
//                        Toast.makeText(activity,"授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();
                        Toast.makeText(activity, "授权失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        };
    };




    /**
     * 支付宝账户授权业务
     */
    public void authV2() {
        if (TextUtils.isEmpty(PID) || TextUtils.isEmpty(APPID)
                || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))
                || TextUtils.isEmpty(TARGET_ID)) {
            new AlertDialog.Builder(activity).setTitle("警告").setMessage("需要配置PARTNER |APP_ID| RSA_PRIVATE| TARGET_ID")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * authInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> authInfoMap = OrderInfoUtil2_0.buildAuthInfoMap(PID, APPID, TARGET_ID, rsa2);
        String info = OrderInfoUtil2_0.buildOrderParam(authInfoMap);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(authInfoMap, privateKey, rsa2);
        final String authInfo = info + "&" + sign;
        Log.e("ALiLoginActivity", "=====authInfo=====" + authInfo);
        Runnable authRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造AuthTask 对象
                AuthTask authTask = new AuthTask(activity);
                // 调用授权接口，获取授权结果
                Map<String, String> result = authTask.authV2(authInfo, true);

                Message msg = new Message();
                msg.what = SDK_AUTH_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread authThread = new Thread(authRunnable);
        authThread.start();
    }

    public String getOpentid(){
        return opentid;
    }
    protected abstract void getOpentid(String s);
}
