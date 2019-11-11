package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.WalletRecordEntity;
import com.kingyon.elevator.utils.CommonUtil;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.TimeUtil;
import com.leo.afbaselibrary.uis.adapters.ItemViewDelegate;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;

import java.util.ArrayList;

/**
 * Created by GongLi on 2019/1/10.
 * Email：lc824767150@163.com
 */

public class MyWalletAdapter extends MultiItemTypeAdapter<Object> {
    public MyWalletAdapter(Context context, ArrayList<Object> mItems) {
        super(context, mItems);
        addItemViewDelegate(1, new RecordDelegate());
        addItemViewDelegate(2, new WalletDelegate());
        addItemViewDelegate(3, new TipDelegate());
    }

    private class RecordDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_my_wallet_record;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof WalletRecordEntity;
        }

        @Override
        public void convert(CommonHolder holder, Object o, int position) {
            WalletRecordEntity item = (WalletRecordEntity) o;
            holder.setTextNotHide(R.id.tv_name, item.getName());
            holder.setTextNotHide(R.id.tv_time, TimeUtil.getAllTimeNoSecond(item.getTime()));
            String recordType = item.getType() != null ? item.getType() : "";
            String negativeNum = item.getAmount() == 0 ? "0" : String.format("-%s", CommonUtil.getMayTwoFloat(Math.abs(item.getAmount())));
            String positiveNum = item.getAmount() == 0 ? "0" : String.format("+%s", CommonUtil.getMayTwoFloat(Math.abs(item.getAmount())));
            switch (recordType) {
                case "EXPEND":
                    holder.setTextNotHide(R.id.tv_sum, negativeNum);
                    break;
                case "INCOME":
                    holder.setTextNotHide(R.id.tv_sum, positiveNum);
                    break;
                default:
                    holder.setTextNotHide(R.id.tv_sum, item.getAmount() > 0 ? positiveNum : negativeNum);
                    break;
            }
            String payWay = FormatUtils.getInstance().getPayWay(item.getPayType());
            holder.setTextNotHide(R.id.tv_pay_way, String.format("%s%s", payWay, TextUtils.isEmpty(payWay) ? "" : "支付"));
            holder.setVisible(R.id.v_line, position != mItems.size() - 1);
        }
    }

    private class WalletDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_my_wallet_wallet;
        }

        @Override
        public boolean isForViewType(Object item, int position) {
            return item instanceof Float;
        }

        @Override
        public void convert(final CommonHolder holder, Object o, int position) {
            holder.setTextNotHide(R.id.tv_balance, CommonUtil.getTwoFloat((float) o));
            holder.setOnClickListener(R.id.tv_recharge, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        int position = holder.getAdapterPosition();
                        mOnItemClickListener.onItemClick(v, holder, (position >= 0 && position < mItems.size()) ? mItems.get(position) : null, position);
                    }
                }
            });
        }
    }

    private class TipDelegate implements ItemViewDelegate<Object> {
        @Override
        public int getItemViewLayoutId() {
            return R.layout.activity_my_wallet_tip;
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
