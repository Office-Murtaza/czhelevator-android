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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.widget.EditText;

import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                httpGetToken(new GetToke() {
                    @Override
                    public boolean setToken(boolean is) {
                        return is;
                    }
                });
            }
        }).start();
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
                        if (ex.getCode()!=1003) {
                            getToke.setToken(false);
                            DataSharedPreferences.clearLoginInfo();
                        }
                    }
                    @Override
                    public void onNext(UserEntity userEntity) {
                        DataSharedPreferences.saveUserBean(userEntity);
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

    public static boolean checkAppInstalled(Context context,String pkgName) {
        if (pkgName== null || pkgName.isEmpty()) {
            return false;
        }
        PackageInfo packageInfo;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if(packageInfo == null) {
            return false;
        } else {
            return true;//true为安装了，false为未安装
        }
    }
    /**
     * 禁止EditText输入空格
     * @param editText
     */
    public static void setEditTextInhibitInputSpace(EditText editText){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if(source.equals(" "))return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter});
    }

    /**
     * 禁止EditText输入特殊字符
     * @param editText
     */
    public static void setEditTextInhibitInputSpeChat(EditText editText,int num){
        InputFilter filter=new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                String speChat="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                Pattern pattern = Pattern.compile(speChat);
                Matcher matcher = pattern.matcher(source.toString());
                if(matcher.find())return "";
                if(source.equals(" "))return "";
                else return null;
            }
        };
        editText.setFilters(new InputFilter[]{filter,new InputFilter.LengthFilter(num)});
    }
}
