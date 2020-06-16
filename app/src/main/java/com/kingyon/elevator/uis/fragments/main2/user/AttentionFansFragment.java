package com.kingyon.elevator.uis.fragments.main2.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.OrderDetailsEntity;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessageAttentionAdapter;
import com.kingyon.elevator.uis.adapters.adaptertwo.order.OrderAdapter;
import com.kingyon.elevator.uis.fragments.main2.found.utilsf.FoundFragemtUtils;
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
import butterknife.Unbinder;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MAIN2_LOGIN;

/**
 * @Created By Admin  on 2020/6/16
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:
 */
public class AttentionFansFragment extends FoundFragemtUtils {
    String type;
    int page = 1;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    Unbinder unbinder;
    List<AttenionUserEntiy> list = new ArrayList<>();
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rl_notlogin;
    MessageAttentionAdapter attentionAdapter;
    public AttentionFansFragment setIndex(String type) {
        this.type = type;
        return (this);
    }

    @Override
    protected void lazyLoad() {
        if (smartRefreshLayout!=null){
            smartRefreshLayout.autoRefresh(100);
        }else {
            showProgressDialog(getString(R.string.wait));
            list.clear();
            page=1;
            httpAttention(type, page);
        }
    }

    private void httpAttention(String type, int page) {
        NetService.getInstance().setAttention(page, type)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        hideProgress();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(getContext(), ex.getDisplayMessage(), 1000);
                            } else {
                                rvComment.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                                rl_notlogin.setVisibility(View.GONE);
                            }

                        } else if (ex.getCode() == 100200) {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.VISIBLE);
                        } else {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.GONE);
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
                            rl_notlogin.setVisibility(View.GONE);
                        }else {
                            rvComment.setVisibility(View.GONE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.VISIBLE);
                            rl_notlogin.setVisibility(View.GONE);
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
            attentionAdapter = new MessageAttentionAdapter((BaseActivity) getActivity());
            attentionAdapter.addData(list);
            rvComment.setAdapter(attentionAdapter);
            rvComment.setLayoutManager(new GridLayoutManager(getActivity(), 1, GridLayoutManager.VERTICAL, false));

        } else {
            attentionAdapter.addData(list);
            attentionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_attention_fans;
    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @Override
    protected void dealLeackCanary() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        if (type.equals("fans")) {
            llSearch.setVisibility(View.GONE);
        } else {
            llSearch.setVisibility(View.VISIBLE);
        }
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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

        @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_error, R.id.rl_notlogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_error:
                showProgressDialog(getString(R.string.wait));
                httpAttention(type,1);
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setActivity(ACTIVITY_MAIN2_LOGIN);
                break;
        }
    }
}
