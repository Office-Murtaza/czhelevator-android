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
import com.kingyon.elevator.presenter.presenter2.MessageAttentionPresenter;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessageAttentionAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_ATTENTION;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息关注
 */
@Route(path = ACTIVITY_MASSAGE_ATTENTION)
public class MessageAttentionActivity extends MvpBaseActivity<MessageAttentionPresenter> {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    MessageAttentionAdapter attentionAdapter;
    @Override
    public MessageAttentionPresenter initPresenter() {
        return new MessageAttentionPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_attention);
        ButterKnife.bind(this);
        tvTopTitle.setText("关注");
        rvComment.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        attentionAdapter = new MessageAttentionAdapter(this,20);
        rvComment.setAdapter(attentionAdapter);
    }

    @OnClick(R.id.img_top_back)
    public void onViewClicked() {
        finish();
    }
}
