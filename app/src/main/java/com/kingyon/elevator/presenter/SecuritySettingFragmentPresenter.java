package com.kingyon.elevator.presenter;

import android.support.v4.app.FragmentActivity;

import com.kingyon.elevator.entities.CooperationInfoNewEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.SecuritySettingFragmentView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

/**
 * Created By SongPeng  on 2019/12/3
 * Email : 1531603384@qq.com
 */
public class SecuritySettingFragmentPresenter extends BasePresenter<SecuritySettingFragmentView> {

    public SecuritySettingFragmentPresenter(FragmentActivity mActivity) {
        super(mActivity);
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


    public void checkPayPasswordIsRight(String pwd) {
        if (isViewAttached()) {
            getView().showProgressDialog("支付密码验证中...", false);
        }
        NetService.getInstance().vaildPasswordIsRight(pwd)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().hideProgressDailog();
                            getView().checkPayPwdIsSuccess();
                        }
                    }

                    @Override
                    public void onNext(String content) {
                        if (isViewAttached()) {
                            if (content.equals("成功")) {
                                getView().hideProgressDailog();
                                getView().showShortToast("支付密码验证成功");
                                getView().checkPayPwdIsSuccess();
                            } else {
                                getView().showShortToast("支付密码验证失败");
                                getView().hideProgressDailog();
                            }
                        }
                    }
                });
    }

}
