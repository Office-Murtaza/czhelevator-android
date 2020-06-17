package com.kingyon.elevator.uis.fragments.main2.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.MyWalletInfo;
import com.kingyon.elevator.entities.WalletRecordEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.MyWalletTwoAdapter;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Created By Admin  on 2020/6/12
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions: 钱包明细
 */
public class MyWalletDetailsFragment extends BaseFragment {
    String type, type1;
    @BindView(R.id.pre_recycler_view)
    RecyclerView preRecyclerView;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    int page = 1;
    List<WalletRecordEntity> recommendEntityList = new ArrayList<>();
    MyWalletTwoAdapter myWalletTwoAdapter;
    public MyWalletDetailsFragment setIndex(String type, String type1) {
        this.type = type;
        this.type1 = type1;
        return (this);
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_mywallet_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        if (smartRefreshLayout!=null) {
            smartRefreshLayout.autoRefresh(100);
        }else {
            httpList(1, type);
        }

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page ++;
                httpList(page,type);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                recommendEntityList.clear();
                httpList(page,type);
            }
        });

    }

    private void httpList(int page, String type) {
//        showProgressDialog("请稍后...");
        NetService.getInstance().myWalletInfo(page, type)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<MyWalletInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        hideProgress();
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                    }

                    @Override
                    public void onNext(MyWalletInfo myWalletInfo) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        hideProgress();
                        dataAdd(myWalletInfo);
                    }
                });


    }

    private void dataAdd(MyWalletInfo myWalletInfo) {
        for (int i = 0; i < myWalletInfo.getRecordPage().getContent().size(); i++) {
            WalletRecordEntity walletRecordEntity = new WalletRecordEntity();
            walletRecordEntity = myWalletInfo.getRecordPage().getContent().get(i);
            recommendEntityList.add(walletRecordEntity);
        }
        if (myWalletTwoAdapter == null || page == 1) {
            myWalletTwoAdapter = new MyWalletTwoAdapter(getActivity(), type);
            myWalletTwoAdapter.addData(recommendEntityList);
            preRecyclerView.setAdapter(myWalletTwoAdapter);
            preRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        } else {
            myWalletTwoAdapter.addData(recommendEntityList);
            myWalletTwoAdapter.notifyDataSetChanged();
        }

    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
