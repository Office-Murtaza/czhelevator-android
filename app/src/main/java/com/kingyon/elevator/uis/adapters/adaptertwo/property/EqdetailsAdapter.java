package com.kingyon.elevator.uis.adapters.adaptertwo.property;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.EquipmentDetailsRevenueEntiy;
import com.kingyon.elevator.utils.TimeUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/19
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class EqdetailsAdapter extends RecyclerView.Adapter<EqdetailsAdapter.ViewHolder> {

    public Context context;
    List<EquipmentDetailsRevenueEntiy> list;


    public EqdetailsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_property_money, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        EquipmentDetailsRevenueEntiy entiy = list.get(position);
        holder.tvMoney.setText(entiy.moneyActual+"");
        holder.tvTime.setText(TimeUtil.getAllTimeNoSecond(entiy.createTime));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void addData(List<EquipmentDetailsRevenueEntiy> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
