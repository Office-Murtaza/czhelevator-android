package com.kingyon.elevator.uis.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.leo.afbaselibrary.utils.GlideUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2017/10/31.
 * Email：lc824767150@163.com
 */

public class ImageShowAdapter extends BaseAdapterWithHF<Object> {
    protected int maxSize = 12;
    protected boolean showSize;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public boolean isShowSize() {
        return showSize;
    }

    public void setShowSize(boolean showSize) {
        this.showSize = showSize;
    }

    public ImageShowAdapter(Context context) {
        super(context);
    }

    @Override
    public int getFooterCount() {
        return 0;
    }

    @Override
    public int getItemCount() {
        return (datas.size() > maxSize ? maxSize : datas.size()) + getHeaderCount() + getFooterCount();
    }

    @Override
    public int getHeaderCount() {
        return 0;
    }

    @NonNull
    @Override
    public BaseAdapterWithHF.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(getLayoutResId(), parent, false);
        return new ViewHolder(view);
    }

    protected int getLayoutResId() {
        return R.layout.picture_selector_item;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        Object item = getItemData(position);
        GlideUtils.loadImage(context, item.toString(), holder.img);
        holder.tvName.setText(String.format("%sP", getItemRealCount()));
        holder.tvName.setVisibility((showSize && getItemRealCount() > maxSize && position == getItemCount() - 1) ? View.VISIBLE : View.GONE);
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        @BindView(R.id.img)
        ImageView img;
        @BindView(R.id.tv_name)
        TextView tvName;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
