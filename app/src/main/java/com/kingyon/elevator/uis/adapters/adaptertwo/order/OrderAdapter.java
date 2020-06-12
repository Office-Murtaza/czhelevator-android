package com.kingyon.elevator.uis.adapters.adaptertwo.order;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;

/**
 * @Created By Admin  on 2020/6/11
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    Context context;

    public OrderAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_order_lits,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.ViewHolder holder, int position) {
        switch (position){
            case 1:
                holder.img_status.setVisibility(View.VISIBLE);
                holder.img_status.setImageResource(R.mipmap.im_order_audit_fail);
                holder.tv_status.setText("待发布");
                break;
            case 2:
                holder.tv_status.setText("审核中");
                break;
            case 3:
                holder.tv_status.setText("已完成");
                holder.tv_again.setVisibility(View.VISIBLE);
                break;
            case 4:
                holder.tv_status.setText("发布中");
                break;
            case 5:
                holder.img_status.setVisibility(View.VISIBLE);
                holder.img_status.setImageResource(R.mipmap.im_order_audit_wait);
                holder.tv_status.setText("待发布");
                break;
                default:

        }

    }

    @Override
    public int getItemCount() {
        return 15;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_again,tv_number,tv_type,tv_point,tv_title,tv_order_time,tv_status;
        ImageView img_status,img_img;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_again = itemView.findViewById(R.id.tv_status);
            tv_number = itemView.findViewById(R.id.tv_number);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_point = itemView.findViewById(R.id.tv_point);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_order_time = itemView.findViewById(R.id.tv_order_time);
            tv_status = itemView.findViewById(R.id.tv_status);
            img_status = itemView.findViewById(R.id.img_status);
            img_img = itemView.findViewById(R.id.img_img);
        }
    }
}
