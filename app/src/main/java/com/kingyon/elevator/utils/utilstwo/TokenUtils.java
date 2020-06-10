package com.kingyon.elevator.utils.utilstwo;

import android.content.Context;

import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.leo.afbaselibrary.utils.ToastUtils;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_LOGIN;

/**
 * @Created By Admin  on 2020/5/13
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TokenUtils {

    public static  boolean isToken(Context context){
        if (DataSharedPreferences.getToken().isEmpty()){
            ToastUtils.showToast(context,"你还没有登录请登录后操作",1000);

            ARouter.getInstance().build(ACTIVITY_MAIN2_LOGIN).navigation();
            return false;
        }else {
            return true;
        }
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
            ARouter.getInstance().build(ACTIVITY_MAIN2_LOGIN).navigation();
        }
    }

}
