package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kingyon.elevator.R;

/**
 * @Created By Admin  on 2020/6/1
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MessageAttentionAdapter extends RecyclerView.Adapter<MessageAttentionAdapter.ViewHolder> {
    Context context;
    int data;
    public MessageAttentionAdapter(Context context,int data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_message_atention,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            if (position%2==0){
                holder.tvAttention.setTextColor(Color.parseColor("#FF3049"));
                holder.tvAttention.setBackgroundResource(R.drawable.message_attention_bj);
            }else {
                holder.tvAttention.setTextColor(Color.parseColor("#ffffff"));
                holder.tvAttention.setBackgroundResource(R.drawable.message_attention_bj1);
            }
    }

    @Override
    public int getItemCount() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAttention;
        public ViewHolder(View itemView) {
            super(itemView);
            tvAttention = itemView.findViewById(R.id.tv_attention);
        }
    }
}
