package com.kingyon.elevator.utils.utilstwo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.entities.entities.CodeEntity;
import com.kingyon.elevator.entities.entities.FingerprintEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.login.CodeActivity;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.uis.actiivty2.login.UserLoginActiivty;
import com.kingyon.elevator.uis.actiivty2.main.CommunityReleasetyActivity;
import com.kingyon.elevator.uis.dialogs.InvoiceSuccessDialog;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.JumpUtils;
import com.kingyon.elevator.utils.KeyBoardUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ActivityUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import static android.app.Activity.RESULT_OK;
import static com.czh.myversiontwo.activity.ActivityUtils.setActivity;
import static com.czh.myversiontwo.utils.CodeType.QQ;
import static com.czh.myversiontwo.utils.CodeType.WX;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CODE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_PASSSWORD_SETTING;
import static com.kingyon.elevator.constants.Constants.LoginType.ALI;
import static com.kingyon.elevator.data.DataSharedPreferences.SAVE_MICRO_ARTICLE_DRAFT;
import static com.kingyon.elevator.data.DataSharedPreferences.SAVE_MICRO_COMMUNITY_DRAFT;
import static com.kingyon.elevator.data.DataSharedPreferences.SAVE_MICRO_VIDEO_DRAFT;
import static com.kingyon.elevator.photopicker.UtilsHelper.getString;

