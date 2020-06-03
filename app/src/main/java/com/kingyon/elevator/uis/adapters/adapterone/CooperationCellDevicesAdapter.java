package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
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
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class CooperationCellDevicesAdapter extends BaseAdapterWithHF<PointItemEntity> {

    public CooperationCellDevicesAdapter(Context context, int resId) {
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
        View view = inflater.inflate(R.layout.activity_cooperation_cell_devices_child, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        PointItemEntity item = getItemData(position);
        holder.tvName.setText(String.format("%s%s", item.getUnit(), item.getLift()));
        holder.tvName.setSelected(item.isChoosed());
        switch (item.getDevice()) {
            case Constants.DEVICE_PLACE.LEFT:
                holder.tvScreen.setText("左屏");
                holder.imgScreen.setImageResource(R.drawable.ic_screen_left);
                break;
            case Constants.DEVICE_PLACE.CENTER:
                holder.tvScreen.setText("中屏");
                holder.imgScreen.setImageResource(R.drawable.ic_screen_center);
                break;
            case Constants.DEVICE_PLACE.RIGHT:
                holder.tvScreen.setText("右屏");
                holder.imgScreen.setImageResource(R.drawable.ic_screen_right);
                break;
            default:
                holder.tvScreen.setText("");
                holder.imgScreen.setImageDrawable(null);
                break;
        }
        boolean deviceNormal = TextUtils.equals(Constants.DEVICE_STATUS.NORMAL, item.getStatus()) || TextUtils.equals(Constants.DEVICE_STATUS.ONLINE, item.getStatus());
        holder.tvOnline.setSelected(deviceNormal);
        holder.tvOnline.setText(deviceNormal ? "在线" : "维修");
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_screen)
        ImageView imgScreen;
        @BindView(R.id.tv_screen)
        TextView tvScreen;
        @BindView(R.id.tv_online)
        TextView tvOnline;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
