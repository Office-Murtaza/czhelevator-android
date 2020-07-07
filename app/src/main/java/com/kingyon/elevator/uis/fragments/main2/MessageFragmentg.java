package com.kingyon.elevator.uis.fragments.main2;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.MassageHomeEntiy;
import com.kingyon.elevator.entities.entities.MassageLitsEntiy;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessageAdapter;
import com.kingyon.elevator.utils.StatusBarUtil;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.fragments.BaseFragment;
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
import butterknife.Unbinder;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_ATTENTION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_COMMENT;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_LIKE;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_MSAGGER;

/**
 * Created By Admin  on 2020/4/14
 * Email : 163235610@qq.com
 * Author:Mrczh
 * Instructions:消息
 */
public class MessageFragmentg extends BaseFragment {
    @BindView(R.id.tv_read)
    TextView tvRead;
    @BindView(R.id.tv_massage_number)
    TextView tvMassageNumber;
    @BindView(R.id.ll_msagger)
    LinearLayout llMsagger;
    @BindView(R.id.tv_attention_number)
    TextView tvAttentionNumber;
    @BindView(R.id.ll_attention)
    LinearLayout llAttention;
    @BindView(R.id.tv_like_number)
    TextView tvLikeNumber;
    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.tv_comment_number)
    TextView tvCommentNumber;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.rcv_list_massage)
    RecyclerView rcvListMassage;
    Unbinder unbinder;
    MessageAdapter messageAdapter;
    @BindView(R.id.rl_bj)
    LinearLayout rlBj;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    private int page = 1;
    private List<MassageLitsEntiy> list = new ArrayList<>();
    @Override
    public int getContentViewId() {
        return R.layout.fragment_message;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        StatusBarUtil.setHeadViewPadding(getActivity(), rlBj);
        httpHomeData(1);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                list.clear();
                httpHomeData(1);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                httpHomeData(page);
            }
        });

    }

    private void httpHomeData(int page) {
        NetService.getInstance().getMsgOverview(page,20)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<MassageHomeEntiy<MassageLitsEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        closeRefresh();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(getContext(), ex.getDisplayMessage(), 1000);
                            } else {
                                rcvListMassage.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                                rlNotlogin.setVisibility(View.GONE);
                            }

                        } else if (ex.getCode()==100200){
                            rcvListMassage.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rlNotlogin.setVisibility(View.VISIBLE);
                        }else {
                            rcvListMassage.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rlNotlogin.setVisibility(View.GONE);
                        }
                    }
                    @Override
                    public void onNext(MassageHomeEntiy<MassageLitsEntiy> conentEntity) {
                        hideProgress();
                        closeRefresh();
                        rcvListMassage.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        rlNotlogin.setVisibility(View.GONE);
                        dataAdd(conentEntity);
                    }
                });
    }

    private void dataAdd(MassageHomeEntiy<MassageLitsEntiy> conentEntity) {
        for (int i = 0; i < conentEntity.pushMessage.size(); i++) {
            MassageLitsEntiy massageLitsEntiy = new MassageLitsEntiy();
            massageLitsEntiy = conentEntity.pushMessage.get(i);
            list.add(massageLitsEntiy);
        }
        if (messageAdapter == null || page == 1) {
            messageAdapter = new MessageAdapter((BaseActivity) getActivity());
            messageAdapter.addData(list);
            rcvListMassage.setAdapter(messageAdapter);
            rcvListMassage.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));
        } else {
            messageAdapter.addData(list);
            messageAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void dealLeackCanary() {
    }
    
    public void closeRefresh() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick({R.id.tv_read, R.id.ll_msagger, R.id.ll_attention, R.id.ll_like, R.id.ll_comment,R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_read:
                /*全部已读*/

                break;
            case R.id.ll_msagger:
                ActivityUtils.setActivity(ACTIVITY_MASSAGE_MSAGGER);
                break;
            case R.id.ll_attention:
                ActivityUtils.setActivity(ACTIVITY_MASSAGE_ATTENTION);
                break;
            case R.id.ll_like:
                ActivityUtils.setActivity(ACTIVITY_MASSAGE_LIKE);
                break;
            case R.id.ll_comment:
                ActivityUtils.setActivity(ACTIVITY_MASSAGE_COMMENT);
                break;
            case R.id.rl_error:

                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
