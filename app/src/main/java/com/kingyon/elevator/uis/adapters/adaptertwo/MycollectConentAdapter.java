package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.view.RoundImageView;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.JsonUtils;
import com.kingyon.elevator.utils.utilstwo.SharedUtils;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.zhaoss.weixinrecorded.util.TimeUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class MycollectConentAdapter extends RecyclerView.Adapter<MycollectConentAdapter.ViewHolder> {

    BaseActivity context;
    List<QueryRecommendEntity> conentEntity;
    Parser mTagParser = new Parser();

    private ShareDialog shareDialog;

    public MycollectConentAdapter(BaseActivity context) {
        this.context = context;
    }

    public void addData(List<QueryRecommendEntity> conentEntity) {
        this.conentEntity = conentEntity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.itme_my_collect_conent, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        /**
         * 1文字
         * 2图片
         * 3视频
         * 4文字+图片
         * 5文字+视频
         * */
        QueryRecommendEntity queryRecommendEntity = conentEntity.get(position);

        holder.tv_name.setText(queryRecommendEntity.nickname);
        holder.tv_like_number.setText(queryRecommendEntity.likes + "");
        holder.tv_like_number_bottm.setText("等" + queryRecommendEntity.likes + "人觉得很赞");
        holder.tv_time.setText(TimeUtil.getRecentlyTime(queryRecommendEntity.createTime));


        holder.tv_title.setClickable(false);
        holder.ll_conent_img.setClickable(false);

        GlideUtils.loadRoundImage(context, queryRecommendEntity.photo, holder.img_tx, 20);

        if (queryRecommendEntity.content == null) {
            holder.tv_title.setVisibility(View.GONE);
        }
        if (queryRecommendEntity.likes < 100) {
            holder.ll_like.setVisibility(View.GONE);
        } else {
            holder.ll_like.setVisibility(View.VISIBLE);
        }
        if (queryRecommendEntity.liked) {
            holder.img_like.setImageResource(R.mipmap.ic_small_like);
        } else {
            holder.img_like.setImageResource(R.mipmap.ic_small_like_off);
        }

        if (queryRecommendEntity.uAuthStatus == 1) {
            holder.tv_authstatus.setVisibility(View.VISIBLE);
        } else {
            holder.tv_authstatus.setVisibility(View.GONE);
        }
        if (queryRecommendEntity.hasMedal) {
            holder.img_talent.setVisibility(View.VISIBLE);
        } else {
            holder.img_talent.setVisibility(View.GONE);
        }

        switch (queryRecommendEntity.type) {
            case "wsq":
//               社区
                if (queryRecommendEntity.image != null) {
                    List<Object> list = StringUtils.StringToList(queryRecommendEntity.image);
                    RecyclerView recyclerView = new RecyclerView(context);
                    ImagAdapter imagAdapter = new ImagAdapter(context, list,queryRecommendEntity);
                    GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false);
                    gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(gridLayoutManager);
                    recyclerView.setAdapter(imagAdapter);
                    holder.ll_conent_img.addView(recyclerView);
                    holder.ll_xssjcs.setVisibility(View.GONE);
                    holder.tv_title.setText(queryRecommendEntity.content);
                }
                holder.tv_title.setText(queryRecommendEntity.content);
                break;
            case "video":
//                视频
                RoundImageView imageView2 = new RoundImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
                GlideUtils.loadRoundCornersImage(context, queryRecommendEntity.videoCover, imageView2, 20);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
                holder.ll_conent_img.addView(imageView2, params);
                holder.ll_xssjcs.setVisibility(View.VISIBLE);
                holder.tv_video_number.setText(queryRecommendEntity.browseTimes + "次播放");
                holder.tv_video_time.setText(TimeUtils.secondToTime(queryRecommendEntity.playTime / 1000) + "");
                holder.tv_title.setText(queryRecommendEntity.title);
                break;
            case "article":
                if (queryRecommendEntity.videoCover == null) {
                    holder.ll_conent_img.setVisibility(View.GONE);
                    holder.ll_xssjcs.setVisibility(View.GONE);
                    holder.tv_video_number.setText(queryRecommendEntity.browseTimes + "次阅读");
                    holder.tv_video_time.setText("文章");
                    holder.tv_title.setText(queryRecommendEntity.title);
                } else {
                    RoundImageView imageView3 = new RoundImageView(context);
                    LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
                    GlideUtils.loadRoundCornersImage(context, queryRecommendEntity.videoCover, imageView3, 20);
                    imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    holder.ll_conent_img.addView(imageView3, params1);
                    holder.ll_xssjcs.setVisibility(View.VISIBLE);
                    holder.tv_video_number.setText(queryRecommendEntity.browseTimes + "次阅读");
                    holder.tv_video_time.setText("文章");
                    holder.tv_title.setText(queryRecommendEntity.title);
                }
                break;
            default:

        }


        holder.ll_itme_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (queryRecommendEntity.type) {
                    case "wsq":
                        LogUtils.e(queryRecommendEntity.id);
                        ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                                "contentId",queryRecommendEntity.id);
                        break;
                    case "video":
                        LogUtils.e(queryRecommendEntity.videoHorizontalVertical);

                        if (queryRecommendEntity.videoHorizontalVertical == 0) {
                            ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS,
                                    "contentId",queryRecommendEntity.id);
                        } else if (queryRecommendEntity.videoHorizontalVertical == 1) {
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
        });
        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queryRecommendEntity.liked) {
                    queryRecommendEntity.liked =false;
                    holder.img_like.setImageResource(R.mipmap.ic_small_like_off);
                    ConentUtils.httpHandlerLikeOrNot(context, queryRecommendEntity.id,
                            HOME_CONTENT, CANCEL_LIKE, position, queryRecommendEntity, "1");
                } else {
                    queryRecommendEntity.liked =true;
                    holder.img_like.setImageResource(R.mipmap.ic_small_like);
                    ConentUtils.httpHandlerLikeOrNot(context, queryRecommendEntity.id,
                            HOME_CONTENT, LIKE, position, queryRecommendEntity, "1");
                }
            }
        });

        holder.img_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedUtils.shared(context,"内容","http://www.gzonehr.cn/","标题");
                SharedUtils.shared(context, shareDialog, queryRecommendEntity.content, "www.baidu.com", queryRecommendEntity.title);
            }
        });

        holder.tv_collect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConentUtils.httpCancelCollect(String.valueOf(queryRecommendEntity.id), new ConentUtils.AddCollect() {
                    @Override
                    public void Collect(boolean is) {
                        if (is) {
                            context.showToast("取消收藏成功");
                            conentEntity.remove(position);
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, conentEntity.size() - position);
                        }else {
                            context.showToast("取消收藏失败");
                        }
                    }
                });
            }
        });


    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return conentEntity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_tx)
        ImageView img_tx;
        @BindView(R.id.tv_authstatus)
        TextView tv_authstatus;
        @BindView(R.id.tv_name)
        TextView tv_name;
        @BindView(R.id.img_talent)
        ImageView img_talent;
        @BindView(R.id.tv_title)
        MentionTextView tv_title;
        @BindView(R.id.ll_conent_img)
        LinearLayout ll_conent_img;
        @BindView(R.id.tv_video_number)
        TextView tv_video_number;
        @BindView(R.id.tv_video_time)
        TextView tv_video_time;
        @BindView(R.id.ll_xssjcs)
        LinearLayout ll_xssjcs;
        @BindView(R.id.rv_conent_img)
        RelativeLayout rv_conent_img;
        @BindView(R.id.img_like)
        ImageView img_like;
        @BindView(R.id.tv_like_number)
        TextView tv_like_number;
        @BindView(R.id.img_shared)
        ImageView img_shared;
        @BindView(R.id.tv_time)
        TextView tv_time;
        @BindView(R.id.img_topimg)
        ImageView img_topimg;
        @BindView(R.id.tv_like_number_bottm)
        TextView tv_like_number_bottm;
        @BindView(R.id.ll_like)
        LinearLayout ll_like;
        @BindView(R.id.ll_itme_root)
        LinearLayout ll_itme_root;
        @BindView(R.id.tv_collect)
        TextView tv_collect;
        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            tv_title.setMovementMethod(new LinkMovementMethod());
            tv_title.setParserConverter(mTagParser);
        }
    }
}
