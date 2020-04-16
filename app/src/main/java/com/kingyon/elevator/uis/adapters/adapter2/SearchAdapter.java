package com.kingyon.elevator.uis.adapters.adapter2;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    Context context;
    public SearchAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search,parent,false);
        return new SearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder!=null){
            holder.tvrak.setText((position+1)+"");
            if (position<3){
                holder.tvrak.setTextColor(Color.parseColor( "#F53051" ));
                holder.imageView.setVisibility(View.VISIBLE);
            }else {
                holder.tvrak.setTextColor(Color.parseColor( "#F0BE59" ));
                holder.imageView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvrak,tvName;
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvrak = itemView.findViewById(R.id.tv_rank);
            imageView = itemView.findViewById(R.id.img_hot);


        }
    }
}
