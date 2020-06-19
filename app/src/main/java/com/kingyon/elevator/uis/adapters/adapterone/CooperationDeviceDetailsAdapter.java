package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.text.TextUtils;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.IncomeRecordEntity;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.List;

/**
 * Created by GongLi on 2019/1/15.
 * Email：lc824767150@163.com
 */

public class CooperationDeviceDetailsAdapter extends MultiItemTypeAdapter<Object> {
    public CooperationDeviceDetailsAdapter(Context context, List<Object> items) {
        super(context, items);
        addItemViewDelegate(1, new RecordDelegate());
        addItemViewDelegate(2, new DeviceDelegate());
        addItemViewDelegate(3, new TipDelegate());
    }

    private class RecordDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_cooperation_devices_details_record;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof IncomeRecordEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            IncomeRecordEntity item = (IncomeRecordEntity) o;
            holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getCreateTime()));
            holder.setTextNotHide(R.id.tv_sum, CommonUtil.getTwoFloat(item.getAmount()));
            holder.setVisible(R.id.v_line, position != mItems.size() - 1);
        }
    }

    private class DeviceDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_cooperation_devices_details_device;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof PointItemEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            PointItemEntity item = (PointItemEntity) o;
            holder.setTextNotHide(R.id.tv_device_id, CommonUtil.getDeviceId(item.getObjectId()));
            holder.setTextNotHide(R.id.tv_device_no, item.getDeviceNo());
            holder.setTextNotHide(R.id.tv_device_time, TimeUtil.getAllTimeNoSecond(item.getNetTime()));
            holder.setTextNotHide(R.id.tv_cell_address, item.getCellAddress());
            holder.setTextNotHide(R.id.tv_lift_no, item.getLiftNo());
            StringBuilder stringBuilder = new StringBuilder();
            if (!TextUtils.isEmpty(item.getCellName())) {
                stringBuilder.append(item.getCellName()).append("·");
            }
            String buildUnit = String.format("%s%s", item.getBuild() != null ? item.getBuild() : "", item.getUnit() != null ? item.getUnit() : "");
            if (!TextUtils.isEmpty(buildUnit)) {
                stringBuilder.append(buildUnit).append("·");
            }
            if (!TextUtils.isEmpty(item.getLift())) {
                stringBuilder.append(item.getLift()).append("·");
            }
            String deviceOritation = FormatUtils.getInstance().getDeviceOritation(item.getDevice());
            if (!TextUtils.isEmpty(deviceOritation)) {
                stringBuilder.append(deviceOritation).append("·");
            }
            String deviceLocatoin = stringBuilder.length() > 1 ? stringBuilder.substring(0, stringBuilder.length() - 1) : "";
            holder.setTextNotHide(R.id.tv_device_location, deviceLocatoin);
            holder.setTextNotHide(R.id.tv_device_status, (TextUtils.equals(Constants.DEVICE_STATUS.NORMAL, item.getStatus()) || TextUtils.equals(Constants.DEVICE_STATUS.ONLINE, item.getStatus())) ? "正常" : "维修中");
            holder.setTextNotHide(R.id.tv_ad_status, TextUtils.equals(Constants.Device_AD_STATUS.PROCESSING, item.getAdStatus()) ? "投放中" : "未投放");

        }
    }

    private class TipDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_cooperation_devices_details_tip;
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
