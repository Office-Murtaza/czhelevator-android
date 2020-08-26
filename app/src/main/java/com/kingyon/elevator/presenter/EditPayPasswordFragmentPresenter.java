package com.kingyon.elevator.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CheckCodePresenter;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.view.EditPayPasswordFragmentView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public class EditPayPasswordFragmentPresenter extends BasePresenter<EditPayPasswordFragmentView> {

    private int maxCount = 120;
    private Disposable mDisposable;

    public EditPayPasswordFragmentPresenter(Context mContext) {
        super(mContext);
    }


    public void checkIsInitPayPwd() {
        if (isViewAttached()) {
            getView().showProgressDialog("数据查询中...", false);
        }
        NetService.getInstance().vaildInitPayPwd()
                .subscribe(new CustomApiCallback<CooperationInfoNewEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().hideProgressDailog();
//                            getView().checkPayPwdIsInit(true);
                        }
                    }

                    @Override
                    public void onNext(CooperationInfoNewEntity isInit) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().checkPayPwdIsInit(isInit.isSetPayPassword());
                        }
                    }
                });
    }


    /**
     * 获取验证码
     *
     * @param phone
     */
    public void getVerCode(String phone) {
        if (phone.isEmpty()) {
            if (isViewAttached()) {
                getView().showShortToast("请输入手机号");
            }
            return;
        }
        if (!PublicFuncation.isMobileNO(phone)) {
            if (isViewAttached()) {
                getView().showShortToast("请输入正确的手机号");
            }
            return;
        }
        if (isViewAttached()) {
            getView().showProgressDialog("验证码发送中...", false);
        }
        NetService.getInstance().sendVerifyCode(phone, CheckCodePresenter.VerifyCodeType.RESETPASSWORD)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().hideProgressDailog();
                        }
                        maxCount = 0;
                    }

                    @Override
                    public void onNext(String s) {
                        if (isViewAttached()) {
                            getView().showShortToast("验证码发送成功");
                            getView().hideProgressDailog();
                            startCountDownTime();
                        }
                    }
                });

    }


    //开始倒计时
    private void startCountDownTime() {
        Observable.interval(1000, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable disposable) {
                        mDisposable = disposable;
                    }

                    @Override
                    public void onNext(@NonNull Long number) {
                        if (isViewAttached()) {
                            getView().showCountDownTime(maxCount);
                            if (maxCount == 0) {
                                maxCount = 120;
                                cancel();
                            } else {
                                maxCount--;
                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 取消订阅
     */
    public void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }


    public void oldPasswordEditPayPwd(String oldPwd, String newPwd) {
        if (isViewAttached()) {
            getView().showProgressDialog("密码修改中...", false);
        }
        NetService.getInstance().changePasswordByOld(oldPwd, newPwd)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            if (ex.getDisplayMessage().contains("原始密码不正确")) {
                                getView().clearAllInputPwd();
                            }
                            getView().hideProgressDailog();
                        }
                    }

                    @Override
                    public void onNext(String content) {
                        getView().hideProgressDailog();
                        getView().showShortToast("支付密码修改成功");
                        getView().payPasswordEditSuccess(true);
//                        if (isViewAttached()) {
//                            if (content.equals("成功")) {
//
//                            } else {
//                                getView().showShortToast("密码修改失败");
//                                getView().hideProgressDailog();
//                            }
//                        }
                    }
                });
    }

    public void changePwdByCode(String phone, String code, String newPwd) {
        if (isViewAttached()) {
            getView().showProgressDialog("密码修改中...", false);
        }
        NetService.getInstance().changePasswordByCode(phone, code, newPwd)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().hideProgressDailog();
                            getView().payPasswordEditSuccess(false);
                        }
                    }

                    @Override
                    public void onNext(String content) {
                        getView().hideProgressDailog();
                        getView().showShortToast("支付密码修改成功");
                        getView().payPasswordEditSuccess(true);
//                        if (isViewAttached()) {
//                            if (content.equals("成功")) {
//
//                            } else {
//                                getView().showShortToast("密码修改失败");
//                                getView().hideProgressDailog();
//                            }
//                        }
                    }
                });
    }



}
