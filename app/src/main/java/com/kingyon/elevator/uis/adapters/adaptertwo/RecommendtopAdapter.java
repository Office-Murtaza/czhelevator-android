package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendTopEntity;
import com.leo.afbaselibrary.utils.GlideUtils;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class RecommendtopAdapter extends RecyclerView.Adapter<RecommendtopAdapter.ViewHolder> {

    Context context;
    ConentEntity<QueryRecommendTopEntity> conentEntity;
    public RecommendtopAdapter(Context context, ConentEntity<QueryRecommendTopEntity> conentEntity){
        this.context = context;
        this.conentEntity = conentEntity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_recommendtop,parent,false);

        return new RecommendtopAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder != null) {
            holder.tvTitle.setText(conentEntity.getContent().get(position).title);
            holder.tvName.setText(conentEntity.getContent().get(position).sourceName);
            holder.tvNumber.setText(conentEntity.getContent().get(position).readNum+"阅读");
            if (conentEntity.getContent().get(position).image==null){
                holder.ll_image.setVisibility(View.GONE);
            }else {
                GlideUtils.loadRoundCornersImage(context,conentEntity.getContent().get(position).image,holder.imageView,20);
            }

        }
    }

    @Override
    public int getItemCount() {
        return conentEntity.getContent().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle,tvName,tvNumber;
        ImageView imageView;
        LinearLayout ll_image;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            imageView = itemView.findViewById(R.id.img_topimg);
            ll_image = itemView.findViewById(R.id.ll_image);

        }
    }
}
