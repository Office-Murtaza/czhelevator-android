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

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.HomeTopicConentEntity;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.leo.afbaselibrary.utils.GlideUtils;

import java.util.List;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_TOPIC_DETAILS;
import static com.kingyon.elevator.utils.utilstwo.HtmlUtil.delHTMLTag;

/**
 * Created By Admin  on 2020/4/15
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class TopicAdapter extends RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    Context context;
    List<HomeTopicConentEntity> conentEntity;

    public TopicAdapter(Context context){
        this.context = context;
    }
    public void addData(List<HomeTopicConentEntity> conentEntity){
        this.conentEntity = conentEntity;
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


//            holder.tv_title.setText(conentEntity.get(position).title);
//
//            holder.tv_title.setText(delHTMLTag(conentEntity.get(position).content)+"");
            if (conentEntity.get(position).latestContent==null) {
                holder.tv_conent.setText(conentEntity.get(position).content);
            }else {
                holder.tv_conent.setText(delHTMLTag(conentEntity.get(position).latestContent));
            }
            if (conentEntity.get(position).latestNickname==null) {
                holder.tv_nickname.setText(conentEntity.get(position).nickname);
            }else {
                holder.tv_nickname.setText(conentEntity.get(position).latestNickname);
            }
            if (conentEntity.get(position).latestPhoto==null) {
                GlideUtils.loadCircleImage(context, conentEntity.get(position).photo, holder.img_portrait);
            }else {
                GlideUtils.loadCircleImage(context, conentEntity.get(position).latestPhoto, holder.img_portrait);
            }
            if (conentEntity.get(position).image==null){
                holder.ll_imagw.setVisibility(View.GONE);
            }else {
                holder.ll_imagw.setVisibility(View.VISIBLE);
                if (conentEntity.get(position).latestImage==null) {
                    GlideUtils.loadRoundCornersImage(context, conentEntity.get(position).image, holder.img_topic_image, 20);
                }else {
                    List<Object> list = StringUtils.StringToList(conentEntity.get(position).latestImage);
                    LogUtils.e(list.toString());
                    GlideUtils.loadRoundCornersImage(context, list.get(0).toString(), holder.img_topic_image, 20);
                }

            }
            HomeTopicConentEntity homeTopicConentEntity = conentEntity.get(position);
            holder.ll_topic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogUtils.e(conentEntity.get(position).id);
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_TOPIC_DETAILS,"topicid",
                            String.valueOf(conentEntity.get(position).id));
//                    ARouter.getInstance().build(ACTIVITY_MAIN2_TOPIC_DETAILS)
//                            .withString("conentEntity", JsonUtils.beanToJson(homeTopicConentEntity))
//                            .navigation();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return conentEntity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title,tv_conent,tv_nickname;
        ImageView img_topic_image,img_portrait;
        LinearLayout ll_topic,ll_imagw;
        public ViewHolder(View itemView) {
            super(itemView);
            img_topic_image = itemView.findViewById(R.id.img_topic_image);
            ll_topic = itemView.findViewById(R.id.ll_topic);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_conent = itemView.findViewById(R.id.tv_conent);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            img_portrait = itemView.findViewById(R.id.img_portrait);
            ll_imagw = itemView.findViewById(R.id.ll_imagw);
        }
    }
}
