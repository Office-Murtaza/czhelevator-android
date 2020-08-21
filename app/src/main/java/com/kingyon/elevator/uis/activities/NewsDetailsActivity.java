package com.kingyon.elevator.uis.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.customview.CommentDetailBottomSheetDialog;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.presenter.NewsDetailsPresenter;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentCommentsAdapter;
import com.kingyon.elevator.uis.dialogs.DeleteShareDialog;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.kingyon.elevator.utils.utilstwo.SharedUtils;
import com.kingyon.elevator.utils.utilstwo.TokenUtils;
import com.kingyon.elevator.view.NewsDetailsView;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseHtmlActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.TimeUtil;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.leo.afbaselibrary.widgets.StateLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.commonsdk.debug.I;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;
import static com.czh.myversiontwo.utils.StringContent.getHtmlData;

/**
 * 新闻详情界面
 */
@Route(path = ACTIVITY_MAIN2_ARTICLE_DRTAILS)
public class NewsDetailsActivity extends MvpBaseActivity<NewsDetailsPresenter> implements NewsDetailsView {

    @BindView(R.id.ecv_list_pl)
    RecyclerView ecvListPl;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.comment_container)
    LinearLayout comment_container;
    @BindView(R.id.input_comment_container)
    ImageView input_comment_container;
    @BindView(R.id.progress_bar)
    ProgressBar progressbar;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.comment_count)
    TextView comment_count;
    @BindView(R.id.sort_list)
    TextView sort_list;
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
    @BindView(R.id.iv_share_news)
    ImageView ivShareNews;
    @BindView(R.id.im_sc)
    ImageView imSc;
    @BindView(R.id.iv_like)
    ImageView ivDianzan;
    @BindView(R.id.bottom_container)
    LinearLayout bottomContainer;
    @BindView(R.id.stateLayout)
    StateLayout stateLayout;
    @BindView(R.id.rl_gj)
    RelativeLayout rlGj;
    private int currentSortType = 1;
    private int page = 1;
    @Autowired
    int contentId;
    CommentDetailBottomSheetDialog commentDetailBottomSheetDialog;
    ContentCommentsAdapter contentCommentsAdapter;
    private int webviewHeight = 0;//webview的高度
    QueryRecommendEntity recommendEntity;
    private ShareDialog shareDialog;
    List<CommentListEntity> listEntities = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_news_details);
        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);
        smart_refresh_layout.setEnableAutoLoadMore(false);
        progressbar.setProgress(5);
        webview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressbar.setVisibility(GONE);
                } else {
                    if (progressbar.getVisibility() == GONE)
                        progressbar.setVisibility(VISIBLE);
                    progressbar.setProgress(newProgress + 5);
                }
                super.onProgressChanged(view, newProgress);
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String  url) {
                LogUtils.e(url);
//                Intent intent = new Intent(NewsDetailsActivity.this,AdvertisionActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString(BaseHtmlActivity.TITLE, "    ");
//                bundle.putString(BaseHtmlActivity.URL, url);
//                startActivity(intent, bundle);
                return true;
            }
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onPageFinished(WebView webView, String s) {
                super.onPageFinished(webView, s);
                stateLayout.showContentView();
                rlGj.setVisibility(VISIBLE);
                //页面加载完成
                int w = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                int h = View.MeasureSpec.makeMeasureSpec(0,
                        View.MeasureSpec.UNSPECIFIED);
                //重新测量
                webView.measure(w, h);
                webviewHeight = webView.getHeight();
                LogUtils.d("测量的webview高度：" + webviewHeight);
                comment_container.setVisibility(VISIBLE);
                httpComment(1, contentId);
            }

        });

        loadNewInfo();
        ecvListPl.setNestedScrollingEnabled(false);
        smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("onLoadMore-----------------");
                page++;
                httpComment(page, contentId);
            }
        });
        smart_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d("onRefresh-----------------");
                page = 1;
                listEntities.clear();
                httpComment(page, contentId);
            }
        });
    }


    /**
     * 加载新闻数据
     */
    private void loadNewInfo() {
        stateLayout.showProgressView(getString(R.string.wait));
        NetService.getInstance().setQueryContentById(String.valueOf(contentId), DataSharedPreferences.getCreatateAccount())
                .subscribe(new CustomApiCallback<QueryRecommendEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        finish();
                        stateLayout.showErrorView();
                    }

                    @Override
                    public void onNext(QueryRecommendEntity queryRecommendEntity) {
                        recommendEntity = queryRecommendEntity;
                        webview.loadDataWithBaseURL(null, getHtmlData(recommendEntity.content), "text/html", "utf-8", null);

                        tvName.setText(recommendEntity.nickname + "");
                        GlideUtils.loadCircleImage(NewsDetailsActivity.this, recommendEntity.photo, imgPortrait);

                        ConentUtils.httpAddBrowse(recommendEntity.id);

                        tvTime.setText(TimeUtil.getRecentlyTime(recommendEntity.createTime));
                        tvTitle.setText(recommendEntity.title + "");
                        tvLikeComments.setText(String.format("%s 点赞    %s 评论    ", recommendEntity.likes, recommendEntity.comments));
                        comment_count.setText(String.format("%s条评论", recommendEntity.comments));

                        if (recommendEntity.createAccount.equals(DataSharedPreferences.getCreatateAccount())) {
                            tvAttention.setVisibility(GONE);
                        } else {
                            tvAttention.setVisibility(VISIBLE);
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
                        if (recommendEntity.isCollect > 0) {
                            imSc.setImageResource(R.mipmap.btn_big_collect_off);
                        } else {
                            imSc.setImageResource(R.mipmap.btn_big_collect);
                        }
                        if (recommendEntity.liked) {
                            ivDianzan.setImageResource(R.mipmap.btn_big_like_off);
                        } else {
                            ivDianzan.setImageResource(R.mipmap.btn_big_like);
                        }
                    }
                });
    }

    @OnClick({R.id.img_bake, R.id.img_jb, R.id.input_comment_container, R.id.iv_share_news, R.id.im_sc, R.id.iv_like, R.id.tv_attention})
    public void OnClick(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_jb:
                if (TokenUtils.isToken(this)) {
                    if (TokenUtils.isCreateAccount(recommendEntity.createAccount)) {
                        /*删除*/
                        DeleteShareDialog deleteShareDialog = new DeleteShareDialog(this, recommendEntity.id, null, "2", 0, null, null);
                        deleteShareDialog.show();
                    } else {
                        /*举报*/
                        ReportShareDialog reportShareDialog = new ReportShareDialog(this, recommendEntity.id, HOME_CONTENT, "");
                        reportShareDialog.show();
                    }
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.input_comment_container:
                if (TokenUtils.isToken(this)) {
                    InputCommentActivity.openEditor(NewsDetailsActivity.this, new EditorCallback() {
                        @Override
                        public void onCancel() {
                            LogUtils.d("关闭输入法-------------");
                            KeyboardUtils.hideSoftInput(NewsDetailsActivity.this);
                        }

                        @Override
                        public void onSubmit(String content) {
                            presenter.addNewsComment(contentId, content);
                        }

                        @Override
                        public void onAttached(ViewGroup rootView) {
                        }

                        @Override
                        public void onIcon() {
                        }
                    });
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;

            case R.id.iv_share_news:
                SharedUtils.shared(NewsDetailsActivity.this, shareDialog, recommendEntity.content, "www.baidu.con", recommendEntity.title, false);
                break;
            case R.id.im_sc:
                /*收藏*/
                if (TokenUtils.isToken(this)) {
                    if (recommendEntity.isCollect == 0) {
                        ConentUtils.httpAddCollect(String.valueOf(recommendEntity.id), Constants.COLLECT_STATE.CONTENT, new ConentUtils.AddCollect() {
                            @Override
                            public void Collect(boolean is) {
                                if (is) {
                                    imSc.setImageResource(R.mipmap.btn_big_collect_off);
                                    ToastUtils.showToast(NewsDetailsActivity.this, "收藏成功", 1000);
                                    LogUtils.e("收藏成功");
                                    recommendEntity.isCollect = 1;
                                } else {
                                    ToastUtils.showToast(NewsDetailsActivity.this, "收藏失败", 1000);
                                }
                            }
                        });
                    } else {
                        ConentUtils.httpCancelCollect(String.valueOf(recommendEntity.id), new ConentUtils.AddCollect() {
                            @Override
                            public void Collect(boolean is) {
                                if (is) {
                                    recommendEntity.isCollect = 0;
                                    imSc.setImageResource(R.mipmap.btn_big_collect);
                                    ToastUtils.showToast(NewsDetailsActivity.this, "取消收藏成功", 1000);
                                } else {
                                    ToastUtils.showToast(NewsDetailsActivity.this, "取消收藏失败", 1000);
                                }
                            }
                        });
                    }
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.iv_like:
                if (TokenUtils.isToken(this)) {
                    if (recommendEntity.liked) {
                        recommendEntity.liked = false;
                        ivDianzan.setImageResource(R.mipmap.btn_big_like);
                        ConentUtils.httpHandlerLikeOrNot(this, recommendEntity.id,
                                HOME_CONTENT, CANCEL_LIKE, new IsSuccess() {
                                    @Override
                                    public void isSuccess(boolean success) {

                                    }
                                });
                    } else {
                        recommendEntity.liked = true;
                        ivDianzan.setImageResource(R.mipmap.btn_big_like_off);
                        ConentUtils.httpHandlerLikeOrNot(this, recommendEntity.id,
                                HOME_CONTENT, LIKE, new IsSuccess() {
                                    @Override
                                    public void isSuccess(boolean success) {

                                    }
                                });
                    }
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
            case R.id.tv_attention:
                if (TokenUtils.isToken(this)) {
                    httpAddUser();
                } else {
                    ActivityUtils.setLoginActivity();
                }
                break;
        }

    }


    @Override
    public NewsDetailsPresenter initPresenter() {
        return new NewsDetailsPresenter(this);
    }

    private ViewTreeObserver.OnGlobalLayoutListener getGlobalLayoutListener(final View decorView, final View contentView) {
        return new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                decorView.getWindowVisibleDisplayFrame(r);

                int height = decorView.getContext().getResources().getDisplayMetrics().heightPixels;
                int diff = height - r.bottom;

                if (diff != 0) {
                    if (contentView.getPaddingBottom() != diff) {
                        contentView.setPadding(0, 0, 0, diff);
                    }
                } else {
                    if (contentView.getPaddingBottom() != 0) {
                        contentView.setPadding(0, 0, 0, 0);
                    }
                }
            }
        };
    }

    private void httpAddUser() {
        if (tvAttention.getText().toString().equals("关注")) {
            ConentUtils.httpAddAttention(this, "add", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(NewsDetailsActivity.this, "关注成功", 100);
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                    tvAttention.setTextColor(Color.parseColor("#FF1330"));
                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(NewsDetailsActivity.this, magssger + code, 100);
                }
            });
        } else {
            ConentUtils.httpAddAttention(this, "cancel", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(NewsDetailsActivity.this, "取消关注成功", 100);
                    tvAttention.setText("关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention));
                    tvAttention.setTextColor(Color.parseColor("#ffffff"));
                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(NewsDetailsActivity.this, magssger + code, 100);
                }
            });
        }

    }

    @Override
    protected void onDestroy() {
        if (webview != null) {
            //加载null内容
            webview.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            //清除历史记录
            webview.clearHistory();
            //销毁VebView
            webview.destroy();
            //WebView置为null
            webview = null;
        }
        if (commentDetailBottomSheetDialog != null) {
            commentDetailBottomSheetDialog.dismiss();
            commentDetailBottomSheetDialog = null;
        }
        super.onDestroy();
    }


    private void httpComment(int page, int id) {
        LogUtils.e(page, id);
        NetService.getInstance().setQueryListComment(page, id)
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        OrdinaryActivity.closeRefresh(smart_refresh_layout);
                    }

                    @Override
                    public void onNext(ConentEntity<CommentListEntity> conentEntity) {
                        dataAdd(conentEntity);
                        OrdinaryActivity.closeRefresh(smart_refresh_layout);

                    }
                });
    }

    private void dataAdd(ConentEntity<CommentListEntity> conentEntity) {

        if (conentEntity.getContent().size() == 0) {
            smart_refresh_layout.setEnableLoadMore(true);
        }
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            CommentListEntity commentListEntity = new CommentListEntity();
            commentListEntity = conentEntity.getContent().get(i);
            listEntities.add(commentListEntity);
        }
        if (contentCommentsAdapter == null || page == 1) {
            ecvListPl.setNestedScrollingEnabled(false);
            ecvListPl.setFocusable(false);
            contentCommentsAdapter = new ContentCommentsAdapter(NewsDetailsActivity.this, "1", 0,
                    new ContentCommentsAdapter.GetRefresh() {
                        @Override
                        public void onRefresh(boolean isSucced) {
                            listEntities.clear();
                            page = 1;
                            httpComment(page, recommendEntity.id);
                        }
                    });
            contentCommentsAdapter.addData(listEntities);
            ecvListPl.setAdapter(contentCommentsAdapter);
            ecvListPl.setLayoutManager(new GridLayoutManager(NewsDetailsActivity.this, 1, GridLayoutManager.VERTICAL, false));
        } else {
            contentCommentsAdapter.addData(listEntities);
            contentCommentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (webview.canGoBack()) {
            webview.goBack();
        } else {
            finish();
        }
    }

    @Override
    public void hideProgressDailog() {
        super.hideProgressDailog();
        smart_refresh_layout.finishRefresh();
        smart_refresh_layout.finishLoadMore();
    }


    @Override
    public void showListData(List<CommentListEntity> incomeDetailsEntities) {
        if (contentCommentsAdapter != null) {
            contentCommentsAdapter.addData(incomeDetailsEntities);
        }
    }

    @Override
    public void loadMoreIsComplete() {
        LogUtils.d("全部加载完成了-----------------");

    }

    @Override
    public void commentAddSuccess() {
        LogUtils.e("12323232323232323232323", contentId, 1);
        listEntities.clear();
        httpComment(1, contentId);
    }

    @Override
    public void addLikeCommentSuccess(int position) {
        try {
            if (presenter.getCommentEntities().get(position).isLiked == 1) {
                presenter.getCommentEntities().get(position).isLiked = 0;
            } else {
                presenter.getCommentEntities().get(position).isLiked = 1;
            }
            contentCommentsAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