/**
 * @Created By Admin  on 2020/4/26
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrdinaryActivity {

    public static LoginActiivty loginActiivty =null;
    public static CodeActivity codeActivity =null;
    public static UserLoginActiivty userLoginActiivty =null;
    public static CommunityReleasetyActivity communityReleasetyActivity = null;


    /**
     * 验证码跳转
     * */
    public static void CodeActivity(BaseActivity activity, String type, String phone,String unique, String avatar, String nickName ,String isbinding,String loginType) {
        activity.showProgressDialog(activity.getString(R.string.wait),true);
            NetService.getInstance().setSendCheckConde(type, phone)
                    .compose(activity.bindLifeCycle())
                    .subscribe(new CustomApiCallback<String>() {
                        @Override
                        protected void onResultError(ApiException ex) {
                            activity.hideProgress();
                            LogUtils.e(ex);
                            ToastUtils.showToast(activity, ex.getDisplayMessage(), 1000);
                        }

                        @Override
                        public void onNext(String s) {
                            ToastUtils.showToast(activity, "发送成功", 1000);
                            activity.hideProgress();
                            setActivity(ACTIVITY_MAIN2_CODE, "phone", phone, "unique", unique, "avatar", avatar, "nickName", nickName,"isbinding",isbinding,"loginType",loginType);
                        }
                    });
    }

    /**
     * 验证码发送
     * */
    public static void CodeTextviewActivity(BaseActivity activity, String type, String phone, TextView textView){
        activity.showProgressDialog(activity.getString(R.string.wait),true);
        NetService.getInstance().setSendCheckConde(type,phone)
                .compose(activity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        activity.hideProgress();
                        LogUtils.e(ex);
                        ToastUtils.showToast(activity,ex.getDisplayMessage(),1000);
                    }
                    @Override
                    public void onNext(String s) {
                        ToastUtils.showToast(activity,"发送成功",1000);
                        activity.hideProgress();
                        CountDownTimerUtils mCountDownTimerUtils = new CountDownTimerUtils(textView, 60000, 1000);
                        mCountDownTimerUtils.start();

                    }
                });
    }

    /**
     * 验证码验证
     * */
    public static void httpCheckVerifyCode(BaseActivity activity, String phone, String verifyCode , String unique,String avatar, String nickName,String loginType){
        activity.showProgressDialog("请稍等...",true);
        NetService.getInstance().setBindPhone(phone,verifyCode,unique,avatar,nickName,loginType)
                .compose(activity.bindLifeCycle())
                .subscribe(new CustomApiCallback<CodeEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex);
                        ToastUtils.showToast(activity,ex.getDisplayMessage(),1000);
                        activity.hideProgress();
                    }
                    @Override
                    public void onNext(CodeEntity codeEntity) {
                        activity.hideProgress();
                        if (codeEntity==null) {
                           ActivityUtils.setActivity(ACTIVITY_MAIN2_PASSSWORD_SETTING,"phone",phone);
                        }else {
                            LogUtils.e(codeEntity.getToken(), codeEntity.getUser(), codeEntity.isNeedFill());
                            UserEntity userEntity = codeEntity.getUser();
                            DataSharedPreferences.saveLoginName(phone);
                            DataSharedPreferences.saveUserBean(userEntity);
                            DataSharedPreferences.saveToken(codeEntity.getToken());
                            Net.getInstance().setToken(DataSharedPreferences.getToken());
                            JumpUtils.getInstance().jumpToRoleMain(activity, AppContent.getInstance().getMyUserRole());
                            loginActiivty.finish();
                            activity.finish();
                        }
                    }
                });
    }

    /**
     * 登录
     * */
    public static void httpLogin(BaseActivity baseActivity, String phone, String password, String way,
                                 String unique, String avatar, String nickName, LinearLayout llSf,
                                 TextView tvLoginUser,TextView tvCode) {
        baseActivity.showProgressDialog(baseActivity.getString(R.string.wait),true);
        NetService.getInstance().setLogin(phone,password,way,unique,avatar,nickName)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<CodeEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        baseActivity.hideProgress();
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                        LogUtils.e(ex.getCode());
                        if (ex.getCode()==100106){
                            if (way.equals(WX)) {
                                ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                if (llSf != null) {
                                    llSf.setVisibility(View.GONE);
                                    tvLoginUser.setVisibility(View.GONE);
                                }
                            } else if (way.equals(QQ)) {
                                ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                if (llSf != null) {
                                    llSf.setVisibility(View.GONE);
                                    tvLoginUser.setVisibility(View.GONE);
                                }
                            } else if (way.equals(ALI)) {
                                ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                if (llSf != null) {
                                    llSf.setVisibility(View.GONE);
                                    tvLoginUser.setVisibility(View.GONE);
                                }
                            }
                        }else if (ex.getCode()==100107){
                            baseActivity.showToast("注册成功，请设置登录密码");
                            ActivityUtils.setActivity(ACTIVITY_MAIN2_PASSSWORD_SETTING, "phone", phone);
                        }else if (ex.getCode()==100102&&way.equals("NOR")){
                            tvCode.setVisibility(View.VISIBLE);
                            tvCode.setText("账号或密码错误，请重新输入");
                        }
                    }
                    @Override
                    public void onNext(CodeEntity codeEntity) {
                        baseActivity.hideProgress();
                        ToastUtils.showToast(baseActivity, "成功", 1000);
                        if (codeEntity != null) {
                            if (codeEntity.isNeedSetPwd()) {
                                UserEntity userEntity = codeEntity.getUser();
                                baseActivity.showToast("请设置登录密码");
                                ActivityUtils.setActivity(ACTIVITY_MAIN2_PASSSWORD_SETTING, "phone", userEntity.getPhone());
                            } else if (codeEntity.isNeedFill()) {
                                if (way.equals(WX)) {
                                    ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                    if (llSf != null) {
                                        llSf.setVisibility(View.GONE);
                                        tvLoginUser.setVisibility(View.GONE);
                                    }
                                } else if (way.equals(QQ)) {
                                    ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                    if (llSf != null) {
                                        llSf.setVisibility(View.GONE);
                                        tvLoginUser.setVisibility(View.GONE);
                                    }
                                } else if (way.equals(ALI)) {
                                    ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                    if (llSf != null) {
                                        llSf.setVisibility(View.GONE);
                                        tvLoginUser.setVisibility(View.GONE);
                                    }
                                } else {
                                    baseActivity.showToast("注册成功，请设置登录密码");
                                    ActivityUtils.setActivity(ACTIVITY_MAIN2_PASSSWORD_SETTING, "phone", phone);
                                }
                            } else {

                                UserEntity userEntity = codeEntity.getUser();
                                LogUtils.e(codeEntity.getToken(), codeEntity.getUser(), codeEntity.isNeedFill(),userEntity.getAccount());
                                DataSharedPreferences.saveLoginName(phone);
                                DataSharedPreferences.saveUserBean(userEntity);
                                DataSharedPreferences.saveCreatateAccount(userEntity.getAccount());
                                DataSharedPreferences.saveToken(codeEntity.getToken());
                                Net.getInstance().setToken(DataSharedPreferences.getToken());
                                JumpUtils.getInstance().jumpToRoleMain(baseActivity, AppContent.getInstance().getMyUserRole());

                                FingerprintEntiy entiy  = new FingerprintEntiy();
                                entiy.setIsFin("1");
                                entiy.setUserId(DataSharedPreferences.getCreatateAccount());
                                entiy.save();

                                loginActiivty.finish();
                                baseActivity.finish();
                            }
                        }else {
                            if (way.equals(WX)) {
                                ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                if (llSf != null) {
                                    llSf.setVisibility(View.GONE);
                                    tvLoginUser.setVisibility(View.GONE);
                                }
                            } else if (way.equals(QQ)) {
                                ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                if (llSf != null) {
                                    llSf.setVisibility(View.GONE);
                                    tvLoginUser.setVisibility(View.GONE);
                                }
                            } else if (way.equals(ALI)) {
                                ToastUtils.showToast(baseActivity, "请绑定手机号", 1000);
                                if (llSf != null) {
                                    llSf.setVisibility(View.GONE);
                                    tvLoginUser.setVisibility(View.GONE);
                                }
                            } else {
                                baseActivity.showToast("注册成功，请设置登录密码");
                                ActivityUtils.setActivity(ACTIVITY_MAIN2_PASSSWORD_SETTING, "phone", phone);
                            }
                        }
                    }
                });
    }

    /**
     * 设置密码
     * */
    public static void httpPasswordSetting(BaseActivity baseActivity,String phone,String password ,String inviter){
        baseActivity.showProgressDialog("设置中",true);
        LogUtils.e(phone,password,inviter);
        NetService.getInstance().setPasswordSetting(phone,password,inviter)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<CodeEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        baseActivity.hideProgress();
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                    }
                    @Override
                    public void onNext(CodeEntity codeEntity) {
                        baseActivity.hideProgress();
                        ToastUtils.showToast(baseActivity,"设置成功",1000);
                        LogUtils.e(codeEntity.getToken(), codeEntity.getUser(), codeEntity.isNeedFill());
                        UserEntity userEntity = codeEntity.getUser();
                        DataSharedPreferences.saveLoginName(phone);
                        DataSharedPreferences.saveUserBean(userEntity);
                        DataSharedPreferences.saveToken(codeEntity.getToken());
                        DataSharedPreferences.saveCreatateAccount(userEntity.getAccount());
                        Net.getInstance().setToken(DataSharedPreferences.getToken());
                        JumpUtils.getInstance().jumpToRoleMain(baseActivity, AppContent.getInstance().getMyUserRole());
                        loginActiivty.finish();
                        codeActivity.finish();
                        baseActivity.finish();
                    }
                });

    }

    /**
     * 找回密码
     * */
    public static void httpResetPassword(BaseActivity baseActivity,String phone,String verifyCode,String newPassword){
        baseActivity.showProgressDialog("请稍等",true);
        NetService.getInstance().setResetPassword(phone,verifyCode,newPassword)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                        baseActivity.hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        ToastUtils.showToast(baseActivity,s,1000);
                        baseActivity.finish();
                    }
                });
    }

    /**2.0验证码找回密码*/
    public static void resetPassword(BaseActivity baseActivity, EditText etMobile,EditText etCode,EditText etPassword,TextView tvLogin) {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etMobile))) {
            baseActivity. showToast("请输入手机号");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etCode))) {
            baseActivity.showToast("请输入验证码");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etPassword))) {
            baseActivity.showToast("请输入密码");
            return;
        }
        if (CommonUtil.getEditText(etPassword).length() < 6) {
            baseActivity.showToast("新密码长度不足6位");
            return;
        }
        baseActivity.showProgressDialog(getString(R.string.wait),true);
        tvLogin.setEnabled(false);
        NetService.getInstance().resetPassword(CommonUtil.getEditText(etMobile), CommonUtil.getEditText(etCode)
                , CommonUtil.getEditText(etPassword))
                .compose(baseActivity.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        baseActivity.showToast(ex.getDisplayMessage());
                        tvLogin.setEnabled(true);
                        baseActivity.hideProgress();
                    }

                    @Override
                    public void onNext(String s) {
                        tvLogin.setEnabled(true);
                        baseActivity.showToast("重置密码成功");
                        ActivityUtil.finishAllNotThis("ResetPasswordActivity");
                        baseActivity.startActivity(LoginActiivty.class);
                        baseActivity.finish();
                    }
                });
    }


    /***
     * 内容发布
     *
     */

    public static void httpContentPublish(BaseActivity baseActivity, String title, String content,
                                          String image, String video, String type , String combination ,
                                          String topicId , String atAccount,int videoSize,
                                          String videoCover,long playTime, int videoHorizontalVertical,boolean isOriginal){
        LogUtils.e(title,content,image,video,type,combination,topicId,atAccount,videoSize,videoCover,playTime,videoHorizontalVertical,isOriginal);

        baseActivity.showProgressDialog(baseActivity.getString(R.string.wait),false);
        NetService.getInstance().setContentPublish(title,content,image,video,type,
                combination,topicId,atAccount,videoSize,videoCover,playTime,videoHorizontalVertical,isOriginal)
                .compose(baseActivity.bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showToast(baseActivity,ex.getDisplayMessage(),1000);
                        baseActivity.hideProgress();
                    }
                    @Override
                    public void onNext(String s) {
                    baseActivity.hideProgress();
                    LogUtils.e(s);
                    baseActivity.finish();
                    ToastUtils.showToast(baseActivity,"发布成功",1000);
                    initShaClear(baseActivity);

                    }
                });

    }

    private static void initShaClear(BaseActivity baseActivity) {
        SharedPreferences sharedPreferences= baseActivity.getSharedPreferences(SAVE_MICRO_ARTICLE_DRAFT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        SharedPreferences sharedPreferences1= baseActivity.getSharedPreferences(SAVE_MICRO_VIDEO_DRAFT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor1 = sharedPreferences1.edit();
        editor1.clear();
        editor1.commit();
        SharedPreferences sharedPreferences2= baseActivity.getSharedPreferences(SAVE_MICRO_COMMUNITY_DRAFT, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor2 = sharedPreferences2.edit();
        editor2.clear();
        editor2.commit();


    }

    /**
         * 未登录或者token失效
         * */

    public static void notLogin(){

    }

    /**
     * 关闭刷新
     * */
    public static void closeRefresh(SmartRefreshLayout smartRefreshLayout) {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }


    public static void  onSubmitClick(BaseActivity baseActivitym,EditText etName,boolean companyType
            ,EditText etTaxpayer,TextView etContent,EditText etSum,EditText etEmail,TextView tvSubmit,String invoiceType) {
        if (TextUtils.isEmpty(CommonUtil.getEditText(etName))) {
            baseActivitym.showToast("请填写发票抬头");
            return;
        }
        if (companyType && TextUtils.isEmpty(CommonUtil.getEditText(etTaxpayer))) {
            baseActivitym.showToast("请填写税号");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etContent))) {
            baseActivitym.showToast("请填写发票内容");
            return;
        }
        if (TextUtils.isEmpty(CommonUtil.getEditText(etSum))) {
            baseActivitym. showToast("请填写开票金额");
            return;
        }
        Float sum;
        try {
            sum = Float.parseFloat(etSum.getText().toString());
        } catch (NumberFormatException e) {
            e.printStackTrace();
            sum = 0f;
        }
        if (sum <= 0) {
            baseActivitym.showToast("输入的开票金额不正确");
            return;
        }

        if (TextUtils.isEmpty(CommonUtil.getEditText(etEmail))) {
            baseActivitym.showToast("请填写电子邮箱");
            return;
        }
        KeyBoardUtils.closeKeybord(baseActivitym);
        tvSubmit.setEnabled(false);
        baseActivitym.showProgressDialog(getString(R.string.wait),true);
        NetService.getInstance().createInvoice(invoiceType, etName.getText().toString()
                , companyType ? etTaxpayer.getText().toString() : "",  ""
                , sum, etEmail.getText().toString(), etContent.getText().toString())
                .compose(baseActivitym.<String>bindLifeCycle())
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        baseActivitym.showToast(ex.getDisplayMessage());
                        baseActivitym. hideProgress();
                        tvSubmit.setEnabled(true);
                    }

                    @Override
                    public void onNext(String s) {
                        baseActivitym. hideProgress();
                        showSuccessDialog(baseActivitym);
                    }
                });
    }

    public static void showSuccessDialog(BaseActivity baseActivity) {
        InvoiceSuccessDialog successDialog = null;
        if (successDialog == null) {
            successDialog = new InvoiceSuccessDialog(baseActivity);
            successDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
//                    loadData();
//                    etSum.setText("");
//                    etSum.setSelection(etSum.getText().length());
//                    tvSubmit.setEnabled(true);
                    baseActivity.setResult(RESULT_OK);
                    baseActivity.finish();
                }
            });
        }
        successDialog.show();
    }

}
