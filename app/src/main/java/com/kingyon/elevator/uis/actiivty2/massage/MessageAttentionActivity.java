package com.kingyon.elevator.uis.actiivty2.massage;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.blankj.utilcode.util.LogUtils;
import com.czh.myversiontwo.activity.ActivityUtils;
import com.kingyon.elevator.R;
import com.kingyon.elevator.entities.entities.AttenionUserEntiy;
import com.kingyon.elevator.entities.entities.ConentEntity;
import com.kingyon.elevator.nets.CustomApiCallback;
import com.kingyon.elevator.nets.NetService;
import com.kingyon.elevator.uis.dialogs.DeleMassageDialog;
import com.kingyon.elevator.uis.dialogs.NotAttentionDialog;
import com.kingyon.elevator.utils.utilstwo.ConentUtils;
import com.kingyon.elevator.utils.utilstwo.IsSuccess;
import com.kingyon.elevator.utils.utilstwo.MassageUtils;
import com.kingyon.elevator.view.DialogOnClick;
import com.leo.afbaselibrary.nets.exceptions.ApiException;
import com.leo.afbaselibrary.nets.exceptions.ResultException;
import com.leo.afbaselibrary.uis.activities.BaseStateRefreshingLoadingActivity;
import com.leo.afbaselibrary.uis.adapters.BaseAdapter;
import com.leo.afbaselibrary.uis.adapters.MultiItemTypeAdapter;
import com.leo.afbaselibrary.uis.adapters.holders.CommonHolder;
import com.leo.afbaselibrary.utils.GlideUtils;
import com.leo.afbaselibrary.utils.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.czh.myversiontwo.utils.Constance.ACTIVITY_MASSAGE_ATTENTION;
import static com.czh.myversiontwo.utils.Constance.ACTIVITY_USER_CENTER;

/**
 * @Created By Admin  on 2020/4/22
 * @Email : 163235610@qq.com
 * @Author:Mrczh
 * @Instructions:消息关注
 */
@Route(path = ACTIVITY_MASSAGE_ATTENTION)
public class MessageAttentionActivity extends BaseStateRefreshingLoadingActivity<AttenionUserEntiy> {
    @BindView(R.id.pre_tv_title)
    TextView preTvTitle;
    @BindView(R.id.pre_v_right)
    TextView preVRight;

