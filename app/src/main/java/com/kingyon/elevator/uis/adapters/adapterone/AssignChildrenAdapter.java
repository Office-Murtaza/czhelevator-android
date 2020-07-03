package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.PointItemEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2019/2/20.
 * Email：lc824767150@163.com
 */

public class AssignChildrenAdapter extends BaseAdapterWithHF<PointItemEntity> {
    private int resId;

    public AssignChildrenAdapter(Context context, int resId) {
        super(context);
        this.resId = resId;
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
        View view = inflater.inflate(resId, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        PointItemEntity item = getItemData(position);
        holder.tvName.setText(String.format("%s%s", item.getUnit(), item.getLift()));
        holder.tvName.setSelected(item.isChoosed());
        holder.tv_device_no.setText("编号："+item.getObjectId());
        switch (item.getDevice()) {
            case Constants.DEVICE_PLACE.LEFT:
                holder.tvScreen.setText("左屏");
                holder.imgScreen.setImageResource(R.mipmap.ic_screen_left);
                break;
            case Constants.DEVICE_PLACE.CENTER:
                holder.tvScreen.setText("中屏");
                holder.imgScreen.setImageResource(R.mipmap.ic_screen_middle);
                break;
            case Constants.DEVICE_PLACE.RIGHT:
                holder.tvScreen.setText("右屏");
                holder.imgScreen.setImageResource(R.mipmap.ic_screen_right);
                break;
            default:
                holder.tvScreen.setText("");
                holder.imgScreen.setImageDrawable(null);
                break;
        }
        if (item.getDeliverState() == null) {
            item.setDeliverState("");
        }
        switch (item.getDeliverState()) {
            case Constants.DELIVER_STATE.USABLE:
                holder.tvDeliver.setText("可用");
                holder.tvDeliver.setVisibility(View.GONE);
                holder.tvDeliver.setTextColor(0xFF81CF28);
                break;
            case Constants.DELIVER_STATE.MAINTAIN:
                holder.tvDeliver.setText("维修中");
                holder.tvDeliver.setBackgroundResource(R.drawable.ad_assign_new);
                holder.tvDeliver.setTextColor(0xFFFF3049);
                break;
            case Constants.DELIVER_STATE.OCCUPY:
                if (item.getOccupyTimes() != null) {
                    holder.tvDeliver.setText(String.format("占用(%s天)", item.getOccupyTimes().size()));
                } else {
                    holder.tvDeliver.setText("占用");
                }
                holder.tvDeliver.setTextColor(0xFFFF3049);
                holder.tvDeliver.setBackgroundResource(R.drawable.ad_assign_new);
                break;
            default:
                holder.tvDeliver.setText("");
                holder.tvDeliver.setTextColor(0xFFFF3049);
                break;
        }
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_screen)
        ImageView imgScreen;
        @BindView(R.id.tv_screen)
        TextView tvScreen;
        @BindView(R.id.tv_deliver)
        TextView tvDeliver;
        @BindView(R.id.tv_device_no)
        TextView tv_device_no;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
