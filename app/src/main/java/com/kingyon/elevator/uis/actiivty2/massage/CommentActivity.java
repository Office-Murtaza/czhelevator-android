package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.kingyon.elevator.R;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.presenter.presenter2.MessageCommentPresenterz;
import com.kingyon.elevator.uis.adapters.adaptertwo.MassageCommentAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_COMMENT;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息评论
 */
@Route(path = ACTIVITY_MASSAGE_COMMENT)
public class CommentActivity extends MvpBaseActivity<MessageCommentPresenterz> {

    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;

    MassageCommentAdapter massageCommentAdapter;
    @Override
    public MessageCommentPresenterz initPresenter() {
        return new MessageCommentPresenterz(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        tvTopTitle.setText("@和评论");
        rvComment.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        massageCommentAdapter = new MassageCommentAdapter(this,20);
        rvComment.setAdapter(massageCommentAdapter);
    }


    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}
