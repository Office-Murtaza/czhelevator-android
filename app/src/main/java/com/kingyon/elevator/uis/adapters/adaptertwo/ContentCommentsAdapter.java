package com.kingyon.elevator.uis.adapters.adaptertwo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.dialogs.DeleteShareDialog;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.kingyon.elevator.utils.utilstwo.TokenUtils;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.umeng.commonsdk.debug.I;

import java.util.List;

import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_COMMENT;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_COMMENT_TWO;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:
 */
public class ContentCommentsAdapter extends RecyclerView.Adapter<ContentCommentsAdapter.ViewHolder> {

    BaseActivity context;
    List<CommentListEntity> conentEntity;
    String type;
    GetRefresh getRefresh;
    int likeNum;
    int onId;
    private String TOP_USER = "&nbsp;<user id='%s' style='color: #4dacee;' name='%s'>@%s</user>&nbsp;";
    Parser mTagParser = new Parser();
    /**
     * type 1 一级 2 子级
     * */
    public ContentCommentsAdapter(BaseActivity context,String type,int onId, GetRefresh getRefresh){
        this.context = context;
        this.type = type;
        this.getRefresh = getRefresh;
        this.onId = onId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_content_comments,parent,false);

        return new ContentCommentsAdapter.ViewHolder(view);
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CommentListEntity commentListEntity = conentEntity.get(position);
        if (holder!=null){
            holder.tv_comment.setText(commentListEntity.comment);
            if (type.equals("1")){
                holder.tv_comment_hf.setText(StringUtils.getNumStr(commentListEntity.child.size(),"回复"));
            }else {
                holder.tv_comment_hf.setText("回复");
            }
            likeNum = commentListEntity.likesNum;
            holder.tv_like_number.setText(StringUtils.getNumStr(commentListEntity.likesNum,"点赞"));
            holder.tv_name.setText(commentListEntity.nickname);
            holder.tv_time.setText(TimeUtil.getRecentlyTime(commentListEntity.createTime));
            GlideUtils.loadCircleImage(context, commentListEntity.photo, holder.img_portrait);
            if (conentEntity.get(position).isLiked==1){
                holder.img_like.setImageResource(R.mipmap.ic_small_like);
            }else {
                holder.img_like.setImageResource(R.mipmap.ic_small_like_off);
            }
            holder.ll_comments.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentOnClick(commentListEntity);
                }
            });

            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (TokenUtils.isToken(context)){
                        LogUtils.e(DataSharedPreferences.getCreatateAccount(),commentListEntity.createAccount);
                        if (TokenUtils.isCreateAccount(commentListEntity.createAccount)){
                            /*删除*/
                            DeleteShareDialog deleteShareDialog = new DeleteShareDialog(context,commentListEntity.id,
                                    ContentCommentsAdapter.this,"3",position,null,conentEntity);
                            deleteShareDialog.show();
                        }else {
                            /*举报*/
                            ReportShareDialog reportShareDialog = new ReportShareDialog(context,commentListEntity.id,HOME_COMMENT,"");
                            reportShareDialog.show();
                        }
                    }else {
                        ActivityUtils.setLoginActivity();
                    }

                }
            });
            holder.img_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (commentListEntity.isLiked==0) {
                        commentListEntity.isLiked = 1;
                        holder.img_like.setImageResource(R.mipmap.ic_small_like);
                        ConentUtils.httpHandlerLikeOrNot(context, commentListEntity.id,
                                HOME_COMMENT, LIKE, new IsSuccess() {
                                    @Override
                                    public void isSuccess(boolean success) {
                                        holder.tv_like_number.setText(StringUtils.getNumStr(likeNum+1,"点赞"));
                                        commentListEntity.likesNum = likeNum+1;

                                    }
                                });
                    }else {
                        commentListEntity.isLiked = 0;
                        holder.img_like.setImageResource(R.mipmap.ic_small_like_off);
                        ConentUtils.httpHandlerLikeOrNot(context, commentListEntity.id,
                                HOME_COMMENT, CANCEL_LIKE, new IsSuccess() {
                                    @Override
                                    public void isSuccess(boolean success) {
                                        holder.tv_like_number.setText(StringUtils.getNumStr(likeNum-1,"点赞"));
                                        commentListEntity.likesNum = likeNum-1;
                                    }
                                });
                    }

                }
            });

        }
    }

    private void commentOnClick(CommentListEntity commentListEntity) {
        LogUtils.e(type);
        if (type.equals("1")) {
            if (commentListEntity.child.size() > 0) {
                /*跳转*/
                ARouter.getInstance().build(ACTIVITY_COMMENT_TWO)
                        .withInt("contentId", commentListEntity.contentId)
                        .withInt("onId", commentListEntity.id)
                        .navigation();
            } else
                {
                InputCommentActivity.openEditor(context, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(context);
                    }

                    @Override
                    public void onSubmit(String content) {
                        ConentUtils.httpComment(context, commentListEntity.contentId,
                                commentListEntity.id, content, new ConentUtils.IsSuccedListener() {
                                    @Override
                                    public void onisSucced(boolean isSucced) {
                                        getRefresh.onRefresh(isSucced);
                                    }
                                });
                    }

                    @Override
                    public void onAttached(ViewGroup rootView) {

                    }

                    @Override
                    public void onIcon() {

                    }
                });
            }
        }else {
            InputCommentActivity.openEditor(context, new EditorCallback() {
                @Override
                public void onCancel() {
                    LogUtils.d("关闭输入法-------------");
                    KeyboardUtils.hideSoftInput(context);
                }
                @Override
                public void onSubmit(String content) {
                    LogUtils.e( commentListEntity.contentId,
                            onId, "回复 : "+commentListEntity.nickname+" "+content);
                    String atAccount = String.format(TOP_USER, commentListEntity.createAccount, commentListEntity.nickname, commentListEntity.nickname);

                    ConentUtils.httpComment(context, commentListEntity.contentId,
                            onId, "回复 "+atAccount+" : "+content, new ConentUtils.IsSuccedListener() {
                                @Override
                                public void onisSucced(boolean isSucced) {
                                    getRefresh.onRefresh(isSucced);
                                }
                            });
                }

                @Override
                public void onAttached(ViewGroup rootView) {
                }

                @Override
                public void onIcon() {

                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return conentEntity.size();
    }

    public void addData(List<CommentListEntity> listEntities) {
        this.conentEntity = listEntities;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_comment_hf,tv_time,tv_like_number,tv_name;
        MentionTextView tv_comment;
        ImageView img_like,img_portrait,img_delete,img_comments;
        LinearLayout ll_comments;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_comment = itemView.findViewById(R.id.tv_comment);
            tv_comment_hf = itemView.findViewById(R.id.tv_comment_hf);
            ll_comments = itemView.findViewById(R.id.ll_comments);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_like_number = itemView.findViewById(R.id.tv_like_number);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_like = itemView.findViewById(R.id.img_like);
            img_portrait = itemView.findViewById(R.id.img_portrait);
            img_delete = itemView.findViewById(R.id.img_delete);
            img_comments = itemView.findViewById(R.id.img_comments);
            tv_comment.setMovementMethod(new LinkMovementMethod());
            tv_comment.setParserConverter(mTagParser);
        }
    }

    public interface GetRefresh{
        void onRefresh(boolean isSucced) ;
    }
}
