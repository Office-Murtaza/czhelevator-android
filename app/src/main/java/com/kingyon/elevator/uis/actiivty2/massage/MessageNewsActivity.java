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
import com.kingyon.elevator.entities.entities.MassageListMentiy;
import com.kingyon.elevator.mvpbase.MvpBaseActivity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.presenter.presenter2.MessageNewsPresenter;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessageNewsAdapter;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
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

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_MSAGGER;

/**
 * @Created By Admin  on 2020/4/23
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息
 */
@Route(path = ACTIVITY_MASSAGE_MSAGGER)
public class MessageNewsActivity extends MvpBaseActivity<MessageNewsPresenter> {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    MessageNewsAdapter messageNewsAdapter;
    int page = 1;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    List<MassageListMentiy> listMentiys = new ArrayList<>();
    @Override
    public MessageNewsPresenter initPresenter() {
        return new MessageNewsPresenter(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_news);
        ButterKnife.bind(this);
        tvTopTitle.setText("消息");
        listMentiys.clear();
        httpMassageData(1);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                listMentiys.clear();
                httpMassageData(1);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                httpMassageData(page);
            }
        });
    }

    private void httpMassageData(int page) {
        NetService.getInstance().getMessageList(page, 20)
                .subscribe(new CustomApiCallback<List<MassageListMentiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        closeRefresh();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(MessageNewsActivity.this, ex.getDisplayMessage(), 1000);
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
                    public void onNext(List<MassageListMentiy> massageListMentiys) {
                        LogUtils.e(massageListMentiys.toString());
                        closeRefresh();
                        listMentiys = massageListMentiys;
                        rvComment.setLayoutManager(new GridLayoutManager(MessageNewsActivity.this, 1, GridLayoutManager.VERTICAL, false));
                        messageNewsAdapter = new MessageNewsAdapter(MessageNewsActivity.this, listMentiys);
                        rvComment.setAdapter(messageNewsAdapter);
                    }
                });
    }


    @OnClick({R.id.img_top_back, R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
                finish();
                break;
            case R.id.rl_error:
                listMentiys.clear();
                httpMassageData(1);
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }

    public void closeRefresh() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }
}
