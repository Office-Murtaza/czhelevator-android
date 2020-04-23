package com.kingyon.elevator.uis.actiivty2.content;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentCommentsAdapter;
import com.kingyon.library.social.ReportShareDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_ARTICLE_DRTAILS;

/**
 * Created By Admin  on 2020/4/17
 * Email : 163235610@qq.com
 * @author:Mrczh
 * Instructions: 文章详情
 *
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

    @Override
    public int getContentViewId() {
        return R.layout.activity_article_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {

        ContentCommentsAdapter contentCommentsAdapter = new ContentCommentsAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        ecvListPl.setLayoutManager(linearLayoutManager);
        ecvListPl.setAdapter(contentCommentsAdapter);
        ecvListPl.setNestedScrollingEnabled(false);
        webview.loadUrl("https://www.baidu.com/");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @SuppressWarnings("AlibabaSwitchStatement")
    @OnClick({R.id.img_bake, R.id.img_jb, R.id.input_comment_container, R.id.iv_share_news, R.id.im_collection, R.id.iv_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_jb:
                ReportShareDialog reportShareDialog = new ReportShareDialog(this);
                reportShareDialog.show();
                break;
            case R.id.input_comment_container:
                break;
            case R.id.iv_share_news:
                break;
            case R.id.im_collection:
                break;
            case R.id.iv_like:
                break;
                default:
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
