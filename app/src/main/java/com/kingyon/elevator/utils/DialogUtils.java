package com.kingyon.elevator.utils;

import android.content.Context;

import com.kingyon.elevator.customview.UserPrivacyTipsDialog;

/**
 * Date:2019/11/21
 * Time:13:45
 * author:songpeng
 * Email:1531603384@qq.com
 */
public class DialogUtils {

    private static volatile DialogUtils dialogUtils;


    private UserPrivacyTipsDialog userPrivacyTipsDialog;

    private DialogUtils() {

    }


    public static DialogUtils getInstance() {
        if (dialogUtils == null) {
            synchronized (DialogUtils.class) {
                if (dialogUtils == null) {
                    dialogUtils = new DialogUtils();
                }
            }
        }
        return dialogUtils;
    }


    /**
     * 显示用户协议与隐私政策对话框
     *
     * @param context
     */
    public void showUserPrivacyTipsDialog(Context context,String data) {
        if (userPrivacyTipsDialog != null && userPrivacyTipsDialog.isShowing()) {
            userPrivacyTipsDialog.dismiss();
            userPrivacyTipsDialog = null;
        }
        userPrivacyTipsDialog = new UserPrivacyTipsDialog(context,data);
        userPrivacyTipsDialog.setCancelable(true);
        userPrivacyTipsDialog.show();
    }

    public void hideUserPrivacyTipsDialog() {
        try {
            if (userPrivacyTipsDialog != null) {
                userPrivacyTipsDialog.dismiss();
                userPrivacyTipsDialog = null;
            }
        } catch (Exception e) {

        }
    }

}
