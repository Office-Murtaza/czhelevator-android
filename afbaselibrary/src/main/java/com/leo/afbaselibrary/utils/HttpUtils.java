package com.leo.afbaselibrary.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by zhengliao on 2017/5/22 0022.
 * Email:dantemustcry@126.com
 */

public class HttpUtils {
    public enum NET_TYPE {
        NET_NO, NET_WIFI, NET_CMWAP, NET_CMNET
    }

    public static boolean isNetWork(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                context.getSystemService
                        (Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if (info != null) {
            return true;
        }
        return false;
    }

    public static NET_TYPE isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return NET_TYPE.NET_NO;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        NetworkInfo netWorkInfo = info[i];
                        if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                            return NET_TYPE.NET_WIFI;
                        } else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                            String extraInfo = netWorkInfo.getExtraInfo();
                            if ("cmwap".equalsIgnoreCase(extraInfo) || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
                                return NET_TYPE.NET_CMWAP;
                            }
                            return NET_TYPE.NET_CMNET;
                        }
                    }
                }
            }
        }
        return NET_TYPE.NET_NO;
    }
}
