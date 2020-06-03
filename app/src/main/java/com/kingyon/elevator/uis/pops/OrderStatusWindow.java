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
import android.widget.TextView;

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
 * Created by GongLi on 2019/1/4.
 * Email：lc824767150@163.com
 */

public class OrderStatusWindow extends PopupWindow {
    @BindView(R.id.rv_status)
    RecyclerView rvStatus;

    private OnStatusChangeListener onStatusChangeListener;
    private OrderStatusAdaper statusAdapter;

    public OrderStatusWindow(Context context, final OnStatusChangeListener onStatusChangeListener) {
        super(context);
        this.onStatusChangeListener = onStatusChangeListener;
        View view = LayoutInflater.from(context).inflate(R.layout.window_order_status, null);
        ButterKnife.bind(this, view);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        rvStatus.setLayoutManager(new GridLayoutManager(context, 5));
        rvStatus.addItemDecoration(new GridSpacingItemDecoration(5, ScreenUtil.dp2px(8), true));
        statusAdapter = new OrderStatusAdaper(context);
        rvStatus.setAdapter(statusAdapter);
        List<NormalParamEntity> entities = new ArrayList<>();
        entities.add(new NormalParamEntity("", "全部订单"));
        entities.add(new NormalParamEntity(Constants.OrderStatus.WAIT_PAY, "待付款"));
        entities.add(new NormalParamEntity(Constants.OrderStatus.WAIT_RELEASE, "待发布"));
        entities.add(new NormalParamEntity(Constants.OrderStatus.RELEASEING, "发布中"));
        entities.add(new NormalParamEntity(Constants.OrderStatus.COMPLETE, "已完成"));
        statusAdapter.refreshDatas(entities);
        statusAdapter.setOnItemClickListener(new BaseAdapterWithHF.OnItemClickListener<NormalParamEntity>() {
            @Override
            public void onItemClick(View view, int position, NormalParamEntity entity, BaseAdapterWithHF<NormalParamEntity> baseAdaper) {
                if (entity != null) {
                    for (NormalParamEntity item : baseAdaper.getDatas()) {
                        item.setChoosed(item == entity);
                    }
                    if (onStatusChangeListener != null) {
                        onStatusChangeListener.onStatusChange(entity);
                    }
                    baseAdaper.notifyDataSetChanged();
                    dismiss();
                }
            }
        });
    }

    public void showAsDropDown(TextView preTvTitle, String status) {
        showAsDropDown(preTvTitle);
        if (statusAdapter != null) {
            for (NormalParamEntity item : statusAdapter.getDatas()) {
                item.setChoosed(TextUtils.equals(status, item.getType()));
            }
            statusAdapter.notifyDataSetChanged();
        }
    }

    public interface OnStatusChangeListener {
        void onStatusChange(NormalParamEntity entity);
    }
}
