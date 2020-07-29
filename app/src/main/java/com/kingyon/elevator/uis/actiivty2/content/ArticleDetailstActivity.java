package com.kingyon.elevator.uis.actiivty2.content;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentCommentsAdapter;
import com.kingyon.elevator.uis.dialogs.DeleteArticeleDialog;
import com.kingyon.elevator.uis.dialogs.DeleteShareDialog;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.SharedUtils;
import com.kingyon.elevator.utils.utilstwo.StringUtils;
import com.kingyon.elevator.utils.utilstwo.TokenUtils;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_COMMENT;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_COMMENT_TWO;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.StringContent.getHtmlData;

/**
 * Created By Admin  on 2020/4/17
 * Email : 163235610@qq.com
 *
 * @author:Mrczh Instructions: 文章详情
 */
@Route(path = ACTIVITY_MAIN2_ARTICLE_DRTAILS)
public class ArticleDetailstActivity extends BaseStateRefreshingLoadingActivity<CommentListEntity> {
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.img_portrait)
    ImageView imgPortrait;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.img_jb)
    ImageView imgJb;
    @BindView(R.id.tv_original)
    TextView tvOriginal;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_like_comments)
    TextView tvLikeComments;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.tv_comments_number)
    TextView tvCommentsNumber;
    @BindView(R.id.input_comment_container)
    ImageView inputCommentContainer;
    @BindView(R.id.iv_share_news)
    ImageView ivShareNews;
    @BindView(R.id.im_collection)
    ImageView imCollection;
    @BindView(R.id.iv_like)
    ImageView ivLike;

    @Autowired
    int contentId;

    QueryRecommendEntity recommendEntity;
    private ShareDialog shareDialog;
    @Override
    protected MultiItemTypeAdapter<CommentListEntity> getAdapter() {
        return new BaseAdapter<CommentListEntity>(this, R.layout.item_content_comments, mItems) {
            @Override
            protected void convert(CommonHolder holder, CommentListEntity item, int position) {
                holder.setText(R.id.tv_comment,item.comment);
                holder.setText(R.id.tv_comment_hf,StringUtils.getNumStr(item.child.size(),"回复"));
                holder.setText(R.id.tv_like_number,StringUtils.getNumStr(item.likesNum,"点赞"));
                holder.setText(R.id.tv_name,item.nickname);
                holder.setText(R.id.tv_time,TimeUtil.getRecentlyTime(item.createTime));
                GlideUtils.loadCircleImage(ArticleDetailstActivity.this, item.photo, holder.getView(R.id.img_portrait));
//            if (mItems.liked){
//                holder.img_like.setImageResource(R.mipmap.ic_small_like);
//            }else {
//                holder.img_like.setImageResource(R.mipmap.ic_small_like_off);
//            }
                holder.setOnClickListener(R.id.tv_comment_hf,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentOnClick(item);
                    }
                });
                holder.setOnClickListener(R.id.img_comments,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        commentOnClick(item);
                    }
                });

                holder.setOnClickListener(R.id.img_delete,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (TokenUtils.isToken(ArticleDetailstActivity.this)){
                            LogUtils.e(DataSharedPreferences.getCreatateAccount(),item.createAccount);
                            if (TokenUtils.isCreateAccount(item.createAccount)){
                                /*删除*/
                                DeleteShareDialog deleteShareDialog = new DeleteShareDialog(ArticleDetailstActivity.this,item.id,
                                        null,"3",position,null,null);
                                deleteShareDialog.show();

                            }else {
                                /*举报*/
                                ReportShareDialog reportShareDialog = new ReportShareDialog(ArticleDetailstActivity.this,item.id,HOME_COMMENT);
                                reportShareDialog.show();
                            }
                        }else {
                            ActivityUtils.setLoginActivity();
                        }

                    }
                });
                holder.setOnClickListener(R.id.img_like,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (true) {

                            holder.setImageResource(R.id.img_like,R.mipmap.ic_small_like);
                        }else {

                            holder.setImageResource(R.id.img_like,R.mipmap.ic_small_like_off);
                        }

                    }
                });
                
            }
        };
    }

    private void commentOnClick(CommentListEntity item) {
        if (item.child.size() > 0) {
            /*跳转*/
            ARouter.getInstance().build(ACTIVITY_COMMENT_TWO)
                    .withInt("contentId", item.contentId)
                    .withInt("onId", item.id)
                    .navigation();
        } else {
            InputCommentActivity.openEditor(ArticleDetailstActivity.this, new EditorCallback() {
                @Override
                public void onCancel() {
                    LogUtils.d("关闭输入法-------------");
                    KeyboardUtils.hideSoftInput(ArticleDetailstActivity.this);
                }

                @Override
                public void onSubmit(String content) {
                    ConentUtils.httpComment(ArticleDetailstActivity.this, item.contentId,
                            item.id, content, new ConentUtils.IsSuccedListener() {
                                @Override
                                public void onisSucced(boolean isSucced) {
//                                                        getRefresh.onRefresh(isSucced);
                                    autoRefresh();
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
    protected void loadData(int page) {
        NetService.getInstance().setQueryListComment(page, contentId)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
//                        showToast(ex.getDisplayMessage());
                        loadingComplete(true, 1);
                        showState(STATE_CONTENT);
                    }

                    @Override
                    public void onNext(ConentEntity<CommentListEntity> conentEntity) {
                        if (conentEntity == null || conentEntity.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(conentEntity.getContent());
                        if (page > 1 && conentEntity.getContent().size() <= 0) {
                            showToast("已经没有了");
                        }
                        loadingComplete(true, conentEntity.getTotalPages());
                    }
                });

    }

    @Override
    protected String getTitleText() {
        return null;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_article_detailst;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        ARouter.getInstance().inject(this);
        ConentUtils.topicStr = "";
        NetService.getInstance().setQueryContentById(String.valueOf(contentId), DataSharedPreferences.getCreatateAccount())
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<QueryRecommendEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        finish();
                        ToastUtils.showToast(ArticleDetailstActivity.this,ex.getDisplayMessage(),1000);
                    }

                    @Override
                    public void onNext(QueryRecommendEntity queryRecommendEntity) {

                        recommendEntity = queryRecommendEntity;

                        webview.loadDataWithBaseURL(null, getHtmlData(recommendEntity.content), "text/html", "utf-8", null);

                        tvName.setText(recommendEntity.nickname + "");
                        GlideUtils.loadCircleImage(ArticleDetailstActivity.this, recommendEntity.photo, imgPortrait);

                        ConentUtils.httpAddBrowse(ArticleDetailstActivity.this, recommendEntity.id);

                        tvTime.setText(TimeUtil.getRecentlyTime(recommendEntity.createTime));
                        tvTitle.setText(recommendEntity.title + "");
                        tvLikeComments.setText(String.format("%s 点赞    %s 评论    ", recommendEntity.likes, recommendEntity.comments));
                        tvCommentsNumber.setText(String.format("%s条评论", recommendEntity.comments));

                        if (recommendEntity.createAccount.equals(DataSharedPreferences.getCreatateAccount())){
                            tvAttention.setVisibility(View.GONE);
                        }else {
                            tvAttention.setVisibility(View.VISIBLE);
                        }
                        if (recommendEntity.isAttent == 0) {
                            tvAttention.setText("关注");
                            tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention));
                            tvAttention.setTextColor(Color.parseColor("#ffffff"));

                        } else {
                            tvAttention.setText("已关注");
                            tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                            tvAttention.setTextColor(Color.parseColor("#FF1330"));

                        }
                        if (!recommendEntity.original) {
                            tvOriginal.setText("原创");
                        } else {
                            tvOriginal.setText("转载");
                        }
                        if (recommendEntity.isCollect>0){
                            imCollection.setImageResource(R.mipmap.btn_big_collect_off);
                        }else {
                            imCollection.setImageResource(R.mipmap.btn_big_collect);
                        }
                        if (recommendEntity.liked) {
                            ivLike.setImageResource(R.mipmap.btn_big_like_off);
                        } else {
                            ivLike.setImageResource(R.mipmap.btn_big_like);
                        }
                        hideProgress();
                    }
                });

    }

    @OnClick({R.id.img_bake, R.id.img_portrait, R.id.tv_name, R.id.tv_attention, R.id.img_jb, R.id.input_comment_container, R.id.iv_share_news, R.id.im_collection, R.id.iv_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_portrait:

                break;
            case R.id.tv_name:

                break;
            case R.id.tv_attention:
                if (TokenUtils.isToken(this)) {
                    httpAddUser();
                }else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.img_jb:
                if (TokenUtils.isToken(this)) {
                    if (TokenUtils.isCreateAccount(recommendEntity.createAccount)) {
                        /*删除*/
                        DeleteShareDialog deleteShareDialog = new DeleteShareDialog(this, recommendEntity.id, null, "2", 0, null, null);
                        deleteShareDialog.show();
                    } else {
                        /*举报*/
                        ReportShareDialog reportShareDialog = new ReportShareDialog(this, recommendEntity.id, HOME_CONTENT);
                        reportShareDialog.show();
                    }
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.input_comment_container:

                if (TokenUtils.isToken(this)) {
                    InputCommentActivity.openEditor(ArticleDetailstActivity.this, new EditorCallback() {
                        @Override
                        public void onCancel() {
                            LogUtils.d("关闭输入法-------------");
                            KeyboardUtils.hideSoftInput(ArticleDetailstActivity.this);
                        }

                        @Override
                        public void onSubmit(String content) {
                            ConentUtils.httpComment(ArticleDetailstActivity.this,
                                    recommendEntity.id, 0, content, new ConentUtils.IsSuccedListener() {
                                        @Override
                                        public void onisSucced(boolean isSucced) {
                                            if (isSucced) {
                                                autoRefresh();
                                            }
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
                }else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.iv_share_news:
                SharedUtils.shared(this, shareDialog, recommendEntity.content, "www.baidu.com", recommendEntity.title,false);

                break;
            case R.id.im_collection:
                if (TokenUtils.isToken(this)) {
                    if (recommendEntity.isCollect == 0) {
                        ConentUtils.httpAddCollect(String.valueOf(recommendEntity.id), Constants.COLLECT_STATE.CONTENT, new ConentUtils.AddCollect() {
                            @Override
                            public void Collect(boolean is) {
                                if (is) {
                                    imCollection.setImageResource(R.mipmap.btn_big_collect_off);
                                    ToastUtils.showToast(ArticleDetailstActivity.this, "收藏成功", 1000);
                                    LogUtils.e("收藏成功");
                                    recommendEntity.isCollect = 1;
                                } else {
                                    ToastUtils.showToast(ArticleDetailstActivity.this, "收藏失败", 1000);
                                }
                            }
                        });
                    } else {
                        ConentUtils.httpCancelCollect(String.valueOf(recommendEntity.id), new ConentUtils.AddCollect() {
                            @Override
                            public void Collect(boolean is) {
                                if (is) {
                                    recommendEntity.isCollect = 0;
                                    imCollection.setImageResource(R.mipmap.btn_big_collect);
                                    ToastUtils.showToast(ArticleDetailstActivity.this, "取消收藏成功", 1000);
                                } else {
                                    ToastUtils.showToast(ArticleDetailstActivity.this, "取消收藏失败", 1000);
                                }
                            }
                        });
                    }
                }else {
                    ActivityUtils.setLoginActivity();
                }

                break;
            case R.id.iv_like:

                if (TokenUtils.isToken(this)) {
                    if (recommendEntity.liked) {
                        recommendEntity.liked = false;
                        ivLike.setImageResource(R.mipmap.btn_big_like);
                        ConentUtils.httpHandlerLikeOrNot(this, recommendEntity.id,
                                HOME_CONTENT, CANCEL_LIKE, 0, recommendEntity, "2");
                    } else {
                        recommendEntity.liked = true;
                        ivLike.setImageResource(R.mipmap.btn_big_like_off);
                        ConentUtils.httpHandlerLikeOrNot(this, recommendEntity.id,
                                HOME_CONTENT, LIKE, 0, recommendEntity, "2");
                    }
                }else {
                    ActivityUtils.setLoginActivity();
                }
                break;
        }
    }

    private void httpAddUser() {
        if (tvAttention.getText().toString().equals("关注")) {
            ConentUtils.httpAddAttention(this, "add", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(ArticleDetailstActivity.this, "关注成功", 100);
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                    tvAttention.setTextColor(Color.parseColor("#FF1330"));


                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(ArticleDetailstActivity.this, magssger + code, 100);
                }
            });
        } else {
            ConentUtils.httpAddAttention(this, "cancel", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(ArticleDetailstActivity.this, "取消关注成功", 100);
                    tvAttention.setText("关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention));
                    tvAttention.setTextColor(Color.parseColor("#ffffff"));
                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(ArticleDetailstActivity.this, magssger + code, 100);
                }
            });
        }

    }
}
