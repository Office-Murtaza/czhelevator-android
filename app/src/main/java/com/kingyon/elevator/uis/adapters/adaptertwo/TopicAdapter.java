package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.czh.myversiontwo.R;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_TOPIC_DETAILS;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    Context context;
    int data;
    public TopicAdapter(Context context,int data){
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic,parent,false);

        return new TopicAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder!=null) {
            holder.img_topic_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ARouter.getInstance().build(ACTIVITY_MAIN2_TOPIC_DETAILS).navigation();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return data;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView img_topic_image;
        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_content);
            img_topic_image = itemView.findViewById(R.id.img_topic_image);
        }
    }
}
