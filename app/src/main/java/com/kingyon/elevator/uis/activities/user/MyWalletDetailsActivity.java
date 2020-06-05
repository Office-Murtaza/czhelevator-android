package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.MyWalletInfo;
import com.kingyon.elevator.entities.WalletRecordEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adapterone.MyWalletAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @Created By Admin  on 2020/6/5
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */

public class MyWalletDetailsActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.pre_recycler_view)
    RecyclerView preRecyclerView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_wallet_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        NetService.getInstance().myWalletInfo(1)
                .compose(this.<MyWalletInfo>bindLifeCycle())
                .subscribe(new CustomApiCallback<MyWalletInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());

                    }
                    @Override
                    public void onNext(MyWalletInfo myWalletInfo) {
                        PageListEntity<WalletRecordEntity> recordPage = myWalletInfo.getRecordPage();

                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }

}
