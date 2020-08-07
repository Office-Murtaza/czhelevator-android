package com.kingyon.elevator.uis.fragments.message;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;

import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.CommentLikesListEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.ContentLikesListEntiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
import com.kingyon.elevator.uis.dialogs.DeleMassageDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.MassageUtils;
import com.kingyon.elevator.view.DialogOnClick;
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
public class MessageLikeCommentFragment extends BaseStateRefreshLoadingFragment<CommentLikesListEntiy> {
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
        return new BaseAdapter<CommentLikesListEntiy>(getActivity(),R.layout.itme_message_comment_like,mItems) {
            @Override
            protected void convert(CommonHolder holder, CommentLikesListEntiy item, int position) {
                MentionTextView mentionTextView = new MentionTextView(getActivity());
                mentionTextView = holder.getView(R.id.tv_content);
                mentionTextView.setMovementMethod(new LinkMovementMethod());
                mentionTextView.setParserConverter(mTagParser);
                GlideUtils.loadCircleImage(getActivity(),item.photo,holder.getView(R.id.img_portrait));
//                if (item.isRead==1){
                    holder.setVisible(R.id.img_is,false);
//                }else {
//                    holder.setVisible(R.id.img_is,true);
//                }
                holder.setText(R.id.tv_nickname,item.nickname);
                holder.setText(R.id.tv_time,item.createTime);
                holder.setText(R.id.tv_content,item.comment);


            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, CommentLikesListEntiy item, int position) {
        super.onItemClick(view, holder, item, position);
        ImageView imageView = view.findViewById(R.id.img_is);
        imageView.setVisibility(View.GONE);
        switch (item.contentType){
            case "wsq":
                /*社区*/
                ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                        "contentId",item.contentId);
                break;
            case "article":
                /*文章*/
                ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS, "contentId", item.contentId);
                break;
            case "video":
                /*视频*/
                if (item.videoHorizontalVertical==0){
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VIDEO_DRTAILS, "contentId", item.contentId);
                } else {
                    ActivityUtils.setActivity(ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS, "contentId", item.contentId);
                }
                break;
        }
    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, CommentLikesListEntiy item, int position) {
        DeleMassageDialog dialog = new DeleMassageDialog(getActivity());
        dialog.show();
        dialog.setDialogOnClick(new DialogOnClick() {
            @Override
            public void onClick() {
                showProgressDialog(getString(R.string.wait));
                dialog.dismiss();
                MassageUtils.httpremoveCommentLikes(String.valueOf(item.id), new IsSuccess() {
                    @Override
                    public void isSuccess(boolean success) {
                        hideProgress();
                        if (success){
                            autoRefresh();
                            showToast("删除成功");
                        }else {
                            showToast("删除失败");
                        }
                    }
                });
            }
        });
        return true;
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().getCommentLikesList(page, 20)
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
        ConentUtils.httpGetMarkRead("", "LIKES", "ALL", new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                LogUtils.e(success);
            }
        });
    }
}
