package com.kingyon.elevator.uis.actiivty2.content;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
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
import com.google.gson.Gson;
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
import com.kingyon.elevator.uis.dialogs.DeleteShareDialog;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.SharedUtils;
import com.kingyon.elevator.utils.utilstwo.TokenUtils;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.StringContent.getHtmlData;
import static com.kingyon.elevator.uis.fragments.main2.found.AttentionFragment.isRefresh;

/**
 * Created By Admin  on 2020/4/17
 * Email : 163235610@qq.com
 *
 * @author:Mrczh Instructions: 文章详情
 */
@Route(path = ACTIVITY_MAIN2_ARTICLE_DRTAILS)
public class ArticleDetailsActivity extends BaseActivity {
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
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.tv_comments_number)
    TextView tvCommentsNumber;
    @BindView(R.id.ecv_list_pl)
    RecyclerView ecvListPl;
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
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private int page = 1;
    List<CommentListEntity> listEntities = new ArrayList<>();
    ContentCommentsAdapter contentCommentsAdapter;
    private ShareDialog shareDialog;
    int i = 1;


    @Override
    public int getContentViewId() {
        return R.layout.activity_article_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        isRefresh = false;
        ARouter.getInstance().inject(this);
        LogUtils.e(contentId);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().setQueryContentById(String.valueOf(contentId), DataSharedPreferences.getCreatateAccount())
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<QueryRecommendEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        finish();
                        ToastUtils.showToast(ArticleDetailsActivity.this,ex.getDisplayMessage(),1000);
                    }

                    @Override
                    public void onNext(QueryRecommendEntity queryRecommendEntity) {

                        recommendEntity = queryRecommendEntity;
                        tvName.setText(recommendEntity.nickname + "");
                        GlideUtils.loadCircleImage(ArticleDetailsActivity.this, recommendEntity.photo, imgPortrait);

                        ConentUtils.httpAddBrowse(ArticleDetailsActivity.this, recommendEntity.id);

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
                        if (recommendEntity.original) {
                            tvOriginal.setText("原创");
                        } else {
                            tvOriginal.setText("转载");
                        }
                        if (recommendEntity.isCollect>0){
                            imCollection.setImageResource(R.mipmap.btn_big_collect_off);
                        }else {
                            imCollection.setImageResource(R.mipmap.btn_big_collect);
                        }
                        webview.loadDataWithBaseURL(null, getHtmlData(recommendEntity.content), "text/html", "utf-8", null);
                        LogUtils.e( getHtmlData(recommendEntity.content));
                        httpComment(page, recommendEntity.id);
                        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                                page++;
                                httpComment(page,recommendEntity.id);
                            }
                        });
                        hideProgress();
                    }
                });



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    private void httpComment(int page, int id) {
        LogUtils.e(page, id);
        NetService.getInstance().setQueryListComment(page, id)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        smartRefreshLayout.finishLoadMore();
//                        ToastUtils.showToast(ArticleDetailsActivity.this,ex.getDisplayMessage(),1000);
                    }

                    @Override
                    public void onNext(ConentEntity<CommentListEntity> conentEntity) {
                        dataAdd(conentEntity);
                        smartRefreshLayout.finishLoadMore();

                    }
                });
    }

    private void dataAdd(ConentEntity<CommentListEntity> conentEntity) {

        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            CommentListEntity commentListEntity = new CommentListEntity();
            commentListEntity = conentEntity.getContent().get(i);
            listEntities.add(commentListEntity);
        }
        if (contentCommentsAdapter == null || page == 1) {
            ecvListPl.setNestedScrollingEnabled(false);
            ecvListPl.setFocusable(false);
            contentCommentsAdapter = new ContentCommentsAdapter(ArticleDetailsActivity.this,"1",
            new ContentCommentsAdapter.GetRefresh() {
                @Override
                public void onRefresh(boolean isSucced) {
                    listEntities.clear();
                    page=1;
                    httpComment(page, recommendEntity.id);
                }
            });
            contentCommentsAdapter.addData(listEntities);
            ecvListPl.setAdapter(contentCommentsAdapter);
            ecvListPl.setLayoutManager(new GridLayoutManager(ArticleDetailsActivity.this, 1, GridLayoutManager.VERTICAL, false));
        } else {
            contentCommentsAdapter.addData(listEntities);
            contentCommentsAdapter.notifyDataSetChanged();
        }
    }


    @SuppressWarnings("AlibabaSwitchStatement")
    @OnClick({R.id.img_bake, R.id.img_jb, R.id.input_comment_container, R.id.iv_share_news, R.id.im_collection, R.id.iv_like, R.id.tv_attention})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_jb:
                if (TokenUtils.isToken(this)) {
                    if (TokenUtils.isCreateAccount(recommendEntity.createAccount)) {
                        /*删除*/
                        DeleteShareDialog deleteShareDialog = new DeleteShareDialog(this, recommendEntity.id, null, "2", 0, null,null);
                        deleteShareDialog.show();
                    } else {
                        /*举报*/
                        ReportShareDialog reportShareDialog = new ReportShareDialog(this, recommendEntity.id, HOME_CONTENT);
                        reportShareDialog.show();
                    }
                }else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.input_comment_container:
                InputCommentActivity.openEditor(ArticleDetailsActivity.this, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(ArticleDetailsActivity.this);
                    }

                    @Override
                    public void onSubmit(String content) {
                        ConentUtils.httpComment(ArticleDetailsActivity.this,
                                recommendEntity.id, 0, content, new ConentUtils.IsSuccedListener() {
                                    @Override
                                    public void onisSucced(boolean isSucced) {
                                        if (isSucced) {
                                            listEntities.clear();
                                            httpComment(1, recommendEntity.id);
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

                break;
            case R.id.iv_share_news:
                SharedUtils.shared(this, shareDialog,recommendEntity.content, "www.baidu.com", recommendEntity.title);
                break;
            case R.id.im_collection:
                /*收藏*/
                if (recommendEntity.isCollect==0) {
                    ConentUtils.httpAddCollect(String.valueOf(recommendEntity.id), Constants.COLLECT_STATE.CONTENT, new ConentUtils.AddCollect() {
                        @Override
                        public void Collect(boolean is) {
                            if (is) {
                                imCollection.setImageResource(R.mipmap.btn_big_collect_off);
                                ToastUtils.showToast(ArticleDetailsActivity.this, "收藏成功", 1000);
                                LogUtils.e("收藏成功");
                                recommendEntity.isCollect = 1;
                            } else {
                                ToastUtils.showToast(ArticleDetailsActivity.this, "收藏失败", 1000);
                            }
                        }
                    });
                }else {
                    ConentUtils.httpCancelCollect(String.valueOf(recommendEntity.id), new ConentUtils.AddCollect() {
                        @Override
                        public void Collect(boolean is) {
                            if (is) {
                                recommendEntity.isCollect = 0;
                                imCollection.setImageResource(R.mipmap.btn_big_collect);
                                ToastUtils.showToast(ArticleDetailsActivity.this, "取消收藏成功", 1000);
                            } else {
                                ToastUtils.showToast(ArticleDetailsActivity.this, "取消收藏失败", 1000);                           }
                        }
                    });
                }
                break;
            case R.id.iv_like:
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

                break;
            case R.id.tv_attention:
                httpAddUser();
                break;
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }




    @OnClick()
    public void onViewClicked() {
    }

    private void httpAddUser() {
        if (tvAttention.getText().toString().equals("关注")) {
            ConentUtils.httpAddAttention(this, "add", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(ArticleDetailsActivity.this, "关注成功", 100);
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                    tvAttention.setTextColor(Color.parseColor("#FF1330"));


                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(ArticleDetailsActivity.this, magssger + code, 100);
                }
            });
        } else {
            ConentUtils.httpAddAttention(this, "cancel", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(ArticleDetailsActivity.this, "取消关注成功", 100);
                    tvAttention.setText("关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention));
                    tvAttention.setTextColor(Color.parseColor("#ffffff"));
                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(ArticleDetailsActivity.this, magssger + code, 100);
                }
            });
        }

    }
}
