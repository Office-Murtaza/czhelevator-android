package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AdZoneEntiy;
import com.kingyon.elevator.util.UIUtil;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.zhihu.matisse.internal.ui.widget.RoundedRectangleImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;

/**
 * @Created By Admin  on 2020/6/9
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AdvertisingSectionAdapter extends RecyclerView.Adapter<AdvertisingSectionAdapter.ViewHolder> {
    Context context;
    List<AdZoneEntiy.DataBean> list;



    public AdvertisingSectionAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.itme_advertising_section, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AdZoneEntiy.DataBean queryRecommendEntity = list.get(position);
        holder.tvOnename.setText(list.get(position).getCreateAccountName());
        holder.tvTwoname.setText(list.get(position).getCreateAccountName());
        GlideUtils.loadAvatarImage(context, list.get(position).getPhoto(), holder.imgOnetx);
        GlideUtils.loadAvatarImage(context, list.get(position).getPhoto(), holder.imgTwotx);
        GlideUtils.loadRoundCornersImage(context,list.get(position).getVideoCover(),holder.img_videoCover,30);
        holder.tvHot.setVisibility(View.GONE);
        holder.tvTitle.setText(list.get(position).getTitle());
        if (list.get(position).getIsOriginal()==0){
            holder.tvIsoriginal.setText("原创");
        }else {
            holder.tvIsoriginal.setText("转载");
        }
        holder.tvOneZpb.setText(list.get(position).getLikeNum()+" 点赞    "+list.get(position).getCommentNum()+" 评论    "+list.get(position).getBrowseTimes()+"播放");
        holder.tvTwoZpb.setText(list.get(position).getLikeNum()+" 点赞    "+list.get(position).getCommentNum()+" 评论    "+list.get(position).getBrowseTimes()+"播放");
        if (position % 3 == 0) {
            holder.llOne.setVisibility(View.VISIBLE);
            holder.llTwo.setVisibility(View.GONE);
            RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) holder.img_videoCover.getLayoutParams();
            //取控件textView当前的布局参数
            linearParams.height = UIUtil.dip2px(context,180);// 控件的高强制设成20
            holder.img_videoCover.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        } else {
            holder.llOne.setVisibility(View.GONE);
            holder.llTwo.setVisibility(View.VISIBLE);
            RelativeLayout.LayoutParams linearParams =(RelativeLayout.LayoutParams) holder.img_videoCover.getLayoutParams();
            //取控件textView当前的布局参数
            linearParams.height = UIUtil.dip2px(context,240);// 控件的高强制设成20
            holder.img_videoCover.setLayoutParams(linearParams); //使设置好的布局参数应用到控件
        }
        holder.img_videoCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryRecommendEntity.getVideoHorizontalVertical()==0) {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS,
                            "contentId",queryRecommendEntity.getId());
                }else if (queryRecommendEntity.getVideoHorizontalVertical()==1){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS,
                            "contentId",queryRecommendEntity.getId());
                }
            }
        });
        holder.img_videoCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryRecommendEntity.getVideoHorizontalVertical()==0) {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS,
                            "contentId",queryRecommendEntity.getId());
                }else if (queryRecommendEntity.getVideoHorizontalVertical()==1){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS,
                            "contentId",queryRecommendEntity.getId());
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void data(List<AdZoneEntiy.DataBean> list) {
        this.list = list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.img_onetx)
        ImageView imgOnetx;
        @BindView(R.id.tv_onename)
        TextView tvOnename;
        @BindView(R.id.tv_one_zpb)
        TextView tvOneZpb;
        @BindView(R.id.ll_one)
        LinearLayout llOne;
        @BindView(R.id.img_twotx)
        ImageView imgTwotx;
        @BindView(R.id.tv_twoname)
        TextView tvTwoname;
        @BindView(R.id.tv_two_zpb)
        TextView tvTwoZpb;
        @BindView(R.id.ll_two)
        LinearLayout llTwo;
        @BindView(R.id.tv_isoriginal)
        TextView tvIsoriginal;
        @BindView(R.id.img_videoCover)
        ImageView img_videoCover;
        @BindView(R.id.tv_hot)
        TextView tvHot;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
