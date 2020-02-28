package com.kingyon.elevator.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.ReflashConstants;
import com.kingyon.elevator.entities.CommentEntity;
import com.kingyon.elevator.entities.NewsEntity;
import com.kingyon.elevator.interfaces.BaseOnItemClick;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.NewsDetailsActivity;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.adapters.NewsCommentAdapter;
import com.kingyon.elevator.uis.adapters.NewsReplyCommentAdapter;
import com.kingyon.elevator.utils.DialogUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created By SongPeng  on 2020/2/27
 * Email : 1531603384@qq.com
 */
public class CommentDetailBottomSheetDialog extends BottomSheetDialog {

    @BindView(R.id.reply_list_container)
    ListView reply_list_container;

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.user_head)
    CircleImageView user_head;
    @BindView(R.id.user_name)
    TextView user_name;
    @BindView(R.id.iv_dianzan)
    ImageView iv_dianzan;
    @BindView(R.id.comment_content)
    TextView comment_content;
    @BindView(R.id.comment_time)
    TextView comment_time;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smart_refresh_layout;
    @BindView(R.id.comment_container)
    LinearLayout comment_container;


    private NewsReplyCommentAdapter newsCommentAdapter;
    private List<CommentEntity> commentEntities;
    private Context mContext;

    BottomSheetBehavior mDialogBehavior;
    int newsid;
    CommentEntity commentEntity;
    private int startPosition = 0;
    private int size = 20;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_comment_details_dialog_layout);
        ButterKnife.bind(this);
        commentEntities = new ArrayList<>();
        newsCommentAdapter = new NewsReplyCommentAdapter(getContext(), commentEntities);
        reply_list_container.setAdapter(newsCommentAdapter);
        reply_list_container.setOnTouchListener((v, event) -> {
            if (!reply_list_container.canScrollVertically(-1)) {      //canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
                reply_list_container.requestDisallowInterceptTouchEvent(false);
            } else {
                reply_list_container.requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });
        newsCommentAdapter.setBaseOnItemClick(new BaseOnItemClick<CommentEntity>() {
            @Override
            public void onItemClick(CommentEntity data, int position) {
                addLikeComment(data.getId(), 2, position);
            }
        });
        reply_list_container.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goReply(commentEntities.get(position));
            }
        });
        smart_refresh_layout.setEnableAutoLoadMore(false);
        smart_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                loadCommentList(ReflashConstants.LoadMore, commentEntity.getId());
            }
        });
        GlideUtils.loadImage(getContext(), commentEntity.getPhotoUrl(), user_head);
        user_name.setText(commentEntity.getNickname());
        comment_content.setText(commentEntity.getComment());
        comment_time.setText(commentEntity.getShowTime());
        if (commentEntity.isLike()) {
            iv_dianzan.setImageResource(R.mipmap.details_shoucangtubiaoyi);
        } else {
            iv_dianzan.setImageResource(R.mipmap.details_shoucangtubiaosan);
        }
        if (commentEntity.getComCount() > 0) {
            title.setText(commentEntity.getComCount() + "回复");
        } else {
            title.setText("暂无回复");
        }
        loadCommentList(ReflashConstants.LoadMore, commentEntity.getId());
    }

    public CommentDetailBottomSheetDialog(@NonNull Context context, int newsid, CommentEntity commentEntity) {
        super(context, R.style.BottomSheetEdit);
        this.newsid = newsid;
        this.commentEntity = commentEntity;
        this.mContext = context;
    }


    /**
     * 去回复子评论
     */
    private void goReply(CommentEntity commentEntity) {
        InputCommentActivity.openEditor(getContext(), new EditorCallback() {
            @Override
            public void onCancel() {
                LogUtils.d("关闭输入法-------------");
                KeyboardUtils.hideSoftInput((NewsDetailsActivity) mContext);
            }

            @Override
            public void onSubmit(String content) {
                LogUtils.d("回复子评论：" + content);
                KeyboardUtils.hideSoftInput((NewsDetailsActivity) mContext);
                addNewsReplySonComment((long) newsid, commentEntity, content);
            }

            @Override
            public void onAttached(ViewGroup rootView) {

            }
        });
    }

    /**
     * 去回复新闻评论
     */
    private void goReplyMainComment(CommentEntity commentEntity) {
        InputCommentActivity.openEditor(getContext(), new EditorCallback() {
            @Override
            public void onCancel() {
                LogUtils.d("关闭输入法-------------");
                KeyboardUtils.hideSoftInput((NewsDetailsActivity) mContext);
            }

            @Override
            public void onSubmit(String content) {
                LogUtils.d("回复主评论：" + content);
                KeyboardUtils.hideSoftInput((NewsDetailsActivity) mContext);
                addNewsReplyMainComment((long)newsid, content);
            }

            @Override
            public void onAttached(ViewGroup rootView) {

            }
        });
    }


    @OnClick({R.id.iv_close, R.id.iv_dianzan, R.id.comment_container})
    public void Onclick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                dismiss();
                break;
            case R.id.iv_dianzan:
                addLikeComment(commentEntity.getId(), 1, -1);
                break;
            case R.id.comment_container:
                goReplyMainComment(commentEntity);
                break;
        }
    }

    @Override
    public void show() {
        super.show();
        FrameLayout bottomSheet = getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        ViewGroup.LayoutParams originLayoutParams = bottomSheet.getLayoutParams();
        originLayoutParams.width = ScreenUtils.getScreenWidth();
        originLayoutParams.height = ScreenUtils.getScreenHeight();
        bottomSheet.setLayoutParams(originLayoutParams);
        mDialogBehavior = BottomSheetBehavior.from(bottomSheet);
        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                LogUtils.d("当前的状态：" + newState);
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                    dismiss();
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    dismiss();
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        };
        mDialogBehavior.setBottomSheetCallback(bottomSheetCallback);
        mDialogBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }


    @Override
    public void setOnDismissListener(@Nullable OnDismissListener listener) {
        super.setOnDismissListener(listener);
        mDialogBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }


    public void loadCommentList(int reflashType, int commentId) {
        if (reflashType == ReflashConstants.Refalshing) {
            startPosition = 0;
        }
        NetService.getInstance().getChildListComment(commentId, startPosition, size)
                .subscribe(new CustomApiCallback<List<CommentEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        smart_refresh_layout.finishLoadMore();
                        smart_refresh_layout.finishRefresh();
                    }

                    @Override
                    public void onNext(List<CommentEntity> incomeDetailsEntities) {
                        smart_refresh_layout.finishLoadMore();
                        if (reflashType == ReflashConstants.Refalshing) {
                            commentEntities = incomeDetailsEntities;
                        } else {
                            commentEntities.addAll(incomeDetailsEntities);
                            if (incomeDetailsEntities.size() == 0) {
                                smart_refresh_layout.setEnableLoadMore(false);
                            }
                        }
                        startPosition = commentEntities.size();
                        LogUtils.d("下一次加载更多开始位置：" + startPosition, "数据长度：" + incomeDetailsEntities.size());
                        showListData(commentEntities);

                    }
                });
    }

    public void showListData(List<CommentEntity> incomeDetailsEntities) {
        if (newsCommentAdapter != null) {
            newsCommentAdapter.reflashData(incomeDetailsEntities);
        }
    }


    /**
     * 回复一条子评论
     *
     * @param newsId
     * @param content
     */
    public void addNewsReplySonComment(Long newsId, CommentEntity replayComment, String content) {
        DialogUtils.getInstance().showProgressDialogView(mContext, "数据提交中...", false);
        NetService.getInstance().addComment(newsId, (long) commentEntity.getId(), (long) replayComment.getId(), 2, content)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("添加子回复失败：" + GsonUtils.toJson(ex));
                        DialogUtils.getInstance().hideProgressDialogView();
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String content) {
                        DialogUtils.getInstance().hideProgressDialogView();
                        ToastUtils.showShort("回复成功");
                        smart_refresh_layout.setEnableLoadMore(true);
                        loadCommentList(ReflashConstants.Refalshing, commentEntity.getId());
                    }
                });
    }


    /**
     * 回复一条主评论
     *
     * @param newsId
     * @param content
     */
    public void addNewsReplyMainComment(Long newsId, String content) {
        DialogUtils.getInstance().showProgressDialogView(mContext, "数据提交中...", false);
        NetService.getInstance().addComment(newsId, (long) commentEntity.getId(), null, 2, content)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.d("添加子评论失败：" + GsonUtils.toJson(ex));
                        DialogUtils.getInstance().hideProgressDialogView();
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String content) {
                        DialogUtils.getInstance().hideProgressDialogView();
                        ToastUtils.showShort("评论成功");
                        smart_refresh_layout.setEnableLoadMore(true);
                        loadCommentList(ReflashConstants.Refalshing, commentEntity.getId());
                    }
                });
    }


    /**
     * 点赞评论
     */
    public void addLikeComment(int commentId, int from, int position) {
        NetService.getInstance().addLikeComment((long) commentId)
                .subscribe(new CustomApiCallback<String>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        ToastUtils.showShort(ex.getDisplayMessage());
                    }

                    @Override
                    public void onNext(String content) {
                        if (mContext != null) {
                            if (from == 1) {
                                //点赞的为主评论
                                if (commentEntity.isLike()) {
                                    commentEntity.setLike(false);
                                } else {
                                    commentEntity.setLike(true);
                                }
                            } else {
                                //点赞的为列表里的评论，需要设置相应的数据然后更新数据集
                                if (commentEntities.get(position).isLike()) {
                                    commentEntities.get(position).setLike(false);
                                } else {
                                    commentEntities.get(position).setLike(true);
                                }
                            }
                            updateInfo();
                        }
                    }
                });
    }

    private void updateInfo() {
        if (commentEntity.isLike()) {
            iv_dianzan.setImageResource(R.mipmap.details_shoucangtubiaoyi);
        } else {
            iv_dianzan.setImageResource(R.mipmap.details_shoucangtubiaosan);
        }
        newsCommentAdapter.notifyDataSetChanged();
    }


    @Override
    public void dismiss() {
        super.dismiss();
        mContext = null;
    }
}
