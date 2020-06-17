package com.kingyon.elevator.uis.actiivty2.content;

import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bobomee.android.mentions.text.MentionTextView;
import com.czh.myversiontwo.utils.QuickClickUtils;
import com.google.gson.Gson;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.uis.actiivty2.input.Parser;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.dialogs.CommentDialog;
import com.kingyon.elevator.uis.dialogs.DeleteShareDialog;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.SharedUtils;
import com.kingyon.elevator.utils.utilstwo.TokenUtils;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.CANCEL_LIKE;
import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.CodeType.LIKE;
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
    MentionTextView tvContent;
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
    @Autowired
    String conentEntity;
    QueryRecommendEntity recommendEntity;
    private ShareDialog shareDialog;
    int page = 1;
    @Override
    public int getContentViewId() {
        return R.layout.activity_voide_vertical;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        ARouter.getInstance().inject(this);
        Gson gson = new Gson();
        recommendEntity = gson.fromJson(conentEntity, QueryRecommendEntity.class);

        ConentUtils.httpAddBrowse(this,recommendEntity.id);
        Parser mTagParser = new Parser();
        tvContent.setMovementMethod(new LinkMovementMethod());
        tvContent.setParserConverter(mTagParser);

        tvName.setText(recommendEntity.nickname+"");
        tvTitle.setText(recommendEntity.title+"");
        tvContent.setText(recommendEntity.content);
        tvLikeNumer.setText(recommendEntity.likes+"");
        tvCommentsNumber.setText(recommendEntity.comments+"");
        GlideUtils.loadRoundImage(this, recommendEntity.photo, imgPortrait,20);
//        httpComment(page,recommendEntity.id);
        if (recommendEntity.isAttent==0){
            tvAttention.setText("关注");
            tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention));
            tvAttention.setTextColor(Color.parseColor("#ffffff"));

        }else {
            tvAttention.setText("已关注");
            tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
            tvAttention.setTextColor(Color.parseColor("#FF1330"));

        }
        if (recommendEntity.original){
            tvOriginal.setText("原创");
        }else {
            tvOriginal.setText("转载");
        }
        if (recommendEntity.liked){
            imgLike.setImageResource(R.mipmap.btn_big_like_off );
        }else {
            imgLike.setImageResource(R.mipmap.btn_big_like);
        }

        rlTop.bringToFront();
        videoView.setUp(recommendEntity.video, true, "");
        videoView.getBackButton().setVisibility(View.GONE);
        videoView.getFullscreenButton().setVisibility(View.GONE);
        videoView.startPlayLogic();
    }

    private void httpComment(int page, int id) {

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

    @OnClick({R.id.img_bake, R.id.img_jb, R.id.btn_send,
            R.id.iv_dianzan, R.id.iv_share_news, R.id.img_like,
            R.id.img_comments, R.id.img_collect, R.id.img_share,
            R.id.input_comment,R.id.tv_attention})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_jb:
                if (TokenUtils.isToken(this)){
                    if (TokenUtils.isCreateAccount(recommendEntity.createAccount)){
                        /*删除*/
                        DeleteShareDialog deleteShareDialog = new DeleteShareDialog(this,recommendEntity.id,null,"2",0,null,null);
                        deleteShareDialog.show();
                    }else {
                        /*举报*/
                        ReportShareDialog reportShareDialog = new ReportShareDialog(this,recommendEntity.id,HOME_CONTENT);
                        reportShareDialog.show();
                    }
                }
                break;
            case R.id.btn_send:

                break;
            case R.id.iv_dianzan:
                break;
            case R.id.iv_share_news:

                break;
            case R.id.img_like:
                if (recommendEntity.liked){
                    imgLike.setImageResource(R.mipmap.btn_big_like);
                    ConentUtils.httpHandlerLikeOrNot(this,recommendEntity.id,
                            HOME_CONTENT,CANCEL_LIKE,0,recommendEntity,"2");
                }else {
                    imgLike.setImageResource(R.mipmap.btn_big_like_off);
                    ConentUtils.httpHandlerLikeOrNot(this,recommendEntity.id,
                            HOME_CONTENT,LIKE,0,recommendEntity,"2");
                }
                break;
            case R.id.img_comments:
                /*评论*/
                if (QuickClickUtils.isFastClick()) {
                    CommentDialog commentDialog = new CommentDialog(this, recommendEntity.id);
                    commentDialog.show();
                }

                break;
            case R.id.img_collect:
                /*收藏*/

                break;
            case R.id.img_share:
                /*分享*/
                SharedUtils.shared(this,shareDialog,recommendEntity.content,"www。baidu.com",recommendEntity.title);
                break;
            case R.id.input_comment:
                /*评论*/

                InputCommentActivity.openEditor(VoideVerticalDetailsActivity.this, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(VoideVerticalDetailsActivity.this);
                    }
                    @Override
                    public void onSubmit(String content) {
                        ConentUtils.httpComment(VoideVerticalDetailsActivity.this,
                                recommendEntity.id, 0, content, new ConentUtils.IsSuccedListener() {
                                    @Override
                                    public void onisSucced(boolean isSucced) {
                                        if (isSucced){
                                            httpComment(1,recommendEntity.id);
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
            case R.id.tv_attention:
                httpAddUser();
                break;
                default:
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        GSYVideoManager.onPause();
    }

    private void httpAddUser() {
        if (tvAttention.getText().toString().equals("关注")){
            ConentUtils.httpAddAttention(this,"add",recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(VoideVerticalDetailsActivity.this,"关注成功",100);
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                    tvAttention.setTextColor(Color.parseColor("#FF1330"));


                }
                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(VoideVerticalDetailsActivity.this,magssger+code,100);
                }
            });
        }else {
            ConentUtils.httpAddAttention(this,"cancel",recommendEntity.createAccount, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    ToastUtils.showToast(VoideVerticalDetailsActivity.this,"取消关注成功",100);
                    tvAttention.setText("关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention));
                    tvAttention.setTextColor(Color.parseColor("#ffffff"));
                }
                @Override
                public void onErron(String magssger, int code) {
                    ToastUtils.showToast(VoideVerticalDetailsActivity.this,magssger+code,100);
                }
            });
        }

    }
}
