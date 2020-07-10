package com.kingyon.elevator.uis.activities.cooperation;

import android.text.Html;
import android.text.TextUtils;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.utils.StringContent;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.WithdrawItemEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.WithdrawEntily;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.AccountNumUtils;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationWithdrawRecordsActivity extends BaseStateRefreshingLoadingActivity<WithdrawItemEntity> {
    @Override
    protected String getTitleText() {
        return "提现记录";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_cooperation_withdraw_records;
    }

    @Override
    protected MultiItemTypeAdapter<WithdrawItemEntity> getAdapter() {
        return new BaseAdapter<WithdrawItemEntity>(this, R.layout.activity_cooperation_withdraw_records_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, WithdrawItemEntity item, int position) {
                holder.setText(R.id.tv_txtype,FormatUtils.getInstance().withdrawalType(item.getWithDrawWay())+"提现");
                holder.setText(R.id.tv_zh, AccountNumUtils.hidePhoneNum(item.getAliAcount()));
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getTime()));
                holder.setText(R.id.tv_state, Html.fromHtml(FormatUtils.getInstance().getWithdrawState(item.getStatus(),item.getFaildReason())));
                holder.setText(R.id.tv_tax,"（扣税"+CommonUtil.getTwoFloat(item.getAmount()*0.06)+"）");

                holder.setTextNotHide(R.id.tv_sum, CommonUtil.getTwoFloat(item.getAmount()));
                boolean failed = TextUtils.equals(item.getStatus(), Constants.Withdraw_Status.FAILED);
//                holder.setTextColor(R.id.tv_state, failed ? 0xFFEE4E33 : 0xFF808080);
//                holder.setTextNotHide(R.id.tv_reason, String.format("失败原因：%s", item.getFaildReason()));
//                holder.setVisible(R.id.tv_reason, failed && !TextUtils.isEmpty(item.getFaildReason()));
            }
        };
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().partnerWithdrawList(page)
                .compose(this.<WithdrawEntily<WithdrawEntily.PageContentBean<WithdrawItemEntity>> >bindLifeCycle())
                .subscribe(new CustomApiCallback<WithdrawEntily<WithdrawEntily.PageContentBean<WithdrawItemEntity>> >() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }
                    @Override
                    public void onNext(WithdrawEntily<WithdrawEntily.PageContentBean<WithdrawItemEntity>> withdrawItemEntityPageContentBean) {
                        if (withdrawItemEntityPageContentBean == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (withdrawItemEntityPageContentBean.pageContent.lstData != null) {
                            mItems.addAll(withdrawItemEntityPageContentBean.pageContent.lstData);
                        }
                        loadingComplete(true, withdrawItemEntityPageContentBean.totalPages);
                    }
                });

    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.white_margin_sixteen_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
    }
}
