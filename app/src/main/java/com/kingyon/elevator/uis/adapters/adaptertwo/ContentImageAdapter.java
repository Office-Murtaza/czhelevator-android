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
import com.gerry.scaledelete.DeletedImageScanDialog;
import com.gerry.scaledelete.ScanleImageUrl;
import com.kingyon.elevator.entities.ImageScan;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class ContentImageAdapter extends RecyclerView.Adapter<ContentImageAdapter.ViewHolder> {
    Context context;
    List<Object> list;
    List<ScanleImageUrl> urls = new ArrayList<>();
    public ContentImageAdapter(Context context,List<Object> list){
        this.context = context;
        this.list = list;
        for (Object obj : list) {
            urls.add(new ImageScan(obj.toString()));
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content_image,parent,false);
        return new ContentImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LogUtils.e(list.get(position));
        GlideUtils.loadImage(context, String.valueOf(list.get(position)),holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletedImageScanDialog dialog = new DeletedImageScanDialog(context, urls, position, null);
                dialog.show(position, false, false);
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
            imageView = itemView.findViewById(R.id.img_content);
        }
    }
}
