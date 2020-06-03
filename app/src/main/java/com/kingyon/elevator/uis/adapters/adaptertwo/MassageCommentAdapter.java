package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
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
public class MassageCommentAdapter extends RecyclerView.Adapter<MassageCommentAdapter.ViewHolder> {

     Context context;
     int data;
    public MassageCommentAdapter(Context context,int data){
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_massage_comment,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (position%3){
            case 1:
                holder.tv_comment.setVisibility(View.VISIBLE);
                holder.ll_community.setVisibility(View.GONE);
                holder.ll_wz_video.setVisibility(View.GONE);
                break;
            case 2:
                holder.tv_comment.setVisibility(View.GONE);
                holder.ll_community.setVisibility(View.VISIBLE);
                holder.ll_wz_video.setVisibility(View.GONE);
                break;
            case 3:
                holder.tv_comment.setVisibility(View.GONE);
                holder.ll_community.setVisibility(View.GONE);
                holder.ll_wz_video.setVisibility(View.VISIBLE);
                break;
                default:
        }

    }

    @Override
    public int getItemCount() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_comment;
        LinearLayout ll_wz_video,ll_community;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            ll_wz_video = itemView.findViewById(R.id.ll_wz_video);
            ll_community = itemView.findViewById(R.id.ll_community);
        }
    }
}
