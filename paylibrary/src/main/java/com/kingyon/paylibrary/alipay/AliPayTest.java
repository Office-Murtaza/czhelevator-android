package com.kingyon.paylibrary.alipay;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.kingyon.paylibrary.alipay.test.OrderInfoUtil2_0;

import java.util.Map;

/**
 * Created by GongLi on 2018/7/26.
 * Email：lc824767150@163.com
 */

public class AliPayTest {

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2018070560526455";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088131557336624";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "2088131557336624";

    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCSd0vdORCp78yCFmNtsOPFt+XiZdz90RlUC6QJAEaapVvtfzVe72VeyXUUR2BOnXplB0mNIHsxoOBJU/e6ty/hvWfaAM+Q2PVtqLStTtHsDHtU21qKpFrlRrQYagNb4JC1Nzea9D4b5PHHr/+PL32KLjAmU+VHtnD4vzc5xjvuB/2WpS8rqAXjHpBzwdb86UFHasL7v2hFPVmqZNcEVZfmT27Ljvsj3BUCAI0jcTX5Ei24fVP3TJegA1lQHXiOFFJ/h0pGrqghCvi2w48TyesuF/j57INrLz/W+ajUsSEWCKEIX9m/IqY71IHCBs6L3MQwVW/u8X4KH++NktReVw7zAgMBAAECggEBAI3Yt0JCd978fEUoRZcADzd3CE4gdMvj6/G3bF/DQp2Ws9bosdmFY9lOpfLDxYOdg+HAEv14jZbcxRp/FZZgvyCSfLdmvZ78X4SaNJD7mtZOqErgPWm0Dsupk9VpJmOessat+U46aMQ/D+Fik9AqIBzo6nzUF2NCT1q4ZVWwpG27Gw2Cg9B/D9aWp5xG4ZytsRXTwgBGTPsliXTt+RGLMZ0AUYl4hr/nHY7CaP5QH+vrQkydO365fgaKPvu9vTXNfpDNWuFDhnggHg/YaFqrWfXIz5Vc1v5PnPRLrihp5PYysPPiZ1PaxgGL3r3kBriyyeVcbDKTQ/0ag9YVGWNecKECgYEA0h/3CqrXfGbuuccYtJw+jSwzBDOkembvNDolRTa9p4kXGIbrOMu78BJ4UWJoV+6DR+hhro/b88ez8xirqDTxUpNFBSYHBlwxDFsBQQc2B7CCENY0J4gU8ZTPN1MwcyserkFFbsV/6qvGH3ZFkN6TttG+YfQqqfbNpciaPytL3DkCgYEAsnFj4eAYnNKYsrmbHVh/eXh9pRCHGZWpXwy470JLfkQ4qFEBfpj1gV2Yg2Jj8Mvf1tE3DHEHBsKNii++9f9waEH6Hy/XzMQ0HABJofzpw+ma4vW+6GHKGbYwJRCArpAGzoXxMQ3wfTie4BlC+hL8S1BZ0j+73xXN0cPnBgUSXIsCgYBDGXIKNgQZ9cTGDo4VHs/axQKFlPgxHi3Ev8ynDZQCrAR0EKGMwYKCZ2OEmag4bDIABHUtCUNz1ZHhTz+Bt2ZYuC6SNlKH9Wi0SVtdKLBYREVEmyKMfSgy/RIu2KHgJSTnAHDrrqXqim/6HAqa+HizKMdjAFWfcPC6wBSBl5HiAQKBgHL694ZTNOjQFo6LZHTbUoB98FANP1BwDEvv0bZxvF1nLi7ey5y8iO1L1vtJyeYeKGSuOM9ugT2M3VEehOjUKKU1Zir0VBRglMc6NDgJ1MNlYmU+Pj5UzLoWmB057gENUyV+TzTxq3W0p0L1ZB/pXeJVqxr78F3GGfJBTjqlx/gTAoGAUjrhIwxIumvrdCvZP1eZYzj0RhOpiW7s3OMIb9iv1fuW2d9rhC6q/zC/2141u0I1kL4quX+SXv/8TL1L977zBparpEIDvs7ZEyzsh4Gwn2KuxFFB8Xkfp1N21KgWe6yEcY0XFFeU88OjKN4zQZrHuwvQomAiJSCwPq+OjYycNXY=";
    public static final String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCSd0vdORCp78yCFmNtsOPFt+XiZdz90RlUC6QJAEaapVvtfzVe72VeyXUUR2BOnXplB0mNIHsxoOBJU/e6ty/hvWfaAM+Q2PVtqLStTtHsDHtU21qKpFrlRrQYagNb4JC1Nzea9D4b5PHHr/+PL32KLjAmU+VHtnD4vzc5xjvuB/2WpS8rqAXjHpBzwdb86UFHasL7v2hFPVmqZNcEVZfmT27Ljvsj3BUCAI0jcTX5Ei24fVP3TJegA1lQHXiOFFJ/h0pGrqghCvi2w48TyesuF/j57INrLz/W+ajUsSEWCKEIX9m/IqY71IHCBs6L3MQwVW/u8X4KH++NktReVw7zAgMBAAECggEBAI3Yt0JCd978fEUoRZcADzd3CE4gdMvj6/G3bF/DQp2Ws9bosdmFY9lOpfLDxYOdg+HAEv14jZbcxRp/FZZgvyCSfLdmvZ78X4SaNJD7mtZOqErgPWm0Dsupk9VpJmOessat+U46aMQ/D+Fik9AqIBzo6nzUF2NCT1q4ZVWwpG27Gw2Cg9B/D9aWp5xG4ZytsRXTwgBGTPsliXTt+RGLMZ0AUYl4hr/nHY7CaP5QH+vrQkydO365fgaKPvu9vTXNfpDNWuFDhnggHg/YaFqrWfXIz5Vc1v5PnPRLrihp5PYysPPiZ1PaxgGL3r3kBriyyeVcbDKTQ/0ag9YVGWNecKECgYEA0h/3CqrXfGbuuccYtJw+jSwzBDOkembvNDolRTa9p4kXGIbrOMu78BJ4UWJoV+6DR+hhro/b88ez8xirqDTxUpNFBSYHBlwxDFsBQQc2B7CCENY0J4gU8ZTPN1MwcyserkFFbsV/6qvGH3ZFkN6TttG+YfQqqfbNpciaPytL3DkCgYEAsnFj4eAYnNKYsrmbHVh/eXh9pRCHGZWpXwy470JLfkQ4qFEBfpj1gV2Yg2Jj8Mvf1tE3DHEHBsKNii++9f9waEH6Hy/XzMQ0HABJofzpw+ma4vW+6GHKGbYwJRCArpAGzoXxMQ3wfTie4BlC+hL8S1BZ0j+73xXN0cPnBgUSXIsCgYBDGXIKNgQZ9cTGDo4VHs/axQKFlPgxHi3Ev8ynDZQCrAR0EKGMwYKCZ2OEmag4bDIABHUtCUNz1ZHhTz+Bt2ZYuC6SNlKH9Wi0SVtdKLBYREVEmyKMfSgy/RIu2KHgJSTnAHDrrqXqim/6HAqa+HizKMdjAFWfcPC6wBSBl5HiAQKBgHL694ZTNOjQFo6LZHTbUoB98FANP1BwDEvv0bZxvF1nLi7ey5y8iO1L1vtJyeYeKGSuOM9ugT2M3VEehOjUKKU1Zir0VBRglMc6NDgJ1MNlYmU+Pj5UzLoWmB057gENUyV+TzTxq3W0p0L1ZB/pXeJVqxr78F3GGfJBTjqlx/gTAoGAUjrhIwxIumvrdCvZP1eZYzj0RhOpiW7s3OMIb9iv1fuW2d9rhC6q/zC/2141u0I1kL4quX+SXv/8TL1L977zBparpEIDvs7ZEyzsh4Gwn2KuxFFB8Xkfp1N21KgWe6yEcY0XFFeU88OjKN4zQZrHuwvQomAiJSCwPq+OjYycNXY=";

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    /**
     * 支付宝支付业务
     */
    public static String getPayV2Params() {
        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        return orderParam + "&" + sign;
    }

}
