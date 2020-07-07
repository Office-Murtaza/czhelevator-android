package com.kingyon.elevator.uis.actiivty2.user;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.czh.myversiontwo.utils.QuickClickUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.data.DataSharedPreferences;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.QueryRecommendEntity;
import com.kingyon.elevator.entities.entities.UserTwoEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.activities.user.UserProfileActivity;
import com.kingyon.elevator.uis.adapters.adaptertwo.AttentionAdapter;
import com.kingyon.elevator.uis.dialogs.CoverDialog;
import com.kingyon.elevator.uis.dialogs.ReportShareDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.CodeType.HOME_CONTENT;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;
import static com.czh.myversiontwo.utils.StringContent.ATTENTION_TO_FANS;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:用户中心
 */
@Route(path = ACTIVITY_USER_CENTER)
public class UserCenterActivity extends BaseActivity {
    @BindView(R.id.img_top_back)
    ImageView imgTopBack;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.img_more)
    ImageView imgMore;
    //    @BindView(R.id.rl_top)
//    RelativeLayout rlTop;
    @BindView(R.id.img_placeholder)
    ImageView imgPlaceholder;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_idnumber)
    TextView tvIdnumber;
    @BindView(R.id.tv_attention_number)
    TextView tvAttentionNumber;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.rv_attention_list)
    RecyclerView rvAttentionList;
    @BindView(R.id.rl_error)
    RelativeLayout rlError;
    @BindView(R.id.rl_null)
    RelativeLayout rlNull;
    @BindView(R.id.rl_notlogin)
    RelativeLayout rlNotlogin;
    @BindView(R.id.smart_refresh_layout)
    SmartRefreshLayout smartRefreshLayout;
    @Autowired
    String type;
    @Autowired
    String otherUserAccount;
    UserTwoEntity userTwoEntity1;
    int page = 1;
    AttentionAdapter attentionAdapter;
    List<QueryRecommendEntity> recommendEntityList = new ArrayList<>();
    @BindView(R.id.tv_dtnum)
    TextView tvDtnum;
    @BindView(R.id.img_portrait1)
    ImageView imgPortrait1;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.img_edit)
    ImageView imgEdit;
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout toolbarLayout;
    @BindView(R.id.app_bar)
    AppBarLayout appBar;
    @BindView(R.id.include_toolbar_open)
    View toolbarOpen;
    @BindView(R.id.include_toolbar_close)
    View toolbarClose;
    @BindView(R.id.tv_attention1)
    TextView tvAttention1;
    @BindView(R.id.img_more1)
    ImageView imgMore1;
    @BindView(R.id.img_bj)
    ImageView imgBj;

    @Override
    public int getContentViewId() {
        return R.layout.activity_user_center1;
    }

    @Override
    public void init(Bundle savedInstanceState) {
        ARouter.getInstance().inject(this);
        LogUtils.e(otherUserAccount, DataSharedPreferences.getCreatateAccount());
        if (otherUserAccount.equals(DataSharedPreferences.getCreatateAccount())) {
            imgMore.setImageResource(R.mipmap.ic_personal_edit_white);
            tvAttention.setVisibility(View.GONE);
            imgMore1.setVisibility(View.GONE);
            tvAttention1.setVisibility(View.GONE);
        } else {
            imgMore.setImageResource(R.mipmap.ic_comm_menu_whiteq);
            tvAttention.setVisibility(View.VISIBLE);
            imgMore1.setVisibility(View.VISIBLE);
            imgEdit.setVisibility(View.GONE);
            tvAttention1.setVisibility(View.VISIBLE);
        }
        showProgressDialog(getString(R.string.wait));
        httpData(page, otherUserAccount);
        httpTop(otherUserAccount);

        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                recommendEntityList.clear();
                page = 1;
                httpData(page, otherUserAccount);
                httpTop(otherUserAccount);
                smartRefreshLayout.finishRefresh();
            }
        });
        smartRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                page++;
                httpData(page, otherUserAccount);
                smartRefreshLayout.finishLoadMore();
            }
        });

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                //垂直方向偏移量
                int offset = Math.abs(verticalOffset);
                //最大偏移距离
                int scrollRange = appBarLayout.getTotalScrollRange();
                if (offset <= scrollRange / 2) {
                    toolbarOpen.setVisibility(View.VISIBLE);
                    toolbarClose.setVisibility(View.GONE);
                } else {
                    toolbarClose.setVisibility(View.VISIBLE);
                    toolbarOpen.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        httpTop(otherUserAccount);
    }

    private void httpTop(String otherUserAccount) {
        NetService.getInstance().setUserCenterInfo(otherUserAccount)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<UserTwoEntity>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        hideProgress();
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                    }

                    @Override
                    public void onNext(UserTwoEntity userTwoEntity) {
                        LogUtils.e(userTwoEntity.toString());
                        hideProgress();
                        userTwoEntity1 = userTwoEntity;
                        GlideUtils.loadCircleImage(UserCenterActivity.this, userTwoEntity.photo, imgPlaceholder);
                        GlideUtils.loadCircleImage(UserCenterActivity.this, userTwoEntity.photo, imgPortrait1);

                        tvName.setText(userTwoEntity.nickname + "");
                        tvNickname.setText(userTwoEntity.nickname + "");

                        tvIdnumber.setText("(id:" + userTwoEntity.id + ")");
                        tvAttentionNumber.setText(String.format(ATTENTION_TO_FANS, userTwoEntity.followers, userTwoEntity.beFollowers));
                        tvContent.setText("" + userTwoEntity.personalizedSignature);
                        tvDtnum.setText("全部动态 " + userTwoEntity.contentNum + " 条");
                        if (userTwoEntity.sex.equals("M")) {
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_sexy_man);
                            drawable.setBounds(10, 10, 10, 10);
                            tvName.setCompoundDrawables(null, null, drawable, null);
                        } else {
                            Drawable drawable = getResources().getDrawable(R.mipmap.ic_sexy_woman);
                            drawable.setBounds(10, 10, 10, 10);
                            tvName.setCompoundDrawables(null, null, drawable, null);
                        }
                        if (userTwoEntity.isAttent == 0) {
                            tvAttention.setText("关注");
                            tvAttention1.setText("关注");
                            tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention1));
                            tvAttention.setTextColor(Color.parseColor("#ffffff"));

                        } else {
                            tvAttention.setText("已关注");
                            tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention1));
                            tvAttention.setTextColor(Color.parseColor("#ffffff"));
                            tvAttention1.setText("已关注");
                            tvAttention1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                            tvAttention1.setTextColor(Color.parseColor("#FF1330"));
                        }
                         GlideUtils.loadImage(UserCenterActivity.this, userTwoEntity.coverImgUrl, imgBj, R.mipmap.bg_information, R.mipmap.bg_information);

                    }
                });

    }

    private void httpData(int page, String otherUserAccount) {
        NetService.getInstance().setUserCenterContent(page, otherUserAccount)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<QueryRecommendEntity>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        closeRefresh();
                        hideProgress();
                        LogUtils.e(ex.getDisplayMessage(), ex.getCode());
                        if (ex.getCode() == -102) {
                            if (page > 1) {
                                ToastUtils.showShort("已经没有更多了");
                                smartRefreshLayout.finishLoadMoreWithNoMoreData();
                            } else {
                                rvAttentionList.setVisibility(View.GONE);
                                rlError.setVisibility(View.GONE);
                                rlNull.setVisibility(View.VISIBLE);
                            }
                        } else {
                            rvAttentionList.setVisibility(View.GONE);
                            rlError.setVisibility(View.VISIBLE);
                            rlNull.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onNext(ConentEntity<QueryRecommendEntity> conentEntity) {
                        closeRefresh();
                        hideProgress();
                        rvAttentionList.setVisibility(View.VISIBLE);
                        rlError.setVisibility(View.GONE);
                        rlNull.setVisibility(View.GONE);
                        dataAdd(conentEntity);
                    }
                });
    }

    private void dataAdd(ConentEntity<QueryRecommendEntity> conentEntity) {
        for (int i = 0; i < conentEntity.getContent().size(); i++) {
            QueryRecommendEntity queryRecommendEntity = new QueryRecommendEntity();
            queryRecommendEntity = conentEntity.getContent().get(i);
            recommendEntityList.add(queryRecommendEntity);
        }
        if (attentionAdapter == null || page == 1) {
            attentionAdapter = new AttentionAdapter(this);
            attentionAdapter.addData(recommendEntityList);
            rvAttentionList.setAdapter(attentionAdapter);
            rvAttentionList.setLayoutManager(new GridLayoutManager(this, 1, GridLayoutManager.VERTICAL, false));
        } else {
            attentionAdapter.addData(recommendEntityList);
            attentionAdapter.notifyDataSetChanged();
        }
        rvAttentionList.setNestedScrollingEnabled(false);
    }

    public void closeRefresh() {
        smartRefreshLayout.finishRefresh();
        smartRefreshLayout.finishLoadMore();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @OnClick({R.id.img_top_back, R.id.tv_attention, R.id.img_more, R.id.rl_error, R.id.rl_notlogin,
            R.id.tv_attention1, R.id.img_top_back1, R.id.img_edit, R.id.img_more1, R.id.img_bj})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.img_top_back:
            case R.id.img_top_back1:
                finish();
                break;
            case R.id.tv_attention:
            case R.id.tv_attention1:
                httpAddUser();
                break;
            case R.id.img_more:
            case R.id.img_edit:
            case R.id.img_more1:
                if (otherUserAccount.equals(DataSharedPreferences.getCreatateAccount())) {
                    startActivity(UserProfileActivity.class);
                } else {
                    /*举报*/
                    ReportShareDialog reportShareDialog = new ReportShareDialog(this, 1, HOME_CONTENT);
                    reportShareDialog.show();
                }
                break;
            case R.id.rl_error:

                break;
            case R.id.rl_notlogin:
                ActivityUtils.setLoginActivity();
                break;
            case R.id.img_bj:
                if (otherUserAccount.equals(DataSharedPreferences.getCreatateAccount())) {
                    if (QuickClickUtils.isFastClick()) {
                        CoverDialog coverDialog = new CoverDialog(this);
                        coverDialog.show();
                    }
                }
                break;
        }
    }

    private void httpAddUser() {
        if (tvAttention.getText().toString().equals("关注")) {
            ConentUtils.httpAddAttention(this, "add", userTwoEntity1.account, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    com.leo.afbaselibrary.utils.ToastUtils.showToast(UserCenterActivity.this, "关注成功", 100);
                    tvAttention.setText("已关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention1));
                    tvAttention.setTextColor(Color.parseColor("#ffffff"));
                    tvAttention1.setText("已关注");
                    tvAttention1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_cancel_attention));
                    tvAttention1.setTextColor(Color.parseColor("#FF1330"));
                }

                @Override
                public void onErron(String magssger, int code) {
                    com.leo.afbaselibrary.utils.ToastUtils.showToast(UserCenterActivity.this, magssger + code, 100);
                }
            });
        } else {
            ConentUtils.httpAddAttention(this, "cancel", userTwoEntity1.account, new ConentUtils.IsAddattention() {
                @Override
                public void onisSucced() {
                    com.leo.afbaselibrary.utils.ToastUtils.showToast(UserCenterActivity.this, "取消关注成功", 100);
                    tvAttention.setText("关注");
                    tvAttention.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention1));
                    tvAttention.setTextColor(Color.parseColor("#ffffff"));
                    tvAttention1.setText("关注");
                    tvAttention1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bj_add_attention1));
                    tvAttention1.setTextColor(Color.parseColor("#ffffff"));
                }

                @Override
                public void onErron(String magssger, int code) {
                    com.leo.afbaselibrary.utils.ToastUtils.showToast(UserCenterActivity.this, magssger + code, 100);
                }
            });
        }

    }
}
