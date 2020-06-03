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
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2018/12/27.
 * Email：lc824767150@163.com
 */

public class SearchCellTypeAdapter extends BaseAdapterWithHF<NormalParamEntity> implements BaseAdapterWithHF.OnItemClickListener<NormalParamEntity> {

    private int layoutRes;

    public SearchCellTypeAdapter(Context context, int layoutRes) {
        super(context);
        this.layoutRes = layoutRes;
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
        View view = inflater.inflate(layoutRes, parent, false);
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
            if (TextUtils.isEmpty(entity.getType())) {
                clearSelected();
            } else {
                entity.setChoosed(!entity.isChoosed());
                List<NormalParamEntity> datas = getDatas();
                boolean hasChoose = false;
                for (NormalParamEntity item : datas) {
                    if (!TextUtils.isEmpty(item.getType()) && item.isChoosed()) {
                        hasChoose = true;
                        break;
                    }
                }
                setNoLimitChoose(!hasChoose);
            }
            notifyDataSetChanged();
        }
    }

    public void setNoLimitChoose(boolean choose) {
        List<NormalParamEntity> datas = getDatas();
        for (NormalParamEntity entity : datas) {
            if (TextUtils.isEmpty(entity.getType())) {
                entity.setChoosed(choose);
            }
        }
    }

    public void clearSelected() {
        List<NormalParamEntity> datas = getDatas();
        for (NormalParamEntity entity : datas) {
            entity.setChoosed(TextUtils.isEmpty(entity.getType()));
        }
        notifyDataSetChanged();
    }

    public String[] getChoosedParams() {
        StringBuilder nameBuilder = new StringBuilder();
        StringBuilder typeBuilder = new StringBuilder();
        List<NormalParamEntity> datas = getDatas();
        for (NormalParamEntity item : datas) {
            if (item.isChoosed()) {
                nameBuilder.append(item.getName()).append(",");
                typeBuilder.append(item.getType()).append(",");
            }
        }
        String name = nameBuilder.length() > 0 ? nameBuilder.substring(0, nameBuilder.length() - 1) : "";
        String type = typeBuilder.length() > 0 ? typeBuilder.substring(0, typeBuilder.length() - 1) : "";
        return new String[]{name, type};
    }

    public void reset() {
        List<NormalParamEntity> datas = getDatas();
        boolean hasChoose = false;
        for (NormalParamEntity item : datas) {
            if (!TextUtils.isEmpty(item.getType()) && item.isChoosed()) {
                hasChoose = true;
                break;
            }
        }
        setNoLimitChoose(!hasChoose);
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
