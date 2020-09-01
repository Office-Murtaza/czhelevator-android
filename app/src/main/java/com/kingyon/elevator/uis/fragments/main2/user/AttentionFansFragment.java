package com.kingyon.elevator.uis.fragments.main2.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.utils.EditTextUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.adapters.adaptertwo.MessageAttentionAdapter;
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
    String extend;
    @BindView(R.id.img_delete)
    ImageView imgDelete;

    public AttentionFansFragment setIndex(String type) {
        this.type = type;
        return (this);
    }

    @Override
    protected void lazyLoad() {
        if (smartRefreshLayout != null) {
            smartRefreshLayout.autoRefresh(100);
        } else {
            showProgressDialog(getString(R.string.wait));
            list.clear();
            page = 1;
            httpAttention(type, page, extend);
        }
    }

    private void httpAttention(String type, int page, String extend) {
        NetService.getInstance().setAttention(page, type, extend)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        OrdinaryActivity.closeRefresh(smartRefreshLayout);
                        hideProgress();
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showToast(getContext(), getString(R.string.complete), 1000);
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
                        if (attenionUserEntiyConentEntity.getContent().size() > 0 || page > 1) {
                            rvComment.setVisibility(View.VISIBLE);
                            rlError.setVisibility(View.GONE);
                            rlNull.setVisibility(View.GONE);
                            rl_notlogin.setVisibility(View.GONE);
                        } else {
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
            attentionAdapter = new MessageAttentionAdapter((BaseActivity) getActivity(), type);
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
        editSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Toast.makeText(getContext(), editSearch.getText().toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        EditTextUtils.setEditTextInhibitInputSpace(editSearch);
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
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                list.clear();
                extend = s.toString();
                httpAttention(type, 1, extend);
                if (s.length()>0){
                    imgDelete.setVisibility(View.VISIBLE);
                }else {
                    imgDelete.setVisibility(View.GONE);
                }
            }
        });
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
                httpAttention(type, page, extend);
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.e("onLoadMore");
                page++;
                httpAttention(type, page, extend);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rl_error, R.id.rl_notlogin,R.id.img_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_error:
                showProgressDialog(getString(R.string.wait));
                httpAttention(type, 1, extend);
                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
            case R.id.img_delete:
                editSearch.setText("");
                break;
        }
    }
}