    @Override
    protected MultiItemTypeAdapter<AttenionUserEntiy> getAdapter() {
        return new BaseAdapter<AttenionUserEntiy>(this, R.layout.itme_message_atention, mItems) {
            @Override
            protected void convert(CommonHolder holder, AttenionUserEntiy item, int position) {
                GlideUtils.loadCircleImage(MessageAttentionActivity.this, item.photo, holder.getView(R.id.img_portrait));
                holder.setText(R.id.tv_name, item.nickName);
                if (item.personalizedSignature!=null) {
                    holder.setText(R.id.tv_conent, item.personalizedSignature);
                }else {
                    holder.setVisible(R.id.tv_conent,false);
                }
                if (item.focusOther == 1) {
                    holder.setTextColor(R.id.tv_attention, Color.parseColor("#FF3049"));
                    holder.setBackgroundRes(R.id.tv_attention, R.drawable.message_attention_bj);
                    holder.setText(R.id.tv_attention, "已关注");
                    holder.setTag(R.id.tv_attention, "已关注");
                } else {
                    holder.setTextColor(R.id.tv_attention, Color.parseColor("#ffffff"));
                    holder.setBackgroundRes(R.id.tv_attention, R.drawable.message_attention_bj1);
                    holder.setText(R.id.tv_attention, "关注");
                    holder.setTag(R.id.tv_attention, "关注");
                }
                holder.setOnClickListener(R.id.tv_attention, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.getView(R.id.tv_attention).getTag().equals("关注")) {
                            ConentUtils.httpAddAttention(MessageAttentionActivity.this, "add", item.followerAccount, new ConentUtils.IsAddattention() {
                                @Override
                                public void onisSucced() {
                                    holder.setTextColor(R.id.tv_attention, Color.parseColor("#FF3049"));
                                    holder.setBackgroundRes(R.id.tv_attention, R.drawable.message_attention_bj);
                                    holder.setText(R.id.tv_attention, "已关注");
                                    holder.setTag(R.id.tv_attention, "已关注");
                                    item.isAttention = 1;
                                }

                                @Override
                                public void onErron(String magssger, int code) {
                                    ToastUtils.showToast(MessageAttentionActivity.this, magssger, 1000);
                                }
                            });
                        } else {
                            NotAttentionDialog notAttentionDialog = new NotAttentionDialog(MessageAttentionActivity.this, new NotAttentionDialog.OnClick() {
                                @Override
                                public void onclick() {
                                    LogUtils.e("212121332");
                                    ConentUtils.httpAddAttention(MessageAttentionActivity.this, "cancel", item.followerAccount, new ConentUtils.IsAddattention() {
                                        @Override
                                        public void onisSucced() {
                                            holder.setTextColor(R.id.tv_attention, Color.parseColor("#ffffff"));
                                            holder.setBackgroundRes(R.id.tv_attention, R.drawable.message_attention_bj1);
                                            holder.setText(R.id.tv_attention, "关注");
                                            holder.setTag(R.id.tv_attention, "关注");
                                            item.isAttention = 0;
                                        }

                                        @Override
                                        public void onErron(String magssger, int code) {
                                            ToastUtils.showToast(MessageAttentionActivity.this, magssger, 1000);
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
        ActivityUtils.setActivity(ACTIVITY_USER_CENTER, "type", "1", "otherUserAccount", item.followerAccount);

    }

    @Override
    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, AttenionUserEntiy item, int position) {

        DeleMassageDialog dialog = new DeleMassageDialog(MessageAttentionActivity.this);
        dialog.show();
        dialog.setDialogOnClick(new DialogOnClick() {
            @Override
            public void onClick() {
                showProgressDialog(getString(R.string.wait),true);
                dialog.dismiss();
                MassageUtils.httpremoveLikeMsg(String.valueOf(item.id), new IsSuccess() {
                    @Override
                    public void isSuccess(boolean success) {
                        hideProgress();
                        if (success){
                            autoRefresh();
                            showToast("删除成功");
                        }else {
                            showToast("删除失败");
                        }
                    }
                });
            }
        });
        return true;
    }

    @Override
    protected void loadData(int page) {
        NetService.getInstance().getFollowerList(page, 20)
                .subscribe(new CustomApiCallback<ConentEntity<AttenionUserEntiy>>() {
                    @Override
                    protected void onResultError(ApiException ex) {
                        showToast(ex.getDisplayMessage());
                        loadingComplete(false, 100000);
                    }

                    @Override
                    public void onNext(ConentEntity<AttenionUserEntiy> massageListMentiys) {
                        if (massageListMentiys == null || massageListMentiys.getContent() == null) {
                            throw new ResultException(9001, "返回参数异常");
                        }
                        if (FIRST_PAGE == page) {
                            mItems.clear();
                        }
                        mItems.addAll(massageListMentiys.getContent());
                        if (page > 1 && massageListMentiys.getContent().size() <= 0) {
                            showToast("已经没有了");
                        }
                        loadingComplete(true, massageListMentiys.getTotalPages());
                    }
                });
    }

    @Override
    protected String getTitleText() {
        return null;
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_message_attention;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        preVRight.setVisibility(View.GONE);
        preTvTitle.setText("新增粉丝");

        ConentUtils.httpGetMarkRead("", "FOLLOWER", "ALL", new IsSuccess() {
            @Override
            public void isSuccess(boolean success) {
                LogUtils.e(success);
            }
        });
    }

    @OnClick(R.id.pre_v_back)
    public void onViewClicked() {
        finish();
    }
}
