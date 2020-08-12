package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendTopEntity;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
import com.leo.afbaselibrary.utils.GlideUtils;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class RecommendtopAdapter extends RecyclerView.Adapter<RecommendtopAdapter.ViewHolder> {

    Context context;
    ConentEntity<QueryRecommendTopEntity> conentEntity;
    Parser mTagParser = new Parser();
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
            QueryRecommendTopEntity queryRecommendTopEntity = conentEntity.getContent().get(position);
            holder.tvTitle.setText(conentEntity.getContent().get(position).title.equals("") ? conentEntity.getContent().get(position).content:conentEntity.getContent().get(position).title);
            holder.tvName.setText(conentEntity.getContent().get(position).nickname);
            holder.tvNumber.setText(conentEntity.getContent().get(position).browseTimes+"阅读");
            if (conentEntity.getContent().get(position).videoCover==null){
                holder.ll_image.setVisibility(View.GONE);
            }else {
                GlideUtils.loadRoundCornersImage(context,conentEntity.getContent().get(position).videoCover,holder.imageView,20);
            }
            holder.ll_itme_root.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itmeonClick(queryRecommendTopEntity);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return conentEntity.getContent().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName,tvNumber;
        MentionTextView tvTitle;
        ImageView imageView;
        LinearLayout ll_image,ll_itme_root;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvName = itemView.findViewById(R.id.tv_name);
            tvNumber = itemView.findViewById(R.id.tv_number);
            imageView = itemView.findViewById(R.id.img_topimg);
            ll_image = itemView.findViewById(R.id.ll_image);
            ll_itme_root = itemView.findViewById(R.id.ll_itme_root);
            tvTitle.setMovementMethod(new LinkMovementMethod());
            tvTitle.setParserConverter(mTagParser);
        }
    }

    private void itmeonClick(QueryRecommendTopEntity queryRecommendEntity) {
        switch (queryRecommendEntity.type) {
            case "wsq":
                LogUtils.e(queryRecommendEntity.id);
                ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                        "contentId",queryRecommendEntity.id);
                break;
            case "video":
                LogUtils.e(queryRecommendEntity.videoHorizontalVertical);

                if (queryRecommendEntity.videoHorizontalVertical==0) {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS,
                            "contentId",queryRecommendEntity.id);
                }else if (queryRecommendEntity.videoHorizontalVertical==1){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS,
                            "contentId",queryRecommendEntity.id);
                }
                break;
            case "article":

                ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS,
                        "contentId",queryRecommendEntity.id);

                break;
            default:
        }

    }

}
