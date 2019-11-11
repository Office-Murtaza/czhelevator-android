package com.kingyon.elevator.others.auth;


import android.util.Base64;
import android.util.Log;


/**
 * Created by Administrator on 2019/1/10.
 */

public class AuthUtills {

    private static String appid = "1000";

    private static String secretid = "HYLrmGvcedOoDhICM3uQtgmccInGWxwIui6T";

    private static String secretkey = "o6QkjSswrVRzRgxArOaHmNqVHezeEcEo";

    private static String parmFormat = "a=%s&k=%s&t=%s&r=%s";


    public static String getAuth() {
        int rdm = (int) ((Math.random() * 9 + 1) * 10000);
        long time = System.currentTimeMillis() / 1000;
        String srcStr = String.format(parmFormat, appid, secretid, time + "", rdm + "");
        Log.i("DREAM", srcStr);
        String sign = null;
        try {
            byte[] bytes1 = HMAC_SHA1.HmacSHA1Encrypt(srcStr, secretkey);
            sign = Base64.encodeToString(byteMerger(bytes1, srcStr.getBytes()), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("DREAM", sign);
        return sign;
    }


    public static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }
}
