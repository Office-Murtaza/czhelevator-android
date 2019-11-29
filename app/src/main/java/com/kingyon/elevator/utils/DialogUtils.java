package com.kingyon.elevator.utils;

import android.content.Context;

import com.kingyon.elevator.customview.InputPayPwdToCashDailog;
import com.kingyon.elevator.customview.SelectCashBindTypeDialog;
import com.kingyon.elevator.customview.UserPrivacyTipsDialog;
import com.kingyon.elevator.date.DatePickerListener;
import com.kingyon.elevator.date.SelectDateDialog;
import com.kingyon.elevator.interfaces.InputPayPwdListener;
import com.kingyon.elevator.interfaces.PrivacyTipsListener;
import com.kingyon.elevator.interfaces.SelectCashBindTypeListener;

/**
 * Date:2019/11/21
 * Time:13:45
 * author:songpeng
 * Email:1531603384@qq.com
 */
public class DialogUtils {

    private static volatile DialogUtils dialogUtils;


    private UserPrivacyTipsDialog userPrivacyTipsDialog;
    private SelectDateDialog selectDateDialog;
    private SelectCashBindTypeDialog selectCashBindTypeDialog;
    private InputPayPwdToCashDailog inputPayPwdToCashDailog;

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
    public void showUserPrivacyTipsDialog(Context context, String data, PrivacyTipsListener privacyTipsListener) {
        try {
            if (userPrivacyTipsDialog != null && userPrivacyTipsDialog.isShowing()) {
                userPrivacyTipsDialog.dismiss();
                userPrivacyTipsDialog = null;
            }
            userPrivacyTipsDialog = new UserPrivacyTipsDialog(context, data,privacyTipsListener);
            userPrivacyTipsDialog.setCancelable(true);
            userPrivacyTipsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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


    /**
     * 显示日期选择框
     *
     * @param context
     */
    public void showSelectDateDialog(Context context, DatePickerListener datePickerListener) {
        if (selectDateDialog != null && selectDateDialog.isShowing()) {
            selectDateDialog.dismiss();
            selectDateDialog = null;
        }
        selectDateDialog = new SelectDateDialog(context, datePickerListener);
        selectDateDialog.setCancelable(true);
        selectDateDialog.show();
    }

    public void hideSelectDateDialog() {
        try {
            if (selectDateDialog != null) {
                selectDateDialog.dismiss();
                selectDateDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示绑定账号类型选择框
     *
     * @param context
     */
    public void showSelectCashBindTypeDialog(Context context, SelectCashBindTypeListener selectCashBindTypeListener) {
        if (selectCashBindTypeDialog != null && selectCashBindTypeDialog.isShowing()) {
            selectCashBindTypeDialog.dismiss();
            selectCashBindTypeDialog = null;
        }
        selectCashBindTypeDialog = new SelectCashBindTypeDialog(context, selectCashBindTypeListener);
        selectCashBindTypeDialog.setCancelable(true);
        selectCashBindTypeDialog.show();
    }

    public void hideSelectCashBindTypeDialog() {
        try {
            if (selectCashBindTypeDialog != null) {
                selectCashBindTypeDialog.dismiss();
                selectCashBindTypeDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 弹出密码验证输入框
     *
     * @param context
     */
    public void showInputPayPwdToCashDailog(Context context, InputPayPwdListener inputPayPwdListener) {
        if (inputPayPwdToCashDailog != null && inputPayPwdToCashDailog.isShowing()) {
            inputPayPwdToCashDailog.dismiss();
            inputPayPwdToCashDailog = null;
        }
        inputPayPwdToCashDailog = new InputPayPwdToCashDailog(context, inputPayPwdListener);
        inputPayPwdToCashDailog.setCancelable(true);
        inputPayPwdToCashDailog.show();
    }

    public void hideInputPayPwdToCashDailog() {
        try {
            if (inputPayPwdToCashDailog != null) {
                inputPayPwdToCashDailog.dismiss();
                inputPayPwdToCashDailog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}