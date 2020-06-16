package com.czh.myversiontwo.activity;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.utils.QuickClickUtils;

import java.util.Objects;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_LOGIN;

/**
 * @Created By Admin  on 2020/4/27
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class ActivityUtils {

    public static void setLoginActivity(){
        if (QuickClickUtils.isFastClick()){
            ARouter.getInstance()
                    .build(ACTIVITY_MAIN2_LOGIN)
                    .navigation();
        }
    }

    public static void setActivity(String Url ){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .navigation();
        }
    }

    public static void setActivity(String Url,String key,String value ){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .withString(key,value)
                    .navigation();
            LogUtils.e();
        }
    }
    public static void setActivity(String Url,String key,int value ){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .withInt(key,value)
                    .navigation();
            LogUtils.e();
        }
    }


    public static void setActivity(String Url,String key1,String value1 ,String key2,String value2){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .withString(key1,value1)
                    .withString(key2,value2)
                    .navigation();
        }
    }

    public static void setActivity(String Url,String key1,String value1,String key2,String value2,String key3,String value3 ){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .withString(key1,value1)
                    .withString(key2,value2)
                    .withString(key3,value3)
                    .navigation();
        }
    }
    public static void setActivity(String Url,String key1,String value1,String key2,String value2,String key3,String value3 ,String key4,String value4){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .withString(key1,value1)
                    .withString(key2,value2)
                    .withString(key3,value3)
                    .withString(key4,value4)
                    .navigation();
        }
    }


    public static void setActivity(String Url,String key1,String value1,String key2,String value2,String key3,String value3 ,String key4,String value4,String key5,String value5){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .withString(key1,value1)
                    .withString(key2,value2)
                    .withString(key3,value3)
                    .withString(key4,value4)
                    .withString(key5,value5)
                    .navigation();
        }
    }

    public static void setActivity(String Url,String key1,String value1,String key2,String value2,String key3,String value3 ,String key4,String value4,String key5,String value5,String key6,String value6){
        if (QuickClickUtils.isFastClick()) {
            ARouter.getInstance()
                    .build(Url)
                    .withString(key1,value1)
                    .withString(key2,value2)
                    .withString(key3,value3)
                    .withString(key4,value4)
                    .withString(key5,value5)
                    .withString(key6,value6)
                    .navigation();
        }
    }
}
