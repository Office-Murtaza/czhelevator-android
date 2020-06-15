package com.kingyon.elevator.uis.adapters.adaptertwo.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.czh.myversiontwo.view.RoundImageView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.OrderCommunityEntiy;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Created By Admin  on 2020/6/15
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderCommunityAdapter extends RecyclerView.Adapter<OrderCommunityAdapter.ViewHoder> {


    private int size;
    private List<OrderCommunityEntiy> list;
    private Context context;

    public OrderCommunityAdapter(Context context, List<OrderCommunityEntiy> list, int size) {
        this.context = context;
        this.list = list;
        this.size = size;

    }

    @NonNull
    @Override
    public ViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_order_community_list, parent, false);
        return new ViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoder holder, int position) {
        OrderCommunityEntiy order = list.get(position);
        if (order.housePic!=null) {
            GlideUtils.loadImage(context, order.housePic, holder.imgImg);
        }
        holder.tvMoney.setText("￥"+order.money+"");
        holder.tvNumber.setText(order.deviceNum+"面屏");
        holder.tvTitle.setText(order.houseName+"");
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class ViewHoder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_img)
        RoundImageView imgImg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_number)
        TextView tvNumber;
        @BindView(R.id.tv_money)
        TextView tvMoney;
        public ViewHoder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
