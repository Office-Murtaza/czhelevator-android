package com.kingyon.elevator.uis.actiivty2.content;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.LogUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.CommentListEntity;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.inputcomment.EditorCallback;
import com.kingyon.elevator.uis.activities.inputcomment.InputCommentActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.ContentCommentsAdapter;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_COMMENT_TWO;

/**
 * @Created By Admin  on 2020/5/14
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:子级评论
 */
@Route(path = ACTIVITY_COMMENT_TWO)
public class CommentReplyActivity extends BaseActivity {
    @BindView(R.id.img_bake)
    ImageView imgBake;
    @BindView(R.id.ecv_list_pl)
    RecyclerView ecvListPl;
    @Autowired
    int contentId;
    @Autowired
    int onId;
    int page = 1;
    @BindView(R.id.tv_comments_number)
    TextView tvCommentsNumber;
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
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;

    @Override
    public int getContentViewId() {
        return R.layout.activity_comment_reply;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        httpCommentBy(page, contentId, onId);

        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpCommentBy(page, contentId, onId);
            }
        });
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                listEntities.clear();
                httpCommentBy(page, contentId, onId);
            }
        });


    }

    private void httpCommentBy(int page, int contentId, int onId) {
        NetService.getInstance().setCommentBy(page, contentId, onId)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<CommentListEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getCode(), ex.getDisplayMessage());
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);

                    }

                    @Override
                    public void onNext(ConentEntity<CommentListEntity> conentEntity) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        dataAdd(conentEntity);
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
            contentCommentsAdapter = new ContentCommentsAdapter(CommentReplyActivity.this, "2",
                    new ContentCommentsAdapter.GetRefresh() {
                        @Override
                        public void onRefresh(boolean isSucced) {
                            listEntities.clear();
                            page = 1;
                            httpCommentBy(page, contentId, onId);
                        }
                    });
            contentCommentsAdapter.addData(listEntities);
            ecvListPl.setAdapter(contentCommentsAdapter);
            ecvListPl.setLayoutManager(new GridLayoutManager(CommentReplyActivity.this, 1, GridLayoutManager.VERTICAL, false));
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


    @OnClick({R.id.img_bake, R.id.img_icon, R.id.input_comment, R.id.btn_send, R.id.iv_dianzan, R.id.iv_share_news})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_bake:
                finish();
                break;
            case R.id.img_icon:

                break;
            case R.id.input_comment:
                InputCommentActivity.openEditor(CommentReplyActivity.this, new EditorCallback() {
                    @Override
                    public void onCancel() {
                        LogUtils.d("关闭输入法-------------");
                        KeyboardUtils.hideSoftInput(CommentReplyActivity.this);
                    }

                    @Override
                    public void onSubmit(String content) {
                        ConentUtils.httpComment(CommentReplyActivity.this,
                                contentId, onId, content, new ConentUtils.IsSuccedListener() {
                                    @Override
                                    public void onisSucced(boolean isSucced) {
                                        if (isSucced) {
                                            httpCommentBy(page, contentId, onId);
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
            case R.id.btn_send:

                break;
            case R.id.iv_dianzan:
                break;
            case R.id.iv_share_news:
                break;
        }
    }
}
