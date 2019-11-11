package com.kingyon.elevator.uis.activities.cooperation;

import android.os.Bundle;
import android.text.TextUtils;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeStatisticsEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.widgets.CooperationIncomeStickyDecoration;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertyFeeActivity extends BaseStateRefreshingLoadingActivity<IncomeStatisticsEntity> implements CooperationIncomeStickyDecoration.GroupStickyListener {
    @Override
    protected String getTitleText() {
        return "点位费";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_property_fee;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        mRecyclerView.addItemDecoration(new CooperationIncomeStickyDecoration(this, this, false));
    }

    @Override
    protected MultiItemTypeAdapter<IncomeStatisticsEntity> getAdapter() {
        return new BaseAdapter<IncomeStatisticsEntity>(this, R.layout.activity_property_fee_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, IncomeStatisticsEntity item, int position) {
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getTime()));
                float sum = item.getSum();
                holder.setTextNotHide(R.id.tv_sum, sum > 0 ? String.format("+%s", CommonUtil.getTwoFloat(sum)) : CommonUtil.getTwoFloat(sum));
            }
        };
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().partnerPropertyFeeRecords(page)
                .compose(this.<PageListEntity<IncomeStatisticsEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<IncomeStatisticsEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<IncomeStatisticsEntity> incomeStatisticsEntityPageListEntity) {
                        if (incomeStatisticsEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (incomeStatisticsEntityPageListEntity.getContent() != null) {
                            mItems.addAll(incomeStatisticsEntityPageListEntity.getContent());
                        }
                        loadingComplete(true, incomeStatisticsEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    public boolean isFirstInGroup(int pos) {
        boolean result;
        if (pos == 0) {
            result = true;
        } else {
            String prevGroupId = getGroupName(pos - 1);
            String groupId = getGroupName(pos);
            result = !TextUtils.equals(prevGroupId, groupId);
        }
        return result;
    }

    @Override
    public String getGroupName(int position) {
        String result = null;
        if (position >= 0 && position < mItems.size()) {
            IncomeStatisticsEntity item = mItems.get(position);
            result = TimeUtil.getYCh(item.getTime());
        }
        return result;
    }

    @Override
    public double getTotal() {
        return 0;
    }
}
