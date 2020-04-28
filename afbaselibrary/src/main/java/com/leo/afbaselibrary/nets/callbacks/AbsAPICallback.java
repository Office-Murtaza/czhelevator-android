package com.leo.afbaselibrary.nets.callbacks;

import android.content.Context;
import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.widget.Toast;

import com.google.gson.JsonParseException;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.NoNetworkException;
import com.leo.afbaselibrary.nets.exceptions.PayApiException;
import com.leo.afbaselibrary.nets.exceptions.PayResultException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.orhanobut.logger.Logger;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * created by arvin on 16/10/24 17:20
 * email：1035407623@qq.com
 */
public abstract class AbsAPICallback<T> extends Subscriber<T> {

    //出错提示
    public final String networkMsg = "访问失败，请稍后重试";
    public final String parseMsg = "数据解析出错";
    public final String net_connection = "网络连接错误";
    public final String socketTimeOut = "连接超时";
    public final String unknownMsg = "未知错误";
    public final String noNetwork = "请检查您的网络是否已连接";
    public final String litePal = "本地数据库异常";

    protected AbsAPICallback() {
    }

    @Override
    public void onError(Throwable e) {
        e = getThrowable(e);
        if (e instanceof HttpException) {//HTTP错误
            error(e, ((HttpException) e).code(), networkMsg);
        } else if (e instanceof ResultException) {//服务器返回的错误
            ResultException resultException = (ResultException) e;
            error(e, resultException.getErrCode(), resultException.getMessage());
            resultError(resultException);//处理错误
        } else if (e instanceof JsonParseException || e instanceof JSONException || e instanceof ParseException) {//解析错误
            error(e, ApiException.PARSE_ERROR, parseMsg);
        } else if (e instanceof ConnectException) {
            error(e, ApiException.PARSE_ERROR, net_connection);
        } else if (e instanceof SocketTimeoutException) {
            error(e, ApiException.UNKNOWN, socketTimeOut);
        } else if (e instanceof NoNetworkException) {
            error(e, ApiException.NET_NOT_AVAILABLE, noNetwork);
//            error(e, ApiException.NET_NOT_AVAILABLE, null);
        } else {//未知错误
            error(e, ApiException.UNKNOWN, unknownMsg);
        }
    }

    private Throwable getThrowable(Throwable e) {
        Throwable throwable = e;
        while (throwable.getCause() != null) {
            e = throwable;
            throwable = throwable.getCause();
        }
        return e;
    }

    protected void resultError(ResultException e) {
        if (e.getErrCode() == ApiException.RE_LOGIN || e.getErrCode() == ApiException.NO_LOGIN) {
            try {
                Context currentActivity = ActivityUtil.getCurrentActivity();
                if (currentActivity != null) {
                    Intent intent = new Intent(currentActivity, Class.forName("com.kingyon.elevator.uis.activities.password.LoginActivity"));
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("value_1", true);
                    intent.putExtras(bundle);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    currentActivity.startActivity(intent);//只释放这一行
//                    Toast.makeText(currentActivity, "请重新登录", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
//        if (e.getErrCode() == ApiException.RE_LOGIN) {
//            try {
////                Context currentActivity = ActivityUtil.getCurrentActivity();
////                if (currentActivity != null) {
////                    Intent intent = new Intent(currentActivity, Class.forName("com.kingyon.guangwei.uis.activities.LoginActivity"));
////                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
////                    currentActivity.startActivity(intent);
////                    Toast.makeText(currentActivity, "请重新登录", Toast.LENGTH_SHORT).show();
////                }
//            } catch (Exception e1) {
//                e1.printStackTrace();
//            }
//        }
    }

    /**
     * 错误信息回调
     */
    private void error(Throwable e, int errorCode, String errorMsg) {
        try {
            ApiException ex;
            if (e instanceof PayResultException) {
                ex = new PayApiException(e, errorCode, ((PayResultException) e).getPayEntity());
            } else {
                ex = new ApiException(e, errorCode);
            }
            Logger.d(ex);
            ex.printStackTrace();
            ex.setDisplayMessage(errorMsg);
            onResultError(ex);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * 服务器返回的错误
     */
    protected abstract void onResultError(ApiException ex);



    @Override
    public void onCompleted() {
    }

}
