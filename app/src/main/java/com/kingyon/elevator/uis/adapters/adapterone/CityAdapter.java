package com.kingyon.elevator.uis.adapters.adapterone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.application.AppContent;
import com.kingyon.elevator.entities.AMapCityEntity;
import com.kingyon.elevator.entities.LocationEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.widgets.FullyGridLayoutManager;
import com.kingyon.elevator.utils.DealScrollRecyclerView;
import com.kingyon.elevator.utils.FormatUtils;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by GongLi on 2017/11/7.
 * Email：lc824767150@163.com
 */

public class CityAdapter extends BaseAdapterWithHF<AMapCityEntity> {
    private List<AMapCityEntity> hotCity = new ArrayList<>();

    public CityAdapter(Context context) {
        super(context);
    }

    public void refreshHot(List<? extends AMapCityEntity> datas) {
        hotCity.clear();
        if (datas != null) {
            hotCity.addAll(datas);
        } else {
            Logger.e("BaseAdapterWithHF ---> refreshDatas(List<T> datas) has null parameter");
        }
    }

    @Override
    public int getFooterCount() {
        return 0;
    }

    //    @Override
//    public int getHeaderCount() {
//        return (hotCity != null && hotCity.size() > 0) ? 1 : 0;
//    }
    @Override
    public int getHeaderCount() {
        return 1;
    }

    @NonNull
    @Override
    public BaseAdapterWithHF.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == TYPE_HEADER) {
            view = inflater.inflate(R.layout.gps_head, parent, false);
        } else {
            view = inflater.inflate(R.layout.gps_item, parent, false);
        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseAdapterWithHF.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        if (getItemViewType(position) == TYPE_HEADER) {
            LocationEntity location = AppContent.getInstance().getLocation();
            if (location != null) {
                holder.tvCurrent.setText("当前城市/位置");
                holder.llInfo.setVisibility(View.VISIBLE);
                holder.tvCity.setText(FormatUtils.getInstance().getCityName(location.getCity()));
                holder.tvLocation.setText(location.getName());
            } else {
                holder.tvCurrent.setText("正在定位...");
                holder.llInfo.setVisibility(View.GONE);
            }
//            HotCityAdapter hotCityAdapter = new HotCityAdapter(context);
//            hotCityAdapter.setOnItemClickListener(new OnItemClickListener<AMapCityEntity>() {
//                @Override
//                public void onItemClick(View view, int position, AMapCityEntity entity, BaseAdapterWithHF<AMapCityEntity> baseAdaper) {
//                    OnItemClickListener<AMapCityEntity> onItemClickListener = CityAdapter.this.getOnItemClickListener();
//                    if (onItemClickListener != null) {
//                        onItemClickListener.onItemClick(view, -100, entity, baseAdaper);
//                    }
//                }
//            });
//            DealScrollRecyclerView.getInstance().dealAdapter(hotCityAdapter, holder.rvHot, new FullyGridLayoutManager(context, 3));
//            hotCityAdapter.refreshDatas(hotCity);
        } else {
            AMapCityEntity item = getItemData(position);
            if (!TextUtils.equals(getIndex(position), getIndex(position - 1))) {
                holder.tvIndex.setVisibility(View.VISIBLE);
            } else {
                holder.tvIndex.setVisibility(View.GONE);
            }
            holder.tvIndex.setText(getIndex(position));
            holder.tvName.setText(item.getName());
        }
    }

    private String getIndex(int position) {
        AMapCityEntity item = getItemData(position);
        return item == null ? "#" : item.getAcronyms();
    }

    public class ViewHolder extends BaseAdapterWithHF.ViewHolder {
        TextView tvIndex;
        TextView tvName;
        RecyclerView rvHot;
        View vLine;

        TextView tvCurrent;
        TextView tvCity;
        TextView tvLocation;
        LinearLayout llInfo;

        ViewHolder(View view) {
            super(view);
            tvIndex = view.findViewById(R.id.tv_index);
            tvName = view.findViewById(R.id.tv_name);
            rvHot = view.findViewById(R.id.rv_hot);
            vLine = view.findViewById(R.id.v_line);
            tvCurrent = view.findViewById(R.id.tv_current);
            tvCity = view.findViewById(R.id.tv_city);
            tvLocation = view.findViewById(R.id.tv_location);
            llInfo = view.findViewById(R.id.ll_info);
            if (tvName != null) {
                tvName.setOnClickListener(this);
            }

            if (tvCurrent != null) {
                tvCurrent.setOnClickListener(this);
            }
            if (tvCity != null) {
                tvCity.setOnClickListener(this);
            }
            if (tvLocation != null) {
                tvLocation.setOnClickListener(this);
            }
        }
    }
}
