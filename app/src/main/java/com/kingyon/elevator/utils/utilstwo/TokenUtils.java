package com.kingyon.elevator.utils.utilstwo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;

import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * @Created By Admin  on 2020/5/13
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TokenUtils {
    static boolean istoken = false;
    public static  boolean isToken(Context context){
        httpGetToken(new GetToke() {
            @Override
            public boolean setToken(boolean is) {
                return is;
            }
        });
        if (DataSharedPreferences.getToken().isEmpty()){
            ToastUtils.showToast(context,"未登录或登录过期请重新登录",1000);
            istoken = false;
        }else {
            istoken = true;
        }
    return istoken;
    }

    private static void httpGetToken(GetToke getToke) {
        NetService.getInstance().userProfile()
                .subscribe(new CustomApiCallback<UserEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
//                        if (ex.getCode()==100200) {
                            getToke.setToken(false);
                            DataSharedPreferences.clearLoginInfo();
//                        }
                    }
                    @Override
                    public void onNext(UserEntity userEntity) {
                        getToke.setToken(true);
                    }
                });
    }

    public static boolean isCreateAccount(String CreatateAccount){
        if (DataSharedPreferences.getCreatateAccount()!=null){
            if (DataSharedPreferences.getCreatateAccount().equals(CreatateAccount)) {
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }

    public static boolean isCertification(){
        UserEntity user = DataSharedPreferences.getUserBean();
        if (user.getAuthStatus()!=null) {
            if (user.getAuthStatus().equals(Constants.IDENTITY_STATUS.AUTHED)) {
                return false;
            } else {
                return true;
            }
        }else {
            return true;
        }
    }

    public static void isLogin(int eree){
        if (eree==100200){
            ActivityUtils.setLoginActivity();
        }
    }

    interface GetToke{
        boolean setToken(boolean is);
    }


}
