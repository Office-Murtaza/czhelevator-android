package com.kingyon.elevator.uis.fragments.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.ContentLikesListEntiy;
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
 * @Instructions:
 */
public class MessageLikeContentFragment extends BaseStateRefreshLoadingFragment<ContentLikesListEntiy> {
    Parser mTagParser = new Parser();
    @Override
    public int getContentViewId() {
        return R.layout.fragment_messagr_like;
    }

    @Override
    protected void dealLeackCanary() {

    }
    @Override
    protected MultiItemTypeAdapter<ContentLikesListEntiy> getAdapter() {
        return new BaseAdapter<ContentLikesListEntiy>(getActivity(),R.layout.itme_message_content_like,mItems) {
            @Override
            protected void convert(CommonHolder holder, ContentLikesListEntiy item, int position) {
                MentionTextView mentionTextView = new MentionTextView(getActivity());
                mentionTextView = holder.getView(R.id.tv_content);
                mentionTextView.setMovementMethod(new LinkMovementMethod());
                mentionTextView.setParserConverter(mTagParser);
                holder.setText(R.id.tv_content,item.content+"");
                holder.setText(R.id.tv_title,item.title+"");
                holder.setText(R.id.tv_nickname,item.nickname+"");
                holder.setText(R.id.tv_time,item.createTime+"");
                GlideUtils.loadCircleImage(getActivity(),item.photo,holder.getView(R.id.img_portrait));
                if (item.isRead==1){
                    holder.setVisible(R.id.img_is,false);
                }else {
                    holder.setVisible(R.id.img_is,true);
                }
                switch (item.type){
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
                        GlideUtils.loadRoundCornersImage(getActivity(),item.videoCover,holder.getView(R.id.img_image),20);
                        break;
                }
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, ContentLikesListEntiy item, int position) {
        super.onItemClick(view, holder, item, position);

        switch (item.type) {
            case "wsq":
                /*社区*/
                ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                        "contentId",item.objId);
                break;
            case "article":
                /*文章*/
                ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS, "contentId", item.objId);
                break;
            case "video":
                /*视频*/
                if (item.videoHorizontalVertical==0){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS, "contentId", item.objId);
                } else {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS, "contentId", item.objId);
                }
                break;
        }
    }


    @Override
    protected void loadData(int page) {

        NetService.getInstance().getContentLikesList(page, 20)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<ContentLikesListEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<ContentLikesListEntiy> contentLikesListEntiyConentEntity) {
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
        ConentUtils.httpGetMarkRead("", "LIKES", "ALL", new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                LogUtils.e(success);
            }
        });
    }
}
