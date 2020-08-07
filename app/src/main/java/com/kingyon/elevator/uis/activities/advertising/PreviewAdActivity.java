package com.kingyon.elevator.uis.activities.advertising;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.kingyon.elevator.R;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.PREVIEW_AD_ACTIVITY;

/**
 * @Created By Admin  on 2020/8/7
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:本地内容预览
 */
@Route(path = PREVIEW_AD_ACTIVITY)
public class PreviewAdActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.video_view)
    StandardGSYVideoPlayer videoView;
    @BindView(R.id.img_view)
    ImageView imgView;
    @Autowired
    String path;
    @Autowired
    String type;

    @Override
    public int getContentViewId() {
        return R.layout.activity_preview_local;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        if (type.equals("video")){
            videoView.setVisibility(View.VISIBLE);
            imgView.setVisibility(View.GONE);
            videoView.setUp(path, true, "");
            videoView.getBackButton().setVisibility(View.GONE);
            videoView.getFullscreenButton().setVisibility(View.GONE);
            videoView.startPlayLogic();
        }else {
            GlideUtils.loadImage(this,path,imgView);
            videoView.setVisibility(View.GONE);
            imgView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        GSYVideoManager.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GSYVideoManager.releaseAllVideos();
    }

}
