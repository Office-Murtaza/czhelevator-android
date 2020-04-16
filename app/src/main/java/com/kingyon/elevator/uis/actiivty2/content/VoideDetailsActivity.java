package com.kingyon.elevator.uis.actiivty2.content;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adapter2.ContentCommentsAdapter;
import com.kingyon.library.social.ReportShareDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.kingyon.elevator.utils.Constance.ACTIVITY_MAIN2_VIDEO_DRTAILS;


/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:视频详情
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
    TextView tvContent;
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

    @Override
    public int getContentViewId() {
        return R.layout.activity_voide_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        ContentCommentsAdapter contentCommentsAdapter = new ContentCommentsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ecvListPl.setLayoutManager(linearLayoutManager);
        ecvListPl.setAdapter(contentCommentsAdapter);
        ecvListPl.setNestedScrollingEnabled(false);
        video.setUp("http://cdn.tlwgz.com/FmR2tIv-8FwdXiSVNE9wQODoZk9E", true, "");
        video.getBackButton().setVisibility(View.GONE);
        video.getFullscreenButton().setVisibility(View.GONE);
        video.startPlayLogic();

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
                break;
            case R.id.img_jb:
                ReportShareDialog reportShareDialog = new ReportShareDialog(this);
                reportShareDialog.show();
                break;
            case R.id.img_screen:
                break;
            case R.id.tv_like_comments:
                break;
            case R.id.input_comment_container:
                break;
            case R.id.iv_share_news:

                break;
            case R.id.im_collection:
                break;
            case R.id.iv_like:
                break;
        }
    }

}
