package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.R;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.utils.utilstwo.JsonUtils;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class ImagAdapter extends RecyclerView.Adapter<ImagAdapter.ViewHolder> {
    Context context;
    List<Object> list;
    QueryRecommendEntity queryRecommendEntity;
    public ImagAdapter(Context context,  List<Object> list,  QueryRecommendEntity queryRecommendEntity ){
        this.context = context;
        this.list = list;
        this.queryRecommendEntity = queryRecommendEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image,parent,false);

        return new ImagAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GlideUtils.loadRoundCornersImage(context, String.valueOf(list.get(position)),holder.imageView,20);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogUtils.e(queryRecommendEntity.id);
                ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                        "contentId",queryRecommendEntity.id);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
        }
    }
}
