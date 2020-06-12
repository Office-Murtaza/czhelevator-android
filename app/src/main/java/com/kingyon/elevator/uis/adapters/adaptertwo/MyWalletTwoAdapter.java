package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.MyWalletInfo;
import com.kingyon.elevator.entities.WalletRecordEntity;
import com.kingyon.elevator.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/12
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MyWalletTwoAdapter extends RecyclerView.Adapter<MyWalletTwoAdapter.ViewHoder> {
    Context context;
    String type;
    List<WalletRecordEntity> recommendEntityList = new ArrayList<>();
    public MyWalletTwoAdapter(Context context,String type) {
        this.context = context;
        this.type = type;
    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_my_wallwttwo, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        WalletRecordEntity wallet = recommendEntityList.get(position);
        if (type.equals("pay_log")){
            holder.tvNumber.setText(wallet.getRemarks());
            holder.tvMoney.setTextColor(Color.parseColor("#333333"));
        }else {
            holder.tvNumber.setText(wallet.getName()+"");
            holder.tvMoney.setTextColor(Color.parseColor("#FF3049"));
        }
        holder.tvMoney.setText(wallet.getAmount()+"");
        holder.tvTime.setText(TimeUtil.getAllTimeNoSecond(wallet.getTime())+"");
        switch (wallet.getPayType()){
            case "BALANCE":
                holder.tvPayType.setText("T币支付");
                break;
            case "WECHAT":
                holder.tvPayType.setText("微信支付");
                break;
            case "ALI":
                holder.tvPayType.setText("支付宝支付");
                break;
        }

    }

    @Override
    public int getItemCount() {
        return recommendEntityList.size();
    }

    public void addData(List<WalletRecordEntity> recommendEntityList) {
        this.recommendEntityList = recommendEntityList;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_pay_type)
        TextView tvPayType;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        public ViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
