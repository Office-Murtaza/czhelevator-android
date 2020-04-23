package com.kingyon.elevator.uis.actiivty2.content;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.NewsDetailsEntity;
import com.kingyon.elevator.entities.NewsSharedEntity;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentCommentsAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentImageAdapter;
import com.kingyon.library.social.BaseSharePramsProvider;
import com.kingyon.library.social.DeleteShareDialog;
import com.kingyon.library.social.ShareDialog;
import com.leo.afbaselibrary.uis.activities.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_CONTENT_DRTAILS;

/**
 * Created By Admin  on 2020/4/16
 * Email : 163235610@qq.com
 * @Author:Mrczh
 * Instructions:内容详情
 */
@Route(path = ACTIVITY_MAIN2_CONTENT_DRTAILS)
public class ContentDetailsActivity extends BaseActivity {
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
    @BindView(R.id.tv_like_comments)
    TextView tvLikeComments;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.ecv_list_image)
    RecyclerView ecvListImage;
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
    NewsSharedEntity newsSharedEntity;
    NewsDetailsEntity newsDetailsEntity;
    private ShareDialog shareDialog;
    @Override
    public int getContentViewId() {
        return R.layout.activity_conent_details;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ContentImageAdapter contentImageAdapter = new ContentImageAdapter(this);
        ecvListImage.setLayoutManager(new LinearLayoutManager(this));
        ecvListImage.setAdapter(contentImageAdapter);
        ecvListImage.setNestedScrollingEnabled(false);
        ContentCommentsAdapter contentCommentsAdapter = new ContentCommentsAdapter(this);
        ecvListPl.setLayoutManager(new LinearLayoutManager(this));
        ecvListPl.setAdapter(contentCommentsAdapter);
        ecvListPl.setNestedScrollingEnabled(false);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.img_bake, R.id.img_portrait, R.id.tv_name, R.id.tv_attention, R.id.input_comment_container, R.id.iv_share_news, R.id.im_collection, R.id.iv_like,R.id.img_jb})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_portrait:

                break;
            case R.id.tv_name:
                break;
            case R.id.tv_attention:
                break;
            case R.id.input_comment_container:
                InputCommentActivity.openEditor(ContentDetailsActivity.this, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(ContentDetailsActivity.this);
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
            case R.id.iv_share_news:
                shared();
                break;
            case R.id.im_collection:

                break;
            case R.id.iv_like:

                break;
            case R.id.img_jb:
                DeleteShareDialog deleteShareDialog = new DeleteShareDialog(this);
                deleteShareDialog.show();
                break;
                default:
        }
    }

    private void shared() {
//        if (newsSharedEntity == null) {
//            newsSharedEntity = new NewsSharedEntity();
//            newsSharedEntity.setShareContent(newsDetailsEntity.getSummary());
//            newsSharedEntity.setShareLink(newsDetailsEntity.getShareUrl());
//            newsSharedEntity.setShareTitle(newsDetailsEntity.getTitle());
//            LogUtils.e(newsDetailsEntity.getSummary(),newsDetailsEntity.getShareUrl(),newsDetailsEntity.getTitle());
//        }
        if (shareDialog == null) {
            BaseSharePramsProvider<NewsSharedEntity> baseSharePramsProvider = new BaseSharePramsProvider<>(ContentDetailsActivity.this);
            baseSharePramsProvider.setShareEntity(newsSharedEntity);
            shareDialog = new ShareDialog(ContentDetailsActivity.this, baseSharePramsProvider);
        }
        shareDialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
