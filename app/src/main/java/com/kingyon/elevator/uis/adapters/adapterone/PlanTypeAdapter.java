package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2019/1/2.
 * Email：lc824767150@163.com
 */

public class PlanTypeAdapter extends BaseAdapterWithHF<NormalParamEntity> implements BaseAdapterWithHF.OnItemClickListener<NormalParamEntity> {
    public PlanTypeAdapter(Context context) {
        super(context);
        setOnItemClickListener(this);
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
        View view = inflater.inflate(R.layout.fragment_plan_type_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        NormalParamEntity item = getItemData(position);
        holder.tvName.setText(item.getName());
        holder.flItem.setSelected(item.isChoosed());
        holder.imgSelected.setSelected(item.isChoosed());
    }

    @Override
    public void onItemClick(View view, int position, NormalParamEntity entity, BaseAdapterWithHF<NormalParamEntity> baseAdaper) {
        if (entity != null) {
            List<NormalParamEntity> datas = baseAdaper.getDatas();
            for (NormalParamEntity item : datas) {
                if (item == entity) {
                    item.setChoosed(!item.isChoosed());
                } else {
                    item.setChoosed(false);
                }
//                item.setChoosed(item == entity);
            }
            notifyDataSetChanged();
        }
    }

    public NormalParamEntity getChoosedEntity() {
        NormalParamEntity result = null;
        for (NormalParamEntity item : getDatas()) {
            if (item.isChoosed()) {
                result = item;
                break;
            }
        }
        return result;
    }

    public void setChoosedType(String type) {
        for (NormalParamEntity item : getDatas()) {
            item.setChoosed(TextUtils.equals(item.getType(), type));
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.img_selected)
        ImageView imgSelected;
        @BindView(R.id.fl_item)
        FrameLayout flItem;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
