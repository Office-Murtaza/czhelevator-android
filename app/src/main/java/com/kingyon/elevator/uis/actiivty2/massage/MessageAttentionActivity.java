package com.kingyon.elevator.uis.actiivty2.massage;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessageAttentionAdapter;
import com.kingyon.elevator.utils.utilstwo.OrdinaryActivity;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

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
public class MessageAttentionActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    MessageAttentionAdapter attentionAdapter;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    List<AttenionUserEntiy> list = new ArrayList<>();
    private int page = 1;
    private String type = "fans";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvTopTitle.setText("关注");

    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_attention;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        smartRefreshLayout.autoRefresh(100);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                list.clear();
                httpAttention(type, page);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                httpAttention(type, page);
            }
        });
    }

    private void httpAttention(String type, int page) {
        NetService.getInstance().setAttention(page, type,"")
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        hideProgress();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(MessageAttentionActivity.this, ex.getDisplayMessage(), 1000);
                            } else {
                                rvComment.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                                rlNotlogin.setVisibility(View.GONE);
                            }

                        } else if (ex.getCode() == 100200) {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rlNotlogin.setVisibility(View.VISIBLE);
                        } else {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rlNotlogin.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<AttenionUserEntiy> attenionUserEntiyConentEntity) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        addData(attenionUserEntiyConentEntity);
                        hideProgress();
                        if (attenionUserEntiyConentEntity.getContent().size()>0||page>1) {
                            rvComment.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rlNotlogin.setVisibility(View.GONE);
                        }else {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            rlNotlogin.setVisibility(View.GONE);
                        }
                    }
                });

    }

    private void addData(ConentEntity<AttenionUserEntiy> attenionUserEntiyConentEntity) {
        for (int i = 0; i < attenionUserEntiyConentEntity.getContent().size(); i++) {
            AttenionUserEntiy attenionUserEntiy = new AttenionUserEntiy();
            attenionUserEntiy = attenionUserEntiyConentEntity.getContent().get(i);
            list.add(attenionUserEntiy);
        }
        if (attentionAdapter == null || page == 1) {
            attentionAdapter = new MessageAttentionAdapter(this,type);
            attentionAdapter.addData(list);
            rvComment.setAdapter(attentionAdapter);
            rvComment.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));

        } else {
            attentionAdapter.addData(list);
            attentionAdapter.notifyDataSetChanged();
        }
    }
    @OnClick({R.id.img_top_back, R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.rl_error:
                httpAttention(type,1);
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }
}
