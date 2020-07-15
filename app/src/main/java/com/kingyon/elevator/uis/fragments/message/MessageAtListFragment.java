package com.kingyon.elevator.uis.fragments.message;

import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;

import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AtListEntiy;
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
 * @Instructions:
 */
public class MessageAtListFragment extends BaseStateRefreshLoadingFragment<AtListEntiy> {
    Parser mTagParser = new Parser();

    @Override
    protected MultiItemTypeAdapter<AtListEntiy> getAdapter() {
        return new BaseAdapter<AtListEntiy>(getActivity(), R.layout.itme_at_list,mItems) {
            @Override
            protected void convert(CommonHolder holder, AtListEntiy item, int position) {
                MentionTextView mentionTextView = new MentionTextView(getActivity());
                mentionTextView = holder.getView(R.id.tv_content);
                mentionTextView.setMovementMethod(new LinkMovementMethod());
                mentionTextView.setParserConverter(mTagParser);
                MentionTextView mentionTextView1 = new MentionTextView(getActivity());
                mentionTextView1 = holder.getView(R.id.tv_comment);
                mentionTextView1.setMovementMethod(new LinkMovementMethod());

                mentionTextView1.setParserConverter(mTagParser);
                holder.setText(R.id.tv_content,item.content+"");
                holder.setText(R.id.tv_title,item.title+"");
                holder.setText(R.id.tv_nickname,item.createNickname+"");
                holder.setText(R.id.tv_time,item.createTime+"");
                GlideUtils.loadCircleImage(getActivity(),item.photo,holder.getView(R.id.img_portrait));
                if (item.isRead==1){
                    holder.setVisible(R.id.img_is,false);
                }else {
                    holder.setVisible(R.id.img_is,true);
                }
                switch (item.atType){
                    case "CONTENT":
                        /*内容*/
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
                                GlideUtils.loadRoundCornersImage(getActivity(),item.videoCover,holder.getView(R.id.img_image),20);
                                break;
                        }
                        holder.setTextSize(R.id.tv_content,12);
                        holder.setText(R.id.tv_comment,item.tips);
                        break;
                    case "COMMENT":
                        /*评论*/
                        holder.setVisible(R.id.img_image,false);
                        holder.setVisible(R.id.img_image1,false);
                        holder.setVisible(R.id.tv_title,false);
                        holder.setVisible(R.id.tv_content,true);
                        holder.setTextSize(R.id.tv_content,14);
                        holder.setText(R.id.tv_content,item.childComment);
                        holder.setText(R.id.tv_comment,item.parentComment);
                        break;
                }
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, AtListEntiy item, int position) {
        super.onItemClick(view, holder, item, position);
        ConentUtils.httpGetMarkRead(String.valueOf(item.id), "AT", "SINGLE ", new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                LogUtils.e(success);
            }
        });
                switch (item.contentType) {
                    case "wsq":
                        /*社区*/
                        LogUtils.e(item.objId);
                        ActivityUtils.setActivity(ACTIVITY_MAIN2_CONTENT_DRTAILS,
                                "contentId",item.objId);
                        break;
                    case "article":
                        /*文章*/
                        LogUtils.e(item.objId);
                        ActivityUtils.setActivity(ACTIVITY_MAIN2_ARTICLE_DRTAILS, "contentId", item.objId);
                        break;
                    case "video":
                        /*视频*/
                        LogUtils.e(item.objId);
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
        NetService.getInstance().getAtList(page,20)
                .subscribe(new CustomApiCallback<ConentEntity<AtListEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<AtListEntiy> atListEntiyConentEntity) {
                        if (atListEntiyConentEntity == null || atListEntiyConentEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(atListEntiyConentEntity.getContent());
                        if (page > 1 && atListEntiyConentEntity.getContent().size() <= 0) {
                            showToast("已经没有了");
                        }
                        loadingComplete(true, atListEntiyConentEntity.getTotalPages());
                    }
                });

    }
    @Override
    public int getContentViewId() {
        return R.layout.fragment_message_atlist;
    }

    @Override
    protected void dealLeackCanary() {

    }
}
