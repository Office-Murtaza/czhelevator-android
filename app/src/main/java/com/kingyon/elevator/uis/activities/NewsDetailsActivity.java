package com.kingyon.elevator.uis.activities;

import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.net.http.SslError;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.customview.CommentDetailBottomSheetDialog;
import com.kingyon.elevator.customview.MyActionBar;
import com.kingyon.elevator.customview.NonScrollableListView;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.NewsDetailsEntity;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.entities.NewsSharedEntity;
import com.kingyon.elevator.entities.RecommendInfoEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.interfaces.OnItemClick;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.presenter.NewsDetailsPresenter;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.adapters.NewsCommentAdapter;
import com.kingyon.elevator.utils.DensityUtil;
import com.kingyon.elevator.utils.QuickClickUtils;
import com.kingyon.elevator.utils.RuntimeUtils;
import com.kingyon.elevator.view.NewsDetailsView;
import com.kingyon.library.social.BaseSharePramsProvider;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.utils.ScreenUtil;
import com.leo.afbaselibrary.widgets.ProgressWebView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * 新闻详情界面
 */
public class NewsDetailsActivity extends MvpBaseActivity<NewsDetailsPresenter> implements NewsDetailsView {

    @BindView(R.id.comment_list)
    NonScrollableListView comment_list;
    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.comment_container)
    LinearLayout comment_container;
    @BindView(R.id.input_comment_container)
    LinearLayout input_comment_container;
    @BindView(R.id.progress_bar)
    ProgressBar progressbar;
    @BindView(R.id.my_action_bar)
    MyActionBar my_action_bar;
    @BindView(R.id.iv_dianzan)
    ImageView iv_dianzan;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.comment_count)
    TextView comment_count;
    CommentDetailBottomSheetDialog commentDetailBottomSheetDialog;

    private int webviewHeight = 0;//webview的高度
    private NewsCommentAdapter newsCommentAdapter;
    private ShareDialog shareDialog;
    private int newsId = -1;
    NewsDetailsEntity newsDetailsEntity;
    NewsSharedEntity newsSharedEntity;
    private Boolean currentIsLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_news_details);
        ButterKnife.bind(this);
        smart_refresh_layout.setEnableAutoLoadMore(false);
        newsId = getIntent().getIntExtra("newsId", -1);
        if (newsId > 0) {
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
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    handler.proceed();
                    super.onReceivedSslError(view, handler, error);
                }

                @Override
                public void onPageFinished(WebView webView, String s) {
                    super.onPageFinished(webView, s);
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
                    initComment();
                }
            });
            my_action_bar.setRightIconClick(new OnItemClick() {
                @Override
                public void onItemClick(int position) {
                    shared();
                }
            });
            loadNewInfo();
            comment_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    showCommentDetailsDialog(presenter.getCommentEntities().get(position));
                }
            });
            smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    presenter.loadCommentList(ReflashConstants.LoadMore, newsId, 1);
                }
            });
        } else {
            finish();
        }
    }


    /**
     * 加载新闻数据
     */
    private void loadNewInfo() {
        NetService.getInstance().getNewsInfo(newsId)
                .subscribe(new CustomApiCallback<NewsDetailsEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("新闻加载失败：" + GsonUtils.toJson(ex));
                        showShortToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(NewsDetailsEntity detailsEntity) {
                        newsDetailsEntity = detailsEntity;
                        currentIsLike = detailsEntity.isLike();
                        comment_count.setText("全部评论(" + detailsEntity.getCommentCount() + ")条");
                        //webview.loadUrl("http://baijiahao.baidu.com/s?id=1659006947390217662");
                        webview.loadUrl(detailsEntity.getContentUrl());
                        if (detailsEntity.isLike()) {
                            iv_dianzan.setImageResource(R.mipmap.details_yishoucang);
                        } else {
                            iv_dianzan.setImageResource(R.mipmap.details_shoucanganniu);
                        }
                    }
                });
    }


    /**
     * 点赞或取消点赞
     */
    private void setLikeOrDislike() {
        iv_dianzan.setEnabled(false);
        NetService.getInstance().setLike(newsId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("点赞失败：" + GsonUtils.toJson(ex));
                        iv_dianzan.setEnabled(true);
                        showShortToast(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String data) {
                        iv_dianzan.setEnabled(true);
                        if (currentIsLike) {
                            currentIsLike = false;
                            iv_dianzan.setImageResource(R.mipmap.details_shoucanganniu);
                        } else {
                            currentIsLike = true;
                            iv_dianzan.setImageResource(R.mipmap.details_yishoucang);
                        }
                    }
                });
    }


    @OnClick({R.id.input_comment_container, R.id.iv_share_news, R.id.iv_dianzan})
    public void OnClick(View view) {
        if (QuickClickUtils.isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.input_comment_container:
                InputCommentActivity.openEditor(NewsDetailsActivity.this, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(NewsDetailsActivity.this);
                    }

                    @Override
                    public void onSubmit(String content) {
                        showShortToast(content);
                        presenter.addNewsComment((long) newsId, content);

                    }

                    @Override
                    public void onAttached(ViewGroup rootView) {

                    }
                });
                break;
            case R.id.iv_share_news:
                shared();
                break;
            case R.id.iv_dianzan:
                setLikeOrDislike();
                break;
        }

    }

    private void shared() {
        if (newsSharedEntity == null) {
            newsSharedEntity = new NewsSharedEntity();
            newsSharedEntity.setShareContent(newsDetailsEntity.getSummary());
            newsSharedEntity.setShareLink(newsDetailsEntity.getShareUrl());
            newsSharedEntity.setShareTitle(newsDetailsEntity.getTitle());
        }
        if (shareDialog == null) {
            BaseSharePramsProvider<NewsSharedEntity> baseSharePramsProvider = new BaseSharePramsProvider<>(NewsDetailsActivity.this);
            baseSharePramsProvider.setShareEntity(newsSharedEntity);
            shareDialog = new ShareDialog(NewsDetailsActivity.this, baseSharePramsProvider);
        }
        shareDialog.show();
    }

    @Override
    public NewsDetailsPresenter initPresenter() {
        return new NewsDetailsPresenter(this);
    }

    private void initComment() {
        if (newsCommentAdapter == null) {
            newsCommentAdapter = new NewsCommentAdapter(this, presenter.getCommentEntities());
            comment_list.setAdapter(newsCommentAdapter);
            newsCommentAdapter.setBaseOnItemClick(new BaseOnItemClick<CommentEntity>() {
                @Override
                public void onItemClick(CommentEntity data, int position) {
                    presenter.addLikeComment(data.getId(), position);
                }
            });
            presenter.loadCommentList(ReflashConstants.Refalshing, newsId, 1);
        }
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


    private void showCommentDetailsDialog(CommentEntity commentEntity) {
        LogUtils.d("评论id：" + commentEntity.getId());
        if (commentDetailBottomSheetDialog != null) {
            commentDetailBottomSheetDialog.dismiss();
            commentDetailBottomSheetDialog = null;
        }
        commentDetailBottomSheetDialog = new CommentDetailBottomSheetDialog(this, newsId, commentEntity);
        commentDetailBottomSheetDialog.show();
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
        smart_refresh_layout.finishLoadMore();
    }

    @Override
    public void showListData(List<CommentEntity> incomeDetailsEntities) {
        if (newsCommentAdapter != null) {
            newsCommentAdapter.reflashData(incomeDetailsEntities);
        }
    }

    @Override
    public void loadMoreIsComplete() {
        LogUtils.d("全部加载完成了-----------------");
        smart_refresh_layout.setEnableLoadMore(false);
    }

    @Override
    public void commentAddSuccess() {
        smart_refresh_layout.setEnableLoadMore(true);
        //smart_refresh_layout.autoLoadMore();
        presenter.loadCommentList(ReflashConstants.LoadMore,newsId, 1);
    }

    @Override
    public void addLikeCommentSuccess(int position) {
        try {
            if (presenter.getCommentEntities().get(position).isLike()) {
                presenter.getCommentEntities().get(position).setLike(false);
            } else {
                presenter.getCommentEntities().get(position).setLike(true);
            }
            newsCommentAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
