package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.SetPasswordView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 */
public class SetPasswordPresenter extends BasePresenter<SetPasswordView> {


    public SetPasswordPresenter(Context mContext) {
        super(mContext);
    }


    /**
     * 初始化支付密码
     *
     * @param password
     */
    public void initPayPassword(String password) {
        if (isViewAttached()) {
            getView().showProgressDialog("密码重置中...", false);
        }
        NetService.getInstance().initPayPassword(password)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().showShortToast(ex.getDisplayMessage());
                            getView().hideProgressDailog();
                        }
                    }

                    @Override
                    public void onNext(String s) {
                        if (isViewAttached()) {
                            LogUtils.d("支付密码设置：" + s);
                            getView().showShortToast("支付密码设置成功");
                            getView().hideProgressDailog();
                            getView().passwordInitSuccess();
                        }
                    }
                });
    }


}
