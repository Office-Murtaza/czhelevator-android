package com.kingyon.elevator.uis.pops;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.entities.NormalParamEntity;
import com.kingyon.elevator.uis.adapters.BaseAdapterWithHF;
import com.kingyon.elevator.uis.adapters.adapterone.OrderStatusAdaper;
import com.kingyon.elevator.utils.GridSpacingItemDecoration;
import com.leo.afbaselibrary.utils.ScreenUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by GongLi on 2019/1/14.
 * Email：lc824767150@163.com
 */

public class IncomeFilterWindow extends PopupWindow {
    @BindView(R.id.rv_filters)
    RecyclerView rvFilters;

    private OrderStatusAdaper statusAdapter;
    private OnFilterChangeListener onFilterChangeListener;

    public IncomeFilterWindow(Context context, final OnFilterChangeListener onFilterChangeListener) {
        super(context);
        this.onFilterChangeListener = onFilterChangeListener;
        View view = LayoutInflater.from(context).inflate(R.layout.window_income_filter, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        rvFilters.setLayoutManager(new GridLayoutManager(context, 3));
        rvFilters.addItemDecoration(new GridSpacingItemDecoration(3, ScreenUtil.dp2px(48), false));
        statusAdapter = new OrderStatusAdaper(context);
        rvFilters.setAdapter(statusAdapter);
        List<NormalParamEntity> entities = new ArrayList<>();
        entities.add(new NormalParamEntity(Constants.INCOME_FILTER.DAY, "日"));
        entities.add(new NormalParamEntity(Constants.INCOME_FILTER.MONTH, "月"));
        entities.add(new NormalParamEntity(Constants.INCOME_FILTER.YEAR, "年"));
        statusAdapter.refreshDatas(entities);
        statusAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<NormalParamEntity>() {
            @Override
            public void onItemClick(View view, int position, NormalParamEntity entity, BaseAdapterWithHF<NormalParamEntity> baseAdaper) {
                if (entity != null) {
                    for (NormalParamEntity item : baseAdaper.getDatas()) {
                        item.setChoosed(item == entity);
                    }
                    if (onFilterChangeListener != null) {
                        onFilterChangeListener.onFilterChage(entity);
                    }
                    baseAdaper.notifyDataSetChanged();
                    dismiss();
                }
            }
        });
    }

    public void showAsDropDown(View view, String status) {
        showAsDropDown(view);
        if (statusAdapter != null) {
            for (NormalParamEntity item : statusAdapter.getDatas()) {
                item.setChoosed(TextUtils.equals(status, item.getType()));
            }
            statusAdapter.notifyDataSetChanged();
        }
    }

    public interface OnFilterChangeListener {
        void onFilterChage(NormalParamEntity entity);
    }
}
