package com.kingyon.elevator.uis.actiivty2.content;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.google.gson.Gson;
import com.kingyon.elevator.R;
import com.kingyon.elevator.constants.Constants;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
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
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.utils.GSYVideoType;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.zhaoss.weixinrecorded.util.TimeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;
import static com.kingyon.elevator.uis.fragments.main2.found.AttentionFragment.isRefresh;


/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 *
 * @Author:Mrczh Instructions:视频详情
 */
@Route(path = ACTIVITY_MAIN2_VIDEO_DRTAILS)
public class VoideDetailsActivity extends BaseActivity {
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
    @BindView(R.id.video)
    StandardGSYVideoPlayer video;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_video_time)
    TextView tvVideoTime;
    @BindView(R.id.img_screen)
    ImageView imgScreen;
    @BindView(R.id.ll_xssjcs)
    LinearLayout llXssjcs;
    @BindView(R.id.tv_original)
    TextView tvOriginal;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_like_comments)
    TextView tvLikeComments;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    MentionTextView tvContent;
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
    int page = 1;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private ShareDialog shareDialog;
    @Override
    public int getContentViewId() {
        return R.layout.activity_voide_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        isRefresh = false;
        ARouter.getInstance().inject(this);
        showProgressDialog(getString(R.string.wait));
        NetService.getInstance().setQueryContentById(String.valueOf(contentId), DataSharedPreferences.getCreatateAccount())
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<QueryRecommendEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        finish();
                        ToastUtils.showToast(VoideDetailsActivity.this,ex.getDisplayMessage(),1000);
                    }

                    @Override
                    public void onNext(QueryRecommendEntity queryRecommendEntity) {
                        recommendEntity = queryRecommendEntity;
                        ConentUtils.httpAddBrowse(VoideDetailsActivity.this, recommendEntity.id);
                        Parser mTagParser = new Parser();
                        tvContent.setMovementMethod(new LinkMovementMethod());
                        tvContent.setParserConverter(mTagParser);
                        tvName.setText(recommendEntity.nickname + "");
                        tvTime.setText(TimeUtil.getRecentlyTime(recommendEntity.createTime));
                        tvTitle.setText(recommendEntity.title + "");
                        tvContent.setText(recommendEntity.content);
                        tvLikeComments.setText(String.format("%s 点赞    %s 评论    ", recommendEntity.likes, recommendEntity.comments));
                        tvCommentsNumber.setText(String.format("%s条评论", recommendEntity.comments));
                        tvNumber.setText(recommendEntity.browseTimes + "次播放");
                        tvVideoTime.setText(TimeUtils.secondToTime(recommendEntity.playTime / 1000) + "");
                        GlideUtils.loadCircleImage(VoideDetailsActivity.this, recommendEntity.photo, imgPortrait);
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
                        if (recommendEntity.isCollect > 0) {
                            imCollection.setImageResource(R.mipmap.btn_big_collect_off);
                        } else {
                            imCollection.setImageResource(R.mipmap.btn_big_collect);
                        }

                        httpComment(page, recommendEntity.id);

                        if (recommendEntity.liked) {
                            ivLike.setImageResource(R.mipmap.btn_big_like_off);
                        } else {
                            ivLike.setImageResource(R.mipmap.btn_big_like);
                        }

//        video.startWindowFullscreen(this, false, false);
                        video.setUp(recommendEntity.video, true, "");
                        video.getBackButton().setVisibility(View.GONE);
                        video.getFullscreenButton().setVisibility(View.GONE);
                        video.startPlayLogic();

                        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                            @Override
                            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                                page++;
                                httpComment(page, recommendEntity.id);
                            }
                        });
                    hideProgress();
                    }
                });

    }

    private void httpComment(int page, int id) {
        NetService.getInstance().setQueryListComment(page, id)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        smartRefreshLayout.finishLoadMore();
//                        ToastUtils.showToast(VoideDetailsActivity.this,ex.getDisplayMessage(),1000);
                    }

                    @Override
                    public void onNext(ConentEntity<CommentListEntity> conentEntity) {
                        dataAdd(conentEntity);
                        smartRefreshLayout.finishLoadMore();
//                        ContentCommentsAdapter contentCommentsAdapter = new ContentCommentsAdapter(VoideDetailsActivity.this, conentEntity, "1", new ContentCommentsAdapter.GetRefresh() {
//                            @Override
//                            public void onRefresh(boolean isSucced) {
//                                httpComment(page,recommendEntity.id);
//                            }
//                        });
//                        ecvListPl.setLayoutManager(new LinearLayoutManager(VoideDetailsActivity.this));
//                        ecvListPl.setAdapter(contentCommentsAdapter);
//                        ecvListPl.setNestedScrollingEnabled(false);
                    }
                });
    }

    List<CommentListEntity> listEntities = new ArrayList<>();
    ContentCommentsAdapter contentCommentsAdapter;

    private void dataAdd(ConentEntity<CommentListEntity> conentEntity) {

        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            CommentListEntity commentListEntity = new CommentListEntity();
            commentListEntity = conentEntity.getContent().get(i);
            listEntities.add(commentListEntity);
        }
        if (contentCommentsAdapter == null || page == 1) {
            ecvListPl.setNestedScrollingEnabled(false);
            ecvListPl.setFocusable(false);
            contentCommentsAdapter = new ContentCommentsAdapter(VoideDetailsActivity.this, "1",
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
            ecvListPl.setLayoutManager(new GridLayoutManager(VoideDetailsActivity.this, 1, GridLayoutManager.VERTICAL, false));
        } else {
            contentCommentsAdapter.addData(listEntities);
            contentCommentsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_bake, R.id.tv_attention, R.id.img_jb, R.id.img_screen, R.id.tv_like_comments, R.id.input_comment_container, R.id.iv_share_news, R.id.im_collection, R.id.iv_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.tv_attention:
                httpAddUser();
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
                }
                break;
            case R.id.img_screen:

                GSYVideoType.setShowType(GSYVideoType.SCREEN_MATCH_FULL);

                break;
            case R.id.tv_like_comments:
                break;
            case R.id.input_comment_container:

                InputCommentActivity.openEditor(VoideDetailsActivity.this, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(VoideDetailsActivity.this);
                    }

                    @Override
                    public void onSubmit(String content) {
                        ConentUtils.httpComment(VoideDetailsActivity.this,
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
                                ToastUtils.showToast(VoideDetailsActivity.this, "收藏成功", 1000);
                                LogUtils.e("收藏成功");
                                recommendEntity.isCollect = 1;
                            } else {
                                ToastUtils.showToast(VoideDetailsActivity.this, "收藏失败", 1000);
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
                                ToastUtils.showToast(VoideDetailsActivity.this, "取消收藏成功", 1000);
                            } else {
                                ToastUtils.showToast(VoideDetailsActivity.this, "取消收藏失败", 1000);

                            }
                        }
                    });
                }
                break;
            case R.id.iv_like:
                LogUtils.e(recommendEntity.liked);
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
            default:
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    private void httpAddUser() {
        if (tvAttention.getText().toString().equals("关注")) {
            ConentUtils.httpAddAttention(this, "add", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(VoideDetailsActivity.this, "关注成功", 100);
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                    tvAttention.setTextColor(Color.parseColor("#FF1330"));


                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(VoideDetailsActivity.this, magssger + code, 100);
                }
            });
        } else {
            ConentUtils.httpAddAttention(this, "cancel", recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(VoideDetailsActivity.this, "取消关注成功", 100);
                    tvAttention.setText("关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention));
                    tvAttention.setTextColor(Color.parseColor("#ffffff"));
                }

                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(VoideDetailsActivity.this, magssger + code, 100);
                }
            });
        }

    }
}
