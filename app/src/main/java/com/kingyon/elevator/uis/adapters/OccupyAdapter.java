package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.utils.TimeUtil;

/**
 * Created by GongLi on 2019/2/21.
 * Email：lc824767150@163.com
 */

public class OccupyAdapter extends BaseAdapterWithHF<Long> {
    public OccupyAdapter(Context context) {
        super(context);
    }

    @Override
    public int getFooterCount() {
        return 0;
    }

    @Override
    public int getHeaderCount() {
        return 0;
    }

    @NonNull
    @Override
    public BaseAdapterWithHF.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.dialog_occupy_item, parent, false);
        return new BaseAdapterWithHF.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder holder, int position) {
        TextView textView = (TextView) holder.itemRoot;
        Long itemData = getItemData(position);
        textView.setText(TimeUtil.getYMdTime(itemData != null ? itemData : 0));
    }
}
