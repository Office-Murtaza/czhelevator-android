package com.kingyon.elevator.uis.actiivty2.input.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.leo.afbaselibrary.utils.GlideUtils;

/**
 * @Created By Admin  on 2020/4/29
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHodler> {
    Context context;
    ConentEntity<AttenionUserEntiy> provideData;
    public UserAdapter(Context context, ConentEntity<AttenionUserEntiy> provideData){
        this.context = context;
        this.provideData = provideData;
    }

    // 利用接口 -> 给RecyclerView设置点击事件
    private ItemClickListener mItemClickListener ;
    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }

    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_user_attent,parent,false);
        return new ViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        if (holder!=null){
            holder.textView.setText(provideData.getContent().get(position).nickname);
            GlideUtils.loadImage(context,provideData.getContent().get(position).photo,holder.image_photo);
            // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
            if (mItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 这里利用回调来给RecyclerView设置点击事件
                        mItemClickListener.onItemClick(position);
                    }
                });
            }
        }

    }

    @Override
    public int getItemCount() {
        return provideData.getContent().size();
    }

    public class ViewHodler extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView image_photo;
        public ViewHodler(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_name);
            image_photo = itemView.findViewById(R.id.image_photo);
        }
    }
    public ConentEntity<AttenionUserEntiy> getData() {
        return this.provideData;
    }


}
