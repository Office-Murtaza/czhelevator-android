package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.PlanItemEntity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2018/12/29.
 * Email：lc824767150@163.com
 */

public class PointPlanAddAdapter extends BaseAdapterWithHF<PlanItemEntity> {
    public PointPlanAddAdapter(Context context) {
        super(context);
    }

    @Override
    public int getFooterCount() {
        return 0;
    }

    @Override
    public int getHeaderCount() {
        return 1;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.fragment_dialog_point_plan_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (getItemViewType(position) == TYPE_HEADER) {
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_create_plan, 0);
            holder.tvName.setText("新建投放计划");
        } else {
            PlanItemEntity item = getItemData(position);
            holder.tvName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_check_big, 0);
            holder.tvName.setText(item.getPlanName());
            holder.tvName.setSelected(item.isChoosed());
        }
    }

    public List<PlanItemEntity> getAllChoosed() {
        List<PlanItemEntity> entities = new ArrayList<>();
        for (PlanItemEntity item : getDatas()) {
            if (item.isChoosed()) {
                entities.add(item);
            }
        }
        return entities;
    }

    public String getAllChoosedParam() {
        StringBuilder stringBuilder = new StringBuilder();
        for (PlanItemEntity item : getDatas()) {
            if (item.isChoosed()) {
                stringBuilder.append(item.getObjectId()).append(",");
            }
        }
        String result;
        if (stringBuilder.length() > 0) {
            result = stringBuilder.substring(0, stringBuilder.length());
        } else {
            result = "";
        }
        return result;
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
