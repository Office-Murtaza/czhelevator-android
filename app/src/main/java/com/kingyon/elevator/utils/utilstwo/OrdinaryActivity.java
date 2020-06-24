package com.kingyon.elevator.utils.utilstwo;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.UserEntity;
import com.kingyon.elevator.entities.entities.CodeEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.Net;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.login.CodeActivity;
import com.kingyon.elevator.uis.actiivty2.login.LoginActiivty;
import com.kingyon.elevator.uis.actiivty2.login.UserLoginActiivty;
import com.kingyon.elevator.uis.actiivty2.main.CommunityReleasetyActivity;
import com.kingyon.elevator.utils.JumpUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import static com.czh.myversiontwo.activity.ActivityUtils.setActivity;
import static com.czh.myversiontwo.utils.CodeType.QQ;
import static com.czh.myversiontwo.utils.CodeType.WX;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CODE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_PASSSWORD_SETTING;
import static com.kingyon.elevator.constants.Constants.LoginType.ALI;

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
        activity.showProgressDialog(activity.getString(R.string.wait));
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
        activity.showProgressDialog(activity.getString(R.string.wait));
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
                        ToastUtils.showToast(activity,s,1000);
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
        activity.showProgressDialog("请稍等...");
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
    public static void httpLogin(BaseActivity baseActivity, String phone, String password, String way, String unique, String avatar, String nickName, LinearLayout llSf,TextView tvLoginUser) {
        baseActivity.showProgressDialog(baseActivity.getString(R.string.wait));
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
                            ActivityUtils.setActivity(ACTIVITY_MAIN2_PASSSWORD_SETTING, "phone", phone);
                        }
                    }
                    @Override
                    public void onNext(CodeEntity codeEntity) {
                        baseActivity.hideProgress();
                        ToastUtils.showToast(baseActivity, "成功", 1000);
                        if (codeEntity != null) {
                            if (codeEntity.isNeedSetPwd()) {
                                UserEntity userEntity = codeEntity.getUser();
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
                                    ActivityUtils.setActivity(ACTIVITY_MAIN2_PASSSWORD_SETTING, "phone", phone);
                                }
                            } else {
                                LogUtils.e(codeEntity.getToken(), codeEntity.getUser(), codeEntity.isNeedFill());
                                UserEntity userEntity = codeEntity.getUser();
                                DataSharedPreferences.saveLoginName(phone);
                                DataSharedPreferences.saveUserBean(userEntity);
                                DataSharedPreferences.saveCreatateAccount(userEntity.getAccount());
                                DataSharedPreferences.saveToken(codeEntity.getToken());
                                Net.getInstance().setToken(DataSharedPreferences.getToken());
                                JumpUtils.getInstance().jumpToRoleMain(baseActivity, AppContent.getInstance().getMyUserRole());
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
        baseActivity.showProgressDialog("设置中");
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
        baseActivity.showProgressDialog("请稍等");
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

    /***
     * 内容发布
     *
     */

    public static void httpContentPublish(BaseActivity baseActivity, String title, String content,
                                          String image, String video, String type , String combination ,
                                          String topicId , String atAccount,int videoSize,
                                          String videoCover,long playTime, int videoHorizontalVertical,boolean isOriginal){
        LogUtils.e(title,content,image,video,type,combination,topicId,atAccount,videoSize,videoCover,playTime,videoHorizontalVertical,isOriginal);

        baseActivity.showProgressDialog(baseActivity.getString(R.string.wait));
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

                    }
                });

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
}
