package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.SettlementEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.TimeUtil;

import java.util.ArrayList;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class PropertySettlementAdapter extends MultiItemTypeAdapter<Object> {
    public PropertySettlementAdapter(Context context, ArrayList<Object> mItems) {
        super(context, mItems);
        addItemViewDelegate(1, new SettlementDelegate());
        addItemViewDelegate(2, new TipDelegate());
    }

    private class SettlementDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_property_settlement_settlement;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof SettlementEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            SettlementEntity item = (SettlementEntity) o;
            holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getCreateTime()));
            float sum = item.getAmount();
            holder.setTextNotHide(R.id.tv_sum, CommonUtil.getTwoFloat(sum));
        }
    }

    private class TipDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_property_settlement_tip;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {

        }
    }
}
