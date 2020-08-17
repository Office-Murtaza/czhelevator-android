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
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
import com.kingyon.elevator.uis.dialogs.DeleteShareDialog;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.JsonUtils;
import com.kingyon.elevator.utils.utilstwo.SharedUtils;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.kingyon.elevator.utils.utilstwo.TokenUtils;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.zhaoss.weixinrecorded.util.TimeUtils;

import java.util.List;

import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;
import static com.kingyon.elevator.uis.fragments.main2.found.AttentionFragment.isRefresh;
import static com.kingyon.elevator.utils.utilstwo.TokenUtils.isToken;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.ViewHolder>{
    BaseActivity context;

    List<QueryRecommendEntity> conentEntity;
    Parser mTagParser = new Parser();
    private ShareDialog shareDialog;
    private int  likes = 0;
    public AttentionAdapter(BaseActivity context){

      this.context = context;
    }
    public void addData(List<QueryRecommendEntity> conentEntity){
        this.conentEntity = conentEntity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_attention,parent,false);

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
        holder.tv_like_number.setText(StringUtils.getNumStr(queryRecommendEntity.likes ,"点赞"));
        holder.tv_comments_number.setText(StringUtils.getNumStr(queryRecommendEntity.comments ,"评论"));
        holder.tv_search.setText(StringUtils.getNumStr(queryRecommendEntity.shares ,"分享"));

        holder.tv_like_number_bottm.setText("等"+queryRecommendEntity.likes+"人觉得很赞");
        holder.tv_time.setText(TimeUtil.getRecentlyTime(queryRecommendEntity.createTime));


        holder.tv_title.setClickable(false);
        holder.ll_conent_img.setClickable(false);

        GlideUtils.loadCircleImage(context, queryRecommendEntity.photo, holder.img_tx);

        if (queryRecommendEntity.content==null){
            holder.tv_title.setVisibility(View.GONE);
        }
        holder.tv_title.setMovementMethod(ConentUtils.CustomMovementMethod.getInstance());
        if (queryRecommendEntity.likes<=0||!queryRecommendEntity.liked){
            holder.ll_like.setVisibility(View.GONE);
        }else {
            holder.ll_like.setVisibility(View.VISIBLE);
            holder.tv_like_number_bottm.setText(String.format("等%s人觉得很赞",queryRecommendEntity.likes));
            GlideUtils.loadCircleImage(context, queryRecommendEntity.likesItem.get(0).photo, holder.img_topimg);
        }
        if (queryRecommendEntity.liked){
            holder.img_like.setImageResource(R.mipmap.ic_small_like);
        }else {
            holder.img_like.setImageResource(R.mipmap.ic_small_like_off);
        }

        if (queryRecommendEntity.uAuthStatus==1){
            holder.tv_authstatus.setVisibility(View.VISIBLE);
        }else {
            holder.tv_authstatus.setVisibility(View.GONE);
        }
        if (queryRecommendEntity.hasMedal){
            holder.img_talent.setVisibility(View.VISIBLE);
        }else {
            holder.img_talent.setVisibility(View.GONE);
        }

        switch (queryRecommendEntity.type){
            case "video":
//                视频
                RoundImageView imageView2 = new RoundImageView(context);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
                GlideUtils.loadRoundCornersImage(context,queryRecommendEntity.videoCover,imageView2,20);
                imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP );
                holder.ll_conent_img.addView(imageView2,params);
                holder.ll_xssjcs.setVisibility(View.VISIBLE);
                holder.tv_video_number.setText(queryRecommendEntity.browseTimes+"次播放");
                holder.tv_video_time.setText(TimeUtils.secondToTime(queryRecommendEntity.playTime/1000)+"");
                holder.tv_title.setText(queryRecommendEntity.title);
                break;
            case "wsq":
//               社区
                if (queryRecommendEntity.image!=null) {
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
            case "article":
                if (queryRecommendEntity.videoCover==null){
                    holder.ll_conent_img.setVisibility(View.GONE);
                    holder.ll_xssjcs.setVisibility(View.GONE);
                    holder.tv_video_number.setText(queryRecommendEntity.browseTimes+"次阅读");
                    holder.tv_video_time.setText("文章");
                    holder.tv_title.setText(queryRecommendEntity.title);
                }else {
                    if (queryRecommendEntity.videoCover.equals("")){
                        holder.ll_conent_img.setVisibility(View.GONE);
                        holder.ll_xssjcs.setVisibility(View.GONE);
                        holder.tv_video_number.setText(queryRecommendEntity.browseTimes+"次阅读");
                        holder.tv_video_time.setText("文章");
                        holder.tv_title.setText(queryRecommendEntity.title);
                    }else {
                        RoundImageView imageView3 = new RoundImageView(context);
                        LinearLayout.LayoutParams params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
                        GlideUtils.loadRoundCornersImage(context,queryRecommendEntity.videoCover,imageView3,20);
                        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP );
                        holder.ll_conent_img.addView(imageView3,params1);
                        holder.ll_xssjcs.setVisibility(View.VISIBLE);
                        holder.tv_video_number.setText(queryRecommendEntity.browseTimes+"次阅读");
                        holder.tv_video_time.setText("文章");
                        holder.tv_title.setText(queryRecommendEntity.title);
                    }

                }
                break;
                default:

        }

        onClick(holder,queryRecommendEntity,position);
    }

    private void onClick(ViewHolder holder, QueryRecommendEntity queryRecommendEntity, int position) {
        /*进入个人资料*/
        holder.img_tx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isToken(context)) {
                    ActivityUtils.setActivity(ACTIVITY_USER_CENTER, "type", "1","otherUserAccount",queryRecommendEntity.createAccount);
                } else {
                    ActivityUtils.setLoginActivity();
                }
            }
        });
        holder.tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isToken(context)) {
                    ActivityUtils.setActivity(ACTIVITY_USER_CENTER, "type", "1","otherUserAccount",queryRecommendEntity.createAccount);
                } else {
                    ActivityUtils.setLoginActivity();
                }
            }
        });


        /*进入详情*/
        holder.ll_itme_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itmeonClick(queryRecommendEntity);
            }
        });
        holder.tv_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itmeonClick(queryRecommendEntity);
            }
        });
        holder.ll_conent_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itmeonClick(queryRecommendEntity);
            }
        });


        /*点赞*/
        holder.img_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TokenUtils.isToken(context)) {
                    likes = queryRecommendEntity.likes;
                    if (queryRecommendEntity.liked) {
                        queryRecommendEntity.liked = false;
                        holder.img_like.setImageResource(R.mipmap.ic_small_like_off);
                        ConentUtils.httpHandlerLikeOrNot(context, queryRecommendEntity.id,
                                HOME_CONTENT, CANCEL_LIKE, new IsSuccess() {
                                    @Override
                                    public void isSuccess(boolean success) {

                                    }
                                });
                        likes--;
                        queryRecommendEntity.likes = likes;
                        LogUtils.e(likes + "");
                        holder.tv_like_number.setText(likes + "");
                    } else {
                        queryRecommendEntity.liked = true;
                        holder.img_like.setImageResource(R.mipmap.ic_small_like);
                        ConentUtils.httpHandlerLikeOrNot(context, queryRecommendEntity.id,
                                HOME_CONTENT, LIKE, new IsSuccess() {
                                    @Override
                                    public void isSuccess(boolean success) {

                                    }
                                });
                        likes++;
                        queryRecommendEntity.likes = likes;
                        LogUtils.e(likes + "");
                        holder.tv_like_number.setText(likes + "");
                    }
                }else {
                    ActivityUtils.setLoginActivity();
                }
            }
        });

        /*分享*/
        holder.img_shared.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedUtils.shared(context,"内容","http://www.gzonehr.cn/","标题");
                if (queryRecommendEntity.videoHorizontalVertical==1) {
                    SharedUtils.shared(context, shareDialog, queryRecommendEntity.content, queryRecommendEntity.video, queryRecommendEntity.title, true);
                }else {
                    SharedUtils.shared(context, shareDialog, queryRecommendEntity.content, "www.baidu.com", queryRecommendEntity.title, false);

                }
            }
        });

        /*举报及删除*/
        holder.img_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TokenUtils.isToken(context)){
                    LogUtils.e(DataSharedPreferences.getCreatateAccount(),queryRecommendEntity.createAccount);
                    if (TokenUtils.isCreateAccount(queryRecommendEntity.createAccount)){
                        /*删除*/
                        DeleteShareDialog deleteShareDialog = new DeleteShareDialog(context,queryRecommendEntity.id,
                                AttentionAdapter.this,"1",position,conentEntity,null);
                        deleteShareDialog.show();
                    }else {
                        /*举报*/
                        ReportShareDialog reportShareDialog = new ReportShareDialog(context,queryRecommendEntity.id,HOME_CONTENT,"");
                        reportShareDialog.show();
                    }
                }else {
                    ActivityUtils.setLoginActivity();
                }
            }
        });

    }

    private void itmeonClick(QueryRecommendEntity queryRecommendEntity) {
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return conentEntity.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout ll_conent_img,ll_xssjcs,ll_like,ll_itme_root;
        RelativeLayout rv_conent_img;
        MentionTextView tv_title;
        ImageView img_tx,img_topimg,img_like,img_talent,img_shared,img_report;
        TextView tv_like_number_bottm,tv_time,tv_like_number,tv_video_time,tv_video_number,tv_name,tv_authstatus,tv_search,tv_comments_number;
        public ViewHolder(View itemView) {
            super(itemView);
            ll_conent_img = itemView.findViewById(R.id.ll_conent_img);
            tv_title = itemView.findViewById(R.id.tv_title);
            rv_conent_img = itemView.findViewById(R.id.rv_conent_img);
            ll_xssjcs = itemView.findViewById(R.id.ll_xssjcs);
            img_tx = itemView.findViewById(R.id.img_tx);
            img_topimg = itemView.findViewById(R.id.img_topimg);
            img_like = itemView.findViewById(R.id.img_like);
            img_talent = itemView.findViewById(R.id.img_talent);
            tv_like_number_bottm = itemView.findViewById(R.id.tv_like_number_bottm);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_like_number = itemView.findViewById(R.id.tv_like_number);
            tv_video_time = itemView.findViewById(R.id.tv_video_time);
            tv_video_number = itemView.findViewById(R.id.tv_video_number);
            tv_name = itemView.findViewById(R.id.tv_name);
            ll_like = itemView.findViewById(R.id.ll_like);
            ll_itme_root = itemView.findViewById(R.id.ll_itme_root);
            img_report = itemView.findViewById(R.id.img_report);
            img_shared = itemView.findViewById(R.id.img_shared);
            tv_authstatus = itemView.findViewById(R.id.tv_authstatus);
            tv_search = itemView.findViewById(R.id.tv_search);
            tv_comments_number = itemView.findViewById(R.id.tv_comments_number);
            tv_title.setMovementMethod(new LinkMovementMethod());
            tv_title.setParserConverter(mTagParser);
        }
    }
}
