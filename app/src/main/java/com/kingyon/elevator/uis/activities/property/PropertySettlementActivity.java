package com.kingyon.elevator.uis.activities.property;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.SettlementEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.IncomeRecordsActivity;
import com.kingyon.elevator.uis.adapters.adapterone.PropertySettlementAdapter;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.nets.entities.PageListEntity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.List;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertySettlementActivity extends BaseStateRefreshingLoadingActivity<Object> {

    private String role;

    @Override
    protected String getTitleText() {
        role = getIntent().getStringExtra(CommonUtil.KEY_VALUE_1);
        return "结算记录";
    }

    @Override
    protected MultiItemTypeAdapter<Object> getAdapter() {
        return new PropertySettlementAdapter(this, mItems);
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_property_settlement;
    }

    @Override
    protected void loadData(final int page) {
        NetService.getInstance().propertySettlementList("ALL", page)
                .compose(this.<PageListEntity<SettlementEntity>>bindLifeCycle())
                .subscribe(new CustomApiCallback<PageListEntity<SettlementEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(PageListEntity<SettlementEntity> settlementEntityPageListEntity) {
                        if (settlementEntityPageListEntity == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        List<SettlementEntity> content = settlementEntityPageListEntity.getContent();
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                            if (content != null && content.size() > 0) {
                                mItems.add("tip");
                            }
                        }
                        if (content != null) {
                            mItems.addAll(content);
                        }
                        loadingComplete(true, settlementEntityPageListEntity.getTotalPages());
                    }
                });
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, Object item, int position) {
        super.onItemClick(view, holder, item, position);
        if (item != null && item instanceof SettlementEntity) {
            SettlementEntity entity = (SettlementEntity) item;
            Bundle bundle = new Bundle();
            bundle.putString(CommonUtil.KEY_VALUE_2, entity.getBillSn());
            bundle.putString(CommonUtil.KEY_VALUE_3, role);
            startActivity(IncomeRecordsActivity.class, bundle);
        }
    }

    @Override
    protected void setDivider() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this)
                .drawable(R.drawable.white_margin_sixteen_divider)
                .sizeResId(R.dimen.spacing_divider)
                .build());
    }
}
