package com.kingyon.elevator.uis.activities;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.IncomeRecordEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class IncomeRecordsActivity extends BaseStateRefreshingLoadingActivity<IncomeRecordEntity> {

    private Long deviceId;
    private String billSn;
    private String role;

    @Override
    protected String getTitleText() {
        deviceId = getIntent().getLongExtra(CommonUtil.KEY_VALUE_1, 0);
        if (deviceId.longValue() == 0) {
            deviceId = null;
        }
        billSn = getIntent().getStringExtra(CommonUtil.KEY_VALUE_2);
        role = getIntent().getStringExtra(CommonUtil.KEY_VALUE_3);
        return "明细";
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_income_records;
    }

    @Override
    protected MultiItemTypeAdapter<IncomeRecordEntity> getAdapter() {
        return new BaseAdapter<IncomeRecordEntity>(this, R.layout.activity_income_records_item, mItems) {
            @Override
            protected void convert(CommonHolder holder, IncomeRecordEntity item, int position) {
                holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getCreateTime()));
                float sum = item.getAmount();
                holder.setTextNotHide(R.id.tv_sum, sum > 0 ? String.format("+%s", CommonUtil.getTwoFloat(sum)) : CommonUtil.getTwoFloat(sum));
            }
        };
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().incomeRecordsList("ALL", deviceId, billSn, role, page)
                .compose(this.<PageListEntity<IncomeRecordEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<IncomeRecordEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<IncomeRecordEntity> incomeRecordEntityPageListEntity) {
                        if (incomeRecordEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        List<IncomeRecordEntity> content = incomeRecordEntityPageListEntity.getContent();
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        if (content != null) {
                            mItems.addAll(content);
                        }
                        loadingComplete(true, incomeRecordEntityPageListEntity.getTotalPages());
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
