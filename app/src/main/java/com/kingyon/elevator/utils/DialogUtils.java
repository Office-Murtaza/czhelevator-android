package com.kingyon.elevator.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.annotation.ColorRes;


import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.customview.EditSuccessTipsDialog;
import com.kingyon.elevator.customview.FingerCheckDailog;
import com.kingyon.elevator.customview.InputPayPwdToCashDailog;
import com.kingyon.elevator.customview.MainTextDialog;
import com.kingyon.elevator.customview.MainWindowNoticeDialog;
import com.kingyon.elevator.customview.OrderDetailedTipsDialog;
import com.kingyon.elevator.customview.PlanSelectDateDialog;
import com.kingyon.elevator.customview.PreviewVideoBackTipsDialog;
import com.kingyon.elevator.customview.RuleDescTipsDialog;
import com.kingyon.elevator.customview.SelectCashBindTypeDialog;
import com.kingyon.elevator.customview.UserPrivacyTipsDialog;
import com.kingyon.elevator.date.DatePickerListener;
import com.kingyon.elevator.date.SelectDateDialog;
import com.kingyon.elevator.entities.AdNoticeWindowEntity;
import com.kingyon.elevator.entities.GoPlaceAnOrderEntity;
import com.kingyon.elevator.interfaces.FingerCheckListener;
import com.kingyon.elevator.interfaces.InputPayPwdListener;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.interfaces.PlanSelectDateLinsener;
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
    private FingerCheckDailog fingerCheckDailog;
    private EditSuccessTipsDialog editSuccessTipsDialog;
    private PlanSelectDateDialog planSelectDateDialog;
    private RuleDescTipsDialog ruleDescTipsDialog;
    private PreviewVideoBackTipsDialog previewVideoBackTipsDialog;
    private OrderDetailedTipsDialog orderDetailedTipsDialog;
    private MainWindowNoticeDialog mainWindowNoticeDialog;
    private MainTextDialog mainTextDialog;

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
            userPrivacyTipsDialog = new UserPrivacyTipsDialog(context, data, privacyTipsListener);
            userPrivacyTipsDialog.setCancelable(false);
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
     * 显示广告上传规则说明对话框
     *
     * @param context
     */
    public void showRuleDescTipsDialog(Context context) {
        try {
            if (ruleDescTipsDialog != null && ruleDescTipsDialog.isShowing()) {
                ruleDescTipsDialog.dismiss();
                ruleDescTipsDialog = null;
            }
            ruleDescTipsDialog = new RuleDescTipsDialog(context);
            ruleDescTipsDialog.setCancelable(true);
            ruleDescTipsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideRuleDescTipsDialog() {
        try {
            if (ruleDescTipsDialog != null) {
                ruleDescTipsDialog.dismiss();
                ruleDescTipsDialog = null;
            }
        } catch (Exception e) {

        }
    }

    /**
     * 显示订单明细数据对话框
     *
     * @param context
     */
    public void showOrderDetailedTipsDialog(Context context, GoPlaceAnOrderEntity goPlaceAnOrderEntity, double allPrice, double realMoney, double zhekouPrice, double couponsPrice) {
        try {
            if (orderDetailedTipsDialog != null && orderDetailedTipsDialog.isShowing()) {
                orderDetailedTipsDialog.dismiss();
                orderDetailedTipsDialog = null;
            }
            orderDetailedTipsDialog = new OrderDetailedTipsDialog(context, goPlaceAnOrderEntity, allPrice, realMoney, zhekouPrice, couponsPrice);
            orderDetailedTipsDialog.setCancelable(true);
            orderDetailedTipsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideOrderDetailedTipsDialog() {
        try {
            if (orderDetailedTipsDialog != null) {
                orderDetailedTipsDialog.dismiss();
                orderDetailedTipsDialog = null;
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
    public void showInputPayPwdToCashDailog(Context context,String type, InputPayPwdListener inputPayPwdListener) {
        if (inputPayPwdToCashDailog != null && inputPayPwdToCashDailog.isShowing()) {
            inputPayPwdToCashDailog.dismiss();
            inputPayPwdToCashDailog = null;
        }
        inputPayPwdToCashDailog = new InputPayPwdToCashDailog(context,type, inputPayPwdListener);
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

    /**
     * 指纹识别验证框
     *
     * @param context
     */
    public void showFingerCheckDailog(Context context,String type, FingerCheckListener fingerCheckListener) {
        if (fingerCheckDailog != null && fingerCheckDailog.isShowing()) {
            fingerCheckDailog.dismiss();
            fingerCheckDailog = null;
        }
        LogUtils.e(type);
        fingerCheckDailog = new FingerCheckDailog(context,type, fingerCheckListener);
        fingerCheckDailog.setCancelable(false);
        fingerCheckDailog.show();
    }

    public void hideFingerCheckDailog() {
        try {
            if (fingerCheckDailog != null) {
                fingerCheckDailog.dismiss();
                fingerCheckDailog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFingerTips(String tip, @ColorRes int colorId) {
        try {
            if (fingerCheckDailog != null) {
                fingerCheckDailog.setTip(tip, colorId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示修改成功对话框
     *
     * @param context
     */
    public void showEditSuccessTipsDialog(Context context, OnItemClick onItemClick) {
        if (editSuccessTipsDialog != null && editSuccessTipsDialog.isShowing()) {
            editSuccessTipsDialog.dismiss();
            editSuccessTipsDialog = null;
        }
        editSuccessTipsDialog = new EditSuccessTipsDialog(context, onItemClick);
        editSuccessTipsDialog.setCancelable(false);
        editSuccessTipsDialog.show();
    }

    public void hideEditSuccessTipsDialog() {
        try {
            if (editSuccessTipsDialog != null) {
                editSuccessTipsDialog.dismiss();
                editSuccessTipsDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 显示计划单选择日期对话框
     *
     * @param context
     */
    public void showPlanSelectDateDialog(Context context, PlanSelectDateLinsener planSelectDateLinsener) {
        if (planSelectDateDialog != null && planSelectDateDialog.isShowing()) {
            planSelectDateDialog.dismiss();
            planSelectDateDialog = null;
        }
        planSelectDateDialog = new PlanSelectDateDialog(context, planSelectDateLinsener);
        planSelectDateDialog.setCancelable(false);
        planSelectDateDialog.show();
    }

    public void hidePlanSelectDateDialog() {
        try {
            if (planSelectDateDialog != null) {
                planSelectDateDialog.dismiss();
                planSelectDateDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 显示视频预览界面返回提示对话框
     *
     * @param context
     */
    public void showBackTipsDialog(Context context, OnItemClick onItemClick) {
        try {
            if (previewVideoBackTipsDialog != null && previewVideoBackTipsDialog.isShowing()) {
                previewVideoBackTipsDialog.dismiss();
                previewVideoBackTipsDialog = null;
            }
            previewVideoBackTipsDialog = new PreviewVideoBackTipsDialog(context, onItemClick);
            previewVideoBackTipsDialog.setCancelable(true);
            previewVideoBackTipsDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideBackTipsDialog() {
        try {
            if (previewVideoBackTipsDialog != null) {
                previewVideoBackTipsDialog.dismiss();
                previewVideoBackTipsDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 显示合伙人信息提示框
     *
     * @param context
     */
    public void showMainWindowNoticeDialog(Context context, AdNoticeWindowEntity adNoticeWindowEntity) {
        try {
            if (mainWindowNoticeDialog != null && mainWindowNoticeDialog.isShowing()) {
                mainWindowNoticeDialog.dismiss();
                mainWindowNoticeDialog = null;
            }
            mainWindowNoticeDialog = new MainWindowNoticeDialog(context, adNoticeWindowEntity);
            mainWindowNoticeDialog.setCancelable(false);
            mainWindowNoticeDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public void hideMainWindowNoticeDialog() {
        LogUtils.e("1322");
        try {
            if (mainWindowNoticeDialog != null) {
                mainWindowNoticeDialog.dismiss();
                mainWindowNoticeDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e("e",e.toString());
        }
    }


    /**
     * 2.0文字广告
     *
     * */
    public void showMainText(Context context, AdNoticeWindowEntity adNoticeWindowEntity){
        try {
            if (mainTextDialog != null && mainTextDialog.isShowing()) {
                mainTextDialog.dismiss();
                mainTextDialog = null;
            }
            mainTextDialog = new MainTextDialog(context, adNoticeWindowEntity);
            mainTextDialog.setCancelable(false);
            mainTextDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideMainText() {
        try {
            if (mainTextDialog != null) {
                mainTextDialog.dismiss();
                mainTextDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected ProgressDialog progressDialog;

    public void showProgressDialogView(Context context, String message, Boolean isCancel) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(context);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setIndeterminate(false);
            progressDialog.setCancelable(isCancel);
        }
        progressDialog.setMessage(message != null ? message : "");
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgressDialogView() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }
}
