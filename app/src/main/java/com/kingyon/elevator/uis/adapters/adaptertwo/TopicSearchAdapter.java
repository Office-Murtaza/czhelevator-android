package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryTopicEntity;
import com.leo.afbaselibrary.utils.GlideUtils;

/**
 * @Created By Admin  on 2020/4/20
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class TopicSearchAdapter extends RecyclerView.Adapter<TopicSearchAdapter.ViewHolder> {
    Context context;
    private ItemClickListener mItemClickListener ;
    ConentEntity<QueryTopicEntity.PageContentBean> pageContentBeanQueryTopicEntity;

    public TopicSearchAdapter(Context context, ConentEntity<QueryTopicEntity.PageContentBean> pageContentBeanQueryTopicEntity){
        this.context = context;
        this.pageContentBeanQueryTopicEntity = pageContentBeanQueryTopicEntity;
    }

    public interface ItemClickListener{
        public void onItemClick(int position) ;
    }
    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.mItemClickListener = itemClickListener ;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_topic_search,parent,false);

        return new TopicSearchAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (holder != null) {
            holder.tv_title.setText(pageContentBeanQueryTopicEntity.getContent().get(position).getTitle());
            holder.tv_peoplenum.setText(pageContentBeanQueryTopicEntity.getContent().get(position).getPeopleNum()+"");
            GlideUtils.loadImage(context,pageContentBeanQueryTopicEntity.getContent().get(position).getImage() , holder.imageView);

            // 点击事件一般都写在绑定数据这里，当然写到上边的创建布局时候也是可以的
            if (mItemClickListener != null) {
                holder.tv_title.setOnClickListener(new View.OnClickListener() {
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
        return pageContentBeanQueryTopicEntity.getContent().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tv_title,tv_peoplenum;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_hot);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_peoplenum = itemView.findViewById(R.id.tv_peoplenum);
        }
    }
}
