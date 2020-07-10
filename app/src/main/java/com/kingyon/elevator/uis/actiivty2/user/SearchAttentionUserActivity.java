package com.kingyon.elevator.uis.actiivty2.user;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.AttentionEntity;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.entities.entities.MassageListMentiy;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.NotAttentionDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseActivity;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_SEARCH_ATTENTION_USERA;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;
import static com.zhaoss.weixinrecorded.util.UIUtils.getResources;

/**
 * @Created By Admin  on 2020/6/17
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:搜索关注
 */
@Route(path = ACTIVITY_SEARCH_ATTENTION_USERA)
public class SearchAttentionUserActivity extends BaseStateRefreshingLoadingActivity<AttenionUserEntiy> {
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.tv_bake)
    TextView tvBake;
    String keyWords;
    @Override
    public int getContentViewId() {
        return R.layout.activity_search_attention_user;
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        super.initViews(savedInstanceState);
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                keyWords = s.toString();

                httpData(1,keyWords);
            }
        });
    }

    @Override
    protected String getTitleText() {
        return "关注";
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_bake})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bake:
                finish();
                break;
        }
    }

    @Override
    protected MultiItemTypeAdapter<AttenionUserEntiy> getAdapter() {
        return new BaseAdapter<AttenionUserEntiy>(this,R.layout.itme_message_atention,mItems) {
            @Override
            protected void convert(CommonHolder holder, AttenionUserEntiy item, int position) {
                GlideUtils.loadCircleImage(SearchAttentionUserActivity.this,item.photo,holder.getView(R.id.img_portrait));
                holder.setText(R.id.tv_name,item.nickname);
                holder.setText(R.id.tv_conent,item.personalizedSignature);
                if (item.isAttention == 1) {
                    holder.setTextColor(R.id.tv_attention,Color.parseColor("#FF3049"));
                    holder.setBackgroundRes(R.id.tv_attention,R.drawable.message_attention_bj);
                    holder.setText(R.id.tv_attention,"已关注");
                    holder.setTag(R.id.tv_attention,"已关注");
                } else {
                    holder.setTextColor(R.id.tv_attention,Color.parseColor("#ffffff"));
                    holder.setBackgroundRes(R.id.tv_attention,R.drawable.message_attention_bj1);
                    holder.setText(R.id.tv_attention,"关注");
                    holder.setTag(R.id.tv_attention,"关注");
                }
                holder.setOnClickListener(R.id.tv_attention,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.getView(R.id.tv_attention).getTag().equals("关注")) {
                            ConentUtils.httpAddAttention(SearchAttentionUserActivity.this, "add", item.beFollowerAccount, new ConentUtils.IsAddattention() {
                                @Override
                                public void onisSucced() {
                                    holder.setTextColor(R.id.tv_attention,Color.parseColor("#FF3049"));
                                    holder.setBackgroundRes(R.id.tv_attention,R.drawable.message_attention_bj);
                                    holder.setText(R.id.tv_attention,"已关注");
                                    item.isAttention = 1;
                                }

                                @Override
                                public void onErron(String magssger, int code) {
                                    ToastUtils.showToast(SearchAttentionUserActivity.this, magssger, 1000);
                                }
                            });
                        } else {
                            NotAttentionDialog notAttentionDialog = new NotAttentionDialog(SearchAttentionUserActivity.this, new NotAttentionDialog.OnClick() {
                                @Override
                                public void onclick() {
                                    LogUtils.e("212121332");
                                    ConentUtils.httpAddAttention(SearchAttentionUserActivity.this, "cancel", item.beFollowerAccount, new ConentUtils.IsAddattention() {
                                        @Override
                                        public void onisSucced() {
                                            holder.setTextColor(R.id.tv_attention,Color.parseColor("#ffffff"));
                                            holder.setBackgroundRes(R.id.tv_attention,R.drawable.message_attention_bj1);
                                            holder.setText(R.id.tv_attention,"关注");
                                            item.isAttention = 0;
                                        }

                                        @Override
                                        public void onErron(String magssger, int code) {
                                            ToastUtils.showToast(SearchAttentionUserActivity.this, magssger, 1000);
                                        }
                                    });
                                }
                            });
                            notAttentionDialog.show();
                        }
                    }
                });
            }
        };
    }

    @Override
    public void onItemClick(View view, RecyclerView.ViewHolder holder, AttenionUserEntiy item, int position) {
        super.onItemClick(view, holder, item, position);
        ActivityUtils.setActivity(ACTIVITY_USER_CENTER, "type", "1","otherUserAccount",item.beFollowerAccount);

    }

    @Override
    protected void loadData(int page) {
        LogUtils.e(page);
        httpData(page,keyWords);
    }

    private void httpData(int page, String keyWords) {
        NetService.getInstance().getMatching(page, keyWords)
                .compose(this.bindLifeCycle())
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }
                    @Override
                    public void onNext(ConentEntity<AttenionUserEntiy> attenionUserEntiyConentEntity) {
                        if (attenionUserEntiyConentEntity == null||attenionUserEntiyConentEntity.getContent()==null ) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(attenionUserEntiyConentEntity.getContent());
                        if (page>1&&attenionUserEntiyConentEntity.getContent().size()<=0){
                            showToast("已经没有了");
                        }
                        loadingComplete(true, attenionUserEntiyConentEntity.getTotalPages());
                    }
                });

    }
}
