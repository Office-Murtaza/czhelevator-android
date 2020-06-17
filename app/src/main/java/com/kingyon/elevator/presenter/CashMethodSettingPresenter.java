package com.kingyon.elevator.presenter;

import android.content.Context;

import com.kingyon.elevator.entities.BindAccountEntity;
import com.kingyon.elevator.mvpbase.BasePresenter;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.view.CashMethodSettingView;
import com.leo.afbaselibrary.nets.exceptions.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By SongPeng  on 2019/11/27
 * Email : 1531603384@qq.com
 */
public class CashMethodSettingPresenter extends BasePresenter<CashMethodSettingView> {

    private int startPosition = 0;
    private int size = 30;
    private List<BindAccountEntity> bindAccountEntityList;


    public CashMethodSettingPresenter(Context mContext) {
        super(mContext);
        bindAccountEntityList = new ArrayList<>();
    }


    public List<BindAccountEntity> getBindAccountEntityList() {
        return bindAccountEntityList;
    }


    public void getAccountList() {
        if (isViewAttached()) {
            getView().showProgressView();
        }
        NetService.getInstance().getUserCashTypeList(startPosition + "", size + "")
                .subscribe(new CustomApiCallback<List<BindAccountEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showErrorView();
                        }
                    }

                    @Override
                    public void onNext(List<BindAccountEntity> incomeDetailsEntities) {
                        if (isViewAttached()) {
                            getView().hideProgressDailog();
                            getView().showContentView();
                            bindAccountEntityList = incomeDetailsEntities;
                            getView().showListData(bindAccountEntityList);
                        }
                    }
                });
    }

}
