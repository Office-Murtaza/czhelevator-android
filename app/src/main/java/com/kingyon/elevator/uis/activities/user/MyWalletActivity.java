package com.kingyon.elevator.uis.activities.user;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chanven.lib.cptr.loadmore.SwipeRefreshHelper;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.MyWalletInfo;
import com.kingyon.elevator.entities.WalletRecordEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.MyWalletAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MyWalletActivity extends BaseStateRefreshingLoadingActivity<Object> {
    @BindView(R.id.tv_balance)
    TextView tvBalance;
    @BindView(R.id.ll_wallet)
    LinearLayout llWallet;
    @BindView(R.id.tv_wallet_records_tip)
    TextView tvWalletRecordsTip;

    private boolean fixation = true;

    @Override
    protected String getTitleText() {
        return "我的钱包";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_wallet;
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new MyWalletAdapter(this, mItems);
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().myWalletInfo(page)
                .compose(this.<MyWalletInfo>bindLifeCycle())
                .subscribe(new CustomApiCallback<MyWalletInfo>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(MyWalletInfo myWalletInfo) {
                        PageListEntity<WalletRecordEntity> recordPage = myWalletInfo.getRecordPage();
                        if (recordPage == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            Float balance = myWalletInfo.getBalance();
                            if (balance == null) {
                                throw new ResultException(9001, "返回参数异常");
                            }
                            if (fixation) {
                                llWallet.setVisibility(View.VISIBLE);
                                tvBalance.setText(CommonUtil.getMayTwoFloat(balance));
                                if (recordPage.getContent() != null && recordPage.getContent().size() > 0) {
                                    tvWalletRecordsTip.setVisibility(View.VISIBLE);
                                } else {
                                    tvWalletRecordsTip.setVisibility(View.GONE);
                                }
                            } else {
                                llWallet.setVisibility(View.GONE);
                                tvWalletRecordsTip.setVisibility(View.GONE);
                                mItems.add(balance);
                                if (recordPage.getContent() != null && recordPage.getContent().size() > 0) {
                                    mItems.add("钱包流水");
                                }
                            }
                        }
                        if (recordPage.getContent() != null) {
                            mItems.addAll(recordPage.getContent());
                        }
                        loadingComplete(true, recordPage.getTotalPages());
                    }
                });
    }

    protected void initState(boolean loadSuccess) {
        if (loadSuccess) {
            stateLayout.showContentView();
        } else {
            if (mCurrPage == FIRST_PAGE) {
                stateLayout.showErrorView(getString(R.string.error));
            }
        }
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (view.getId() == R.id.tv_recharge) {
            startActivityForResult(RechargeActivity.class, CommonUtil.REQ_CODE_1);
        }
    }

    @Override
    protected boolean isShowDivider() {
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode && CommonUtil.REQ_CODE_1 == requestCode) {
            autoRefresh();
        }
    }

    @OnClick(R.id.tv_recharge)
    public void onViewClicked() {
        startActivityForResult(RechargeActivity.class, CommonUtil.REQ_CODE_1);
    }

    @Override
    protected void setupRefreshAndLoadMore() {
        mSwipeRefreshHelper = new SwipeRefreshHelper(mLayoutRefresh);
        try {
            Field field = mSwipeRefreshHelper.getClass().getDeclaredField("mContentView");
            field.setAccessible(true);
            field.set(mSwipeRefreshHelper, mRecyclerView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mSwipeRefreshHelper.setOnSwipeRefreshListener(this);
        mSwipeRefreshHelper.setOnLoadMoreListener(this);

        if (isAutoRefresh()) {
            autoRefresh();
        }
    }
}
