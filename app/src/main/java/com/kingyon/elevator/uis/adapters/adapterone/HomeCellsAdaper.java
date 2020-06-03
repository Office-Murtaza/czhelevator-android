package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.CellItemEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.utils.FormatUtils;
import com.kingyon.elevator.utils.NumberFormatUtils;
import com.leo.afbaselibrary.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2018/12/25.
 * Email：lc824767150@163.com
 */

public class HomeCellsAdaper extends BaseAdapterWithHF<CellItemEntity> {
    public HomeCellsAdaper(Context context) {
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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_homepage_cell, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        CellItemEntity item = getItemData(position);
        holder.itemRoot.setSelected(position % 2 == 1);
        GlideUtils.loadImage(context, item.getCellLogo(), holder.imgCover);
        holder.tvName.setText(item.getCellName());
        holder.tvLift.setText(FormatUtils.getInstance().getCellLift(item.getLiftNum()));
        holder.tvUnit.setText(FormatUtils.getInstance().getCellUnit(item.getUnitNum()));
        holder.tvPrice.setText(FormatUtils.getInstance().getCellPrice(item.getBusinessAdPrice()));
        holder.tvAddress.setText(item.getAddress());
        holder.tvDistance.setText(FormatUtils.getInstance().getCellDistance(item.getLongitude(), item.getLatitude(), item.getDistance()));
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.img_cover)
        ImageView imgCover;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_lift)
        TextView tvLift;
        @BindView(R.id.tv_unit)
        TextView tvUnit;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.tv_distance)
        TextView tvDistance;
        @BindView(R.id.img_plan)
        ImageView imgPlan;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            imgPlan.setOnClickListener(this);
        }
    }
}
