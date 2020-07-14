package com.kingyon.elevator.uis.fragments.message;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.CommentLikesListEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.uis.fragments.BaseStateRefreshLoadingFragment;
import com.leo.afbaselibrary.utils.GlideUtils;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;

/**
 * @Created By Admin  on 2020/7/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息-点赞评论
 */
public class MessageCommentMeListFragment extends BaseStateRefreshLoadingFragment<CommentLikesListEntiy> {
    Parser mTagParser = new Parser();
    @Override
    public int getContentViewId() {
        return R.layout.fragment_messagr_comment_like;
    }
    @Override
    protected void dealLeackCanary() {

    }
    @Override
    protected MultiItemTypeAdapter<CommentLikesListEntiy> getAdapter() {
        return new BaseAdapter<CommentLikesListEntiy>(getActivity(),R.layout.itme_message_comment_melist,mItems) {
            @Override
            protected void convert(CommonHolder holder, CommentLikesListEntiy item, int position) {
                MentionTextView mentionTextView = new MentionTextView(getActivity());
                mentionTextView = holder.getView(R.id.tv_content);
                mentionTextView.setMovementMethod(new LinkMovementMethod());
                mentionTextView.setParserConverter(mTagParser);
                holder.setText(R.id.tv_comment,item.comment);
                holder.setText(R.id.tv_content,item.content);
                holder.setText(R.id.tv_nickname,item.commentNickName);
                holder.setText(R.id.tv_time,item.createTime);
                GlideUtils.loadCircleImage(getActivity(),item.commentPhoto,holder.getView(R.id.img_portrait));
                if (item.isRead==1){
                    holder.setVisible(R.id.img_is,false);
                }else {
                    holder.setVisible(R.id.img_is,true);
                }
                switch (item.contentType){
                    case "wsq":
                        /*社区*/
                        holder.setVisible(R.id.img_image,false);
                        holder.setVisible(R.id.img_image1,true);
                        holder.setVisible(R.id.tv_title,false);
                        holder.setVisible(R.id.tv_content,true);
                        GlideUtils.loadRoundCornersImage(getActivity(),item.image,holder.getView(R.id.img_image1),20);
                        break;
                    case "article":
                        /*文章*/
                    case "video":
                        /*视频*/
                        holder.setVisible(R.id.img_image,true);
                        holder.setVisible(R.id.img_image1,false);
                        holder.setVisible(R.id.tv_title,true);
                        holder.setVisible(R.id.tv_content,true);
                        GlideUtils.loadRoundCornersImage(getActivity(),item.video_cover,holder.getView(R.id.img_image),20);
                        break;
                }




            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CommentLikesListEntiy item, int position) {
        super.onItemClick(view, holder, item, position);
        ConentUtils.httpGetMarkRead(String.valueOf(item.id), "COMMENT", "SINGLE", new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                LogUtils.e(success);
            }
        });
        switch (item.contentType){
            case "wsq":
                /*社区*/
                LogUtils.e(item.contentId);
                ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                        "contentId",item.contentId);
                break;
            case "article":
                /*文章*/
                LogUtils.e(item.contentId);
                ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS, "contentId", item.contentId);
                break;
            case "video":
                /*视频*/
                LogUtils.e(item.contentId);
                if (item.videoHorizontalVertical==0){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS, "contentId", item.contentId);
                } else {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS, "contentId", item.contentId);
                }
                break;
        }
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().getCommentMeList(page, 20)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CommentLikesListEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<CommentLikesListEntiy> contentLikesListEntiyConentEntity) {
                        if (contentLikesListEntiyConentEntity == null || contentLikesListEntiyConentEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(contentLikesListEntiyConentEntity.getContent());
                        if (page > 1 && contentLikesListEntiyConentEntity.getContent().size() <= 0) {
                            showToast("已经没有了");
                        }
                        loadingComplete(true, contentLikesListEntiyConentEntity.getTotalPages());
                    }
                });
    }
}
