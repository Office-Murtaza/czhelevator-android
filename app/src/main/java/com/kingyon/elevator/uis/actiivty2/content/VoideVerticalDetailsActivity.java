package com.kingyon.elevator.uis.actiivty2.content;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS;

/**
 * Created By Admin  on 2020/4/17
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:视频竖版
 */
@Route(path = ACTIVITY_MAIN2_VOIDEVERTICAL_DRTAILS)
public class VoideVerticalDetailsActivity extends BaseActivity {
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
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.tv_original)
    TextView tvOriginal;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.img_icon)
    ImageView imgIcon;
    @BindView(R.id.input_comment)
    TextView inputComment;
    @BindView(R.id.btn_send)
    TextView btnSend;
    @BindView(R.id.iv_dianzan)
    ImageView ivDianzan;
    @BindView(R.id.iv_share_news)
    ImageView ivShareNews;
    @BindView(R.id.img_like)
    ImageView imgLike;
    @BindView(R.id.tv_like_numer)
    TextView tvLikeNumer;
    @BindView(R.id.img_comments)
    ImageView imgComments;
    @BindView(R.id.tv_comments_number)
    TextView tvCommentsNumber;
    @BindView(R.id.img_collect)
    ImageView imgCollect;
    @BindView(R.id.img_share)
    ImageView imgShare;
    @BindView(R.id.video_view)
    StandardGSYVideoPlayer videoView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_voide_vertical;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        rlTop.bringToFront();
        videoView.setUp("http://cdn.tlwgz.com/FmR2tIv-8FwdXiSVNE9wQODoZk9E", true, "");
        videoView.getBackButton().setVisibility(View.GONE);
        videoView.getFullscreenButton().setVisibility(View.GONE);
        videoView.startPlayLogic();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        transparentStatusBar();
    }

    @OnClick({R.id.img_bake, R.id.img_jb, R.id.btn_send, R.id.iv_dianzan, R.id.iv_share_news, R.id.img_like, R.id.img_comments, R.id.img_collect, R.id.img_share,R.id.input_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_jb:

                break;
            case R.id.btn_send:

                break;
            case R.id.iv_dianzan:
                break;
            case R.id.iv_share_news:
                break;
            case R.id.img_like:
                break;
            case R.id.img_comments:
                break;
            case R.id.img_collect:
                break;
            case R.id.img_share:
                break;
            case R.id.input_comment:
                InputCommentActivity.openEditor(VoideVerticalDetailsActivity.this, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(VoideVerticalDetailsActivity.this);
                    }

                    @Override
                    public void onSubmit(String content) {
//                        presenter.addNewsComment((long) newsId, content);

                    }

                    @Override
                    public void onAttached(ViewGroup rootView) {

                    }
                });
                break;
                default:
        }
    }
}
