package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellDeviceNumberEntity;
import com.kingyon.elevator.entities.DeviceNumberEntity;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.ArrayList;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationDeviceNumberAdapter extends MultiItemTypeAdapter<Object> {
    public CooperationDeviceNumberAdapter(Context context, ArrayList<Object> mItems) {
        super(context, mItems);
        addItemViewDelegate(1, new CellDelegate());
        addItemViewDelegate(2, new TotalDelegate());
        addItemViewDelegate(3, new TipDelegate());
    }

    private class CellDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_cooperation_device_cell;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof CellDeviceNumberEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            CellDeviceNumberEntity item = (CellDeviceNumberEntity) o;
            holder.setTextNotHide(R.id.tv_name, item.getCellName());
            holder.setTextNotHide(R.id.tv_address, item.getCellAddress());
            holder.setTextNotHide(R.id.tv_total, String.valueOf(item.getTotalScreen()));
            holder.setTextNotHide(R.id.tv_used, String.valueOf(item.getUsedScreen()));
            holder.setVisible(R.id.v_line, position != mItems.size() - 1);
        }
    }

    private class TipDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_cooperation_device_tip;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof String;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {

        }
    }

    private class TotalDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_cooperation_device_total;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof DeviceNumberEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            DeviceNumberEntity item = (DeviceNumberEntity) o;
            holder.setTextNotHide(R.id.tv_online, String.valueOf(item.getOnlineScreen()));
            holder.setTextNotHide(R.id.tv_used, String.valueOf(item.getUsedScreen()));
            holder.setTextNotHide(R.id.tv_total, String.valueOf(item.getTotalScreen()));
        }
    }
}
