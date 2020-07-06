package com.kingyon.elevator.presenter;

import android.content.Context;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.PublicFuncation;
import com.kingyon.elevator.view.AddNewBankCardView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 */
public class AddNewBankCardPresenter extends BasePresenter<AddNewBankCardView> {

    public AddNewBankCardPresenter(Context mContext) {
        super(mContext);
    }


    public void checkBindAccountData(String bingType, String account, String name, String kaihuhang) {
        if (account.isEmpty()) {
                getView().showShortToast("请输入账号");
            return;
        }
        if (name.isEmpty()) {
                getView().showShortToast("请输入名字");
            return;
        }
        if (bingType.equals("2")) {
            if (PublicFuncation.isMobileNO(account) || PublicFuncation.checkEmail(account)) {
                addAccount("2", account, name, kaihuhang);
            } else {
                    getView().showShortToast("请输入正确的支付宝账号");
                return;
            }
        } else if (bingType.equals("3")) {
            if (account.length()>6) {
                addAccount("3", account, name, kaihuhang);
            } else {
                    getView().showShortToast("请输入正确的微信账号");
                return;
            }
        } else {
            if (!AccountNumUtils.checkBankCard(account)) {
                    getView().showShortToast("请输入正确的银行卡号");
                return;
            }
            if (kaihuhang.isEmpty()) {
                    getView().showShortToast("请输入银行卡开户行");
                return;
            }
            addAccount("1", account, name, kaihuhang);
        }
    }


    private void addAccount(String bingType, String account, String name, String kaihuhang) {
        if (isViewAttached()) {
            getView().showProgressDialog("正在绑定账号...", false);
        }
        NetService.getInstance().addCashAccount(account, bingType, name, kaihuhang)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(),ex.getDisplayMessage());
                        getView().showShortToast("绑定失败");
                    }

                    @Override
                    public void onNext(String data) {
                        LogUtils.e(data);
                        getView().showShortToast("绑定成功");
                        getView().bindSuccess(bingType,account, name, kaihuhang);
                    }
                });


    }

}
